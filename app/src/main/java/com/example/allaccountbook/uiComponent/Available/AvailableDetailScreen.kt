package com.example.allaccountbook.uiComponent.Available

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog

data class MonthlyCategoryInfo(
    val month: String,
    val categoryName: String,
    val remainingAmount: Int,
    val maxAmount: Int
) {
    val remainingPercent: Int
        get() = if (maxAmount == 0) 0 else (remainingAmount * 100 / maxAmount)
}
@Preview(showBackground = true)
@Composable
fun AvailableDetailScreenPreview() {
    val navController = rememberNavController()
    AvailableDetailScreen(navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableDetailScreen(navController: NavController) {
    var selectedMonth by remember { mutableStateOf("2025-05") }
    var showDateDialog by remember { mutableStateOf(false) }

    val allData = listOf(
        MonthlyCategoryInfo("2025-05",  "A", 30000, 40000),
        MonthlyCategoryInfo("2025-05", "B", 15000, 30000),
        MonthlyCategoryInfo("2025-04",  "C", 20000, 40000),
        MonthlyCategoryInfo("2025-04",  "D", 10000, 25000),
        MonthlyCategoryInfo("2025-05", "E", 10000, 10000)
    )

    val filteredList = remember(selectedMonth) {
        allData.filter { it.month == selectedMonth }
    }

    val averagePercent =
        if (filteredList.isEmpty()) 0 else filteredList.map { it.remainingPercent }.average().toInt()
    val totalRemaining = filteredList.sumOf { it.remainingAmount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedMonth,
                modifier = Modifier
                    .clickable { showDateDialog = true }
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge
            )


        }


        CustomDatePickerDialog(
            showDialog = showDateDialog,
            onDismiss = { showDateDialog = false },
            onDateSelected = {
                selectedMonth = it.substring(0, 7)
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = averagePercent / 100f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFC107), RoundedCornerShape(8.dp))
            )

            Text(
                text = "남은 사용량 : $averagePercent%",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }


        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("카테고리", fontWeight = FontWeight.SemiBold)
            Text("남은 금액", fontWeight = FontWeight.SemiBold)
            Text("남은 비율", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(filteredList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            navController.currentBackStackEntry?.savedStateHandle?.set("categoryName", item.categoryName)
                            navController.currentBackStackEntry?.savedStateHandle?.set("remaining", item.remainingAmount)
                            navController.currentBackStackEntry?.savedStateHandle?.set("max", item.maxAmount)
                            navController.navigate("categoryDetail")
                        }
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.categoryName)
                    Text("${item.remainingAmount}원")
                    Text(
                        "${item.remainingPercent}%",
                        modifier = Modifier
                            .background(Color(0xFFA8F0A3))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text("전체 남은 금액: ${totalRemaining}원", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }

}
