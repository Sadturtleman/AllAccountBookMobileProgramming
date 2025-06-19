package com.example.allaccountbook.uiComponent.investment


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiComponent.toYearMonth
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InvestmentTrendDetailScreen(
    selectedDate: String,
    category: String,
    navController: NavController,
    viewmodel: TransactionViewModel = hiltViewModel()
) {
    val selectedDateFormat = remember { SimpleDateFormat("yyyy년 M월", Locale.KOREA) }
    val selectedDateObj = selectedDateFormat.parse(selectedDate)

    val transactions = viewmodel.transactions.collectAsState()
    val investList = transactions.value.filterIsInstance<TransactionDetail.Invest>()
        .filter { it.data.date.toYearMonth() == selectedDateObj?.toYearMonth() }
        .filter { it.data.category == category }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("$category - 투자 내역", modifier = Modifier.padding(bottom = 16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("날짜")
            Text("매도/매수")
            Text("매매 금액")
            Text("종목")
        }

        Spacer(modifier = Modifier.height(8.dp))

        investList.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(it.data.date.formatToString())
                Text(it.data.type.label)
                Text((it.data.count * it.data.price).toString())
                Text(it.data.name)
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
