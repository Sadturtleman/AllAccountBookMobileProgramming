package com.example.allaccountbook.uiComponent.SavingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.temporal.ChronoUnit



data class SavingGoal(
    val name: String,
    val goalAmount: Int,
    val currentAmount: Int,
    val deadline: String
) {
    val achievementRate: Int
        get() = if (goalAmount == 0) 0 else (currentAmount * 100 / goalAmount)

    val remainingAmount: Int
        get() = goalAmount - currentAmount

    val remainingDaysPercent: Int
        get() {
            val today = LocalDate.now()
            val endDate = LocalDate.parse(deadline)
            val totalDays = ChronoUnit.DAYS.between(today, endDate).coerceAtLeast(1)
            val elapsedDays = ChronoUnit.DAYS.between(today.minusDays(1), endDate).coerceAtLeast(1)
            return ((totalDays.toFloat() / elapsedDays) * 100).toInt().coerceIn(0, 100)
        }

    val remainingDays: Long
        get() = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(deadline)).coerceAtLeast(0)
}

@Composable
fun SavingGoalDialog(
    goal: SavingGoal,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("닫기")
            }
        },
        title = {
            Text("목표 상세 정보", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("달성률:")
                LinearProgressIndicator(
                    progress = goal.achievementRate / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFFA8F0A3))
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("남은 기간:")
                LinearProgressIndicator(
                    progress = goal.remainingDaysPercent / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFFA8F0A3))
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("목표명: ${goal.name}")
                Text("목표금액: ${goal.goalAmount}원")
                Text("남은 양: ${goal.remainingAmount}원")
                Text("저축 양: ${goal.currentAmount}원")
                Text("기한: ${goal.deadline}")
                Text("남은 기한: ${goal.remainingDays}일")
            }
        }
    )
}
