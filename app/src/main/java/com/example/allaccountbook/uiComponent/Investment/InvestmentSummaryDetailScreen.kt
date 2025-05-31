package com.example.allaccountbook.uiComponent.Investment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.allaccountbook.uiPersistent.BottomNevBar

@Composable
fun InvestmentSummaryDetailScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {

        Text("A주식 - 투자 내역", modifier = Modifier.padding(bottom = 16.dp))


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("날짜")
            Text("매도/매수")
            Text("매매 금액")
            Text("종목")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // data
        listOf(
            listOf("2025-05-01", "매수", "10,000", "A기업"),
            listOf("2025-05-10", "매도", "12,000", "A기업"),
            listOf("2025-05-15", "매수", "11,000", "A기업")
        ).forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(it[0])
                Text(it[1])
                Text(it[2])
                Text(it[3])
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNevBar()
    }
}
