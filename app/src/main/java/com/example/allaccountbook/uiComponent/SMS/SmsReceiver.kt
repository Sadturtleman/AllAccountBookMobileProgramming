package com.example.allaccountbook.uiComponent.SMS

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.allaccountbook.database.AccountDB
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.TransactionType
import com.example.allaccountbook.database.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION && context != null) {
            val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val msgBody = msgs.joinToString(separator = "") { it.messageBody }
            val sender = msgs[0].originatingAddress ?: ""
            Log.d("SmsReceiver", "문자 메시지 내용: $msgBody, 발신자: $sender")

            val parsed = parseSmsMessage(msgBody)
            if (parsed != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val apiKey = "95889668bdd945a3d78d8ce66b45e8fd"
                    val pos = getKakaoPos(parsed.place, apiKey)
                    val category = getKakaoCategoryName(parsed.place, apiKey) ?: "기타"

                    val entity = smsParsedToExpenseEntity(
                        parsed = parsed,
                        transactionId = 0,
                        category = category
                    )

                    val detail = TransactionDetail.Expense(
                        data = entity,
                        latitude = pos?.first,
                        longitude = pos?.second
                    )

                    // ✅ DB 인스턴스에서 필요한 DAO 전부 획득
                    val db = AccountDB.getDBInstance(context.applicationContext)
                    val repository = TransactionRepository(
                        transactionDAO = db.getTransactionDao(),
                        expenseDAO = db.getExpenseDao(),
                        incomeDAO = db.getIncomeDao(),
                        savingDAO = db.getSavingDao(),
                        investDAO = db.getInvestDao()
                    )

                    repository.insertExpenseIfNotExists(detail)
                    Log.d("SmsReceiver", "DB 삽입 시도: $entity, 위치: $pos")
                }
            } else {
                Log.d("SmsReceiver", "SMS 분석 실패")
            }
        }
    }
}
