package com.example.allaccountbook.uiComponent.savingScreen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true)
@Composable
fun SavingDetailScreenPreview() {
    val navController = rememberNavController()
    SavingDetailScreen(navController)
}

data class SavingLog(
    var name: String,
    val date: String,
    val amount: Int,
    val interest: Float
)

@Composable
fun SavingDetailScreen(
    navController: NavController,
    viewmodel: TransactionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("goal_prefs", Context.MODE_PRIVATE) }

    var goalName by remember {
        mutableStateOf(prefs.getString("goal_name", "여행") ?: "여행")
    }
    var goalAmount by remember {
        mutableIntStateOf(prefs.getInt("goal_amount", 100000))
    }
    var showOnlyGoalSaving by remember { mutableStateOf(false) }

    val allList by viewmodel.transactions.collectAsState()
    val fullSavingList = allList.filterIsInstance<TransactionDetail.Saving>()

// ✅ showOnlyGoalSaving에 따라 필터링
    val savingList = if (showOnlyGoalSaving) {
        fullSavingList.filter { it.data.isGoal }
    } else {
        fullSavingList
    }

    val goal = SavingGoal(
        name = goalName,
        goalAmount = goalAmount,
        currentAmount = savingList.filter { it.data.isGoal }.sumOf { it.data.price },
        deadline = "2025-12-31"
    )

    // 🔁 SavingLog ↔ TransactionDetail.Saving 매핑
    val savingLogMap = remember(savingList) {
        savingList.associateBy {
            SavingLog(
                name = it.data.name,
                date = SimpleDateFormat("MM월 dd일", Locale.KOREA).format(it.data.startDate),
                amount = it.data.price,
                interest = it.data.percent
            )
        }
    }
    val logs = savingLogMap.keys.toList()

    val achievementRate = goal.achievementRate
    var showGoalDialog by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }
    var showGoalAmountDialog by remember { mutableStateOf(false) }
    var selectedLog by remember { mutableStateOf<SavingLog?>(null) }

    if (showGoalDialog) {
        SavingGoalDialog(goal = goal, onDismiss = { showGoalDialog = false })
    }
    if (showAmountDialog && selectedLog != null) {
        SavingAmountDialog(
            navController = navController,
            onDismiss = { showAmountDialog = false },
            onConfirm = { newAmount, _ ->
                goalAmount = newAmount
                prefs.edit().putInt("goal_amount", newAmount).apply()
                showAmountDialog = false
            },
            savingName = selectedLog!!.name,
            amount = selectedLog!!.amount
        )
    }
    if (showNameDialog) {
        SavingNameDialog(
            currentName = goalName,
            onDismiss = { showNameDialog = false },
            onConfirm = { newName ->
                goalName = newName
                prefs.edit().putString("goal_name", newName).apply()
                showNameDialog = false
            }
        )
    }
    if (showGoalAmountDialog) {
        GoalAmountDialog(
            currentAmount = goalAmount,
            onDismiss = { showGoalAmountDialog = false },
            onConfirm = { newAmount ->
                goalAmount = newAmount
                prefs.edit().putInt("goal_amount", newAmount).apply()
                showGoalAmountDialog = false
            }
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
            Text("목표명", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
            Text("목표 금액", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
            Text("달성률", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(goal.name, modifier = Modifier.weight(1f).clickable { showNameDialog = true }, textAlign = TextAlign.Center)
            Text("${goal.goalAmount}원", modifier = Modifier.weight(1f).clickable { showGoalAmountDialog = true }, textAlign = TextAlign.Center)
            Column(modifier = Modifier.weight(1f)) {
                Text("${achievementRate}%", textAlign = TextAlign.Center)
                LinearProgressIndicator(
                    progress = (achievementRate / 100f).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clickable {
                            selectedLog = logs.firstOrNull()
                            showGoalDialog = true
                        },
                    color = Color(0xFFA8F0A3),
                    trackColor = Color.LightGray
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showOnlyGoalSaving,
                onCheckedChange = { showOnlyGoalSaving = it }
            )
            Text("목표 저축만 보기")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(thickness = 1.dp, color = Color.Gray)

        val groupedLogs = logs.groupBy { it.date }

        LazyColumn {
            groupedLogs.forEach { (date, dailyLogs) ->
                item {
                    Text(
                        text = date,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(dailyLogs) { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = log.name,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${log.interest}% 연이율",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "%,d원".format(log.amount),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedLog = log
                                    showAmountDialog = true
                                },
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End,
                            fontSize = 18.sp
                        )
                        IconButton(
                            onClick = {
                                savingLogMap[log]?.let {
                                    viewmodel.deleteTransaction(it)
                                }
                            },
                            modifier = Modifier.weight(0.3f)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "삭제",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
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
