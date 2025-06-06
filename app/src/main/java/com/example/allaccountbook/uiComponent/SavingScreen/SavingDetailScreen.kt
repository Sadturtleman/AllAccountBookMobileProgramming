package com.example.allaccountbook.uiComponent.SavingScreen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.uiPersistent.BottomNavBar

@Preview(showBackground = true)
@Composable
fun SavingDetailScreenPreview() {
    val navController = rememberNavController()
    SavingDetailScreen(navController)
}
data class SavingLog(
    val name: String,
    val amount: Int
)

@Composable
fun SavingDetailScreen(navController: NavController) {
    val goal = SavingGoal(
        name = "여행",
        goalAmount = 100000,
        currentAmount = 60000,
        deadline = "2025-12-31"
    )
    val logs = listOf(
        SavingLog("5월 1일", 30000),
        SavingLog("5월 15일", 30000)
    )
    val achievementRate = goal.achievementRate

    var showGoalDialog by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }


    if (showGoalDialog) {
        SavingGoalDialog(goal = goal, onDismiss = { showGoalDialog = false })
    }

    if (showAmountDialog) {
        SavingAmountDialog(
            navController = navController,
            onDismiss = { showAmountDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("목표명", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.SemiBold)
            Text("목표 금액", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.SemiBold)
            Text("달성률", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(6.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(goal.name, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text("${goal.goalAmount}원", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Column(modifier = Modifier.weight(1f)) {
                Text("${achievementRate}%", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                LinearProgressIndicator(
                    progress = (achievementRate / 100f).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clickable { showGoalDialog = true },
                    color = Color(0xFFA8F0A3),
                    trackColor = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(thickness = 1.dp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("저축 명", fontWeight = FontWeight.SemiBold)
            Text("저축 금액", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(logs) { log ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(log.name)
                    Text(
                        "${log.amount}원",
                        modifier = Modifier.clickable { showAmountDialog = true }
                    )
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

