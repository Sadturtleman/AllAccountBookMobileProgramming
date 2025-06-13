package com.example.allaccountbook.uiComponent.investment


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InvestmentSummaryDialog(
    onDismiss: () -> Unit,
    navController: NavController,
    stockName: String = "A주식"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onDismiss()
                navController.navigate("investmentSummaryDetail")
            }) {
                Text("자세히 보기")
            }
        },
        title = { Text(text = stockName) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 表头
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("종목명")
                    Text("보유 개수")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 假数据列表
                listOf(
                    "A기업" to 3,
                    "B기업" to 2,
                    "C기업" to 4
                ).forEach { (name, qty) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(name)
                        Text("$qty 개")
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 总计
                Text("총 보유 개수: 9개")
                Text("평균 단가: 12,000원")
                Text("총 금액: 108,000원")
            }
        }
    )
}
