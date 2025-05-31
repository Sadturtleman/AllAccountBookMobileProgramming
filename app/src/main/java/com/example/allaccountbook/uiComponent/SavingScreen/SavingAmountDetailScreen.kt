package com.example.allaccountbook.uiComponent.SavingScreen



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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.allaccountbook.uiPersistent.BottomNevBar


@Preview(showBackground = true)
@Composable
fun SavingAmountDetailScreenPreview() {
    SavingAmountDetailScreen()
}
data class SavingDetailLog(
    val date: String,
    val amount: Int,
    val method: String
)

@Composable
fun SavingAmountDetailScreen() {

    val savingName = "여행 적금"
    val logs = listOf(
        SavingDetailLog("2025-05-01", 30000, "통장"),
        SavingDetailLog("2025-05-15", 30000, "카드")
    )

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
            Text("방식", fontWeight = FontWeight.Bold)
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
                    Text("${log.amount}원")
                    Text(log.method)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        BottomNevBar()
    }
}
