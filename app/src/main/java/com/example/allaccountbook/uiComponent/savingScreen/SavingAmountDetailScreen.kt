package com.example.allaccountbook.uiComponent.savingScreen



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Preview(showBackground = true)
@Composable
fun SavingAmountDetailScreenPreview() {
//    SavingAmountDetailScreen()
}
data class SavingDetailLog(
    val date: String,
    val amount: Int
)

@Composable
fun SavingAmountDetailScreen(
    navController: NavController,
    savingName: String,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = savingName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("날짜", fontWeight = FontWeight.Bold)
            Text("금액", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn {
            items(logs) { log ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(log.date)
                    Text("%,d원".format(log.amount))
                }
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
