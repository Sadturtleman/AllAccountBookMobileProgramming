package com.example.allaccountbook.uiComponent.SMS

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.allaccountbook.database.repository.TransactionRepository
import com.example.allaccountbook.database.repository.ExpenseRepository
import com.example.allaccountbook.database.entity.TransactionEntity
import com.example.allaccountbook.database.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver(
    private val transactionRepository: TransactionRepository,
    private val expenseRepository: ExpenseRepository
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val msgBody = msgs.joinToString(separator = "") { it.messageBody }
            val sender = msgs[0].originatingAddress ?: ""
            Log.d("SmsReceiver", "문자 메시지 내용: $msgBody, 발신자: $sender")

            val parsed = parseSmsMessage(msgBody)
            if (parsed != null) {

                CoroutineScope(Dispatchers.IO).launch {
                    val transaction = TransactionEntity(
                        transactionType = TransactionType.EXPENSE
                    )

                    val transactionId = transactionRepository.insertAndGetId(transaction).toInt()

                    val entity = smsParsedToExpenseEntity(parsed, transactionId = transactionId)
                    expenseRepository.insert(entity)
                    Log.d("SmsReceiver", "DB삽입: $entity")
                }
            } else {
                Log.d("SmsReceiver", "분석 실패")
            }
        }
    }
}
