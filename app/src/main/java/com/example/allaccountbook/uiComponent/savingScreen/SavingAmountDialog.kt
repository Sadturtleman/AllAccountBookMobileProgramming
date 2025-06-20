package com.example.allaccountbook.uiComponent.savingScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavingAmountDialog(
    navController: NavController,
    onDismiss: () -> Unit,
    onConfirm: (Int, String) -> Unit,
    savingName: String,
    amount: Int,
    viewmodel: TransactionViewModel = hiltViewModel()
) {
    val allList by viewmodel.transactions.collectAsState()
    val logs = allList.filterIsInstance<TransactionDetail.Saving>()
        .filter { it.data.name == savingName }
        .map {
            SavingDetailLog(
                date = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA).format(it.data.startDate),
                amount = it.data.price
            )
        }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .widthIn(max = 560.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "$savingName",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("날짜", fontWeight = FontWeight.Bold)
                    Text("금액", fontWeight = FontWeight.Bold)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    items(logs) { log ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(log.date, fontSize = 14.sp)
                            Text("%,d원".format(log.amount), fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {


                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        onConfirm(amount, savingName)
                        onDismiss()
                    }) {
                        Text("확인")
                    }
                }
            }
        }
    }
}


