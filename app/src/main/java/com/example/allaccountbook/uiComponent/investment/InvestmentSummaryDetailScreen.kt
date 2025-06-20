package com.example.allaccountbook.uiComponent.investment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InvestmentSummaryDetailScreen(
    selectedDate: String,
    name: String,
    navController: NavController,
    viewmodel: TransactionViewModel = hiltViewModel()
) {
    val selectedDateFormat = remember { SimpleDateFormat("yyyy년 M월", Locale.KOREA) }
    val dateFormatter = remember { SimpleDateFormat("MM월 dd일", Locale.KOREA) }
    val selectedDateObj = selectedDateFormat.parse(selectedDate)

    val transactions = viewmodel.transactions.collectAsState()
    val investList = transactions.value
        .filterIsInstance<TransactionDetail.Invest>()
        .filter { selectedDateObj?.toYearMonth() == it.data.date.toYearMonth() }
        .filter { it.data.name == name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "$name - 투자 내역",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("매도/매수", modifier = Modifier.weight(1f))
            Text("매매 금액", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text("날짜", modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        }

        Spacer(modifier = Modifier.height(8.dp))

        investList.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(it.data.type.label)
                    Text(
                        "${it.data.company}, x ${it.data.count}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                
                Text(
                    text = "${it.data.count * it.data.price}원",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                
                Text(
                    text = dateFormatter.format(it.data.date),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}
