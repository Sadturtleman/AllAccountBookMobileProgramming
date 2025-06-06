package com.example.allaccountbook.Category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.Available.CategoryMonthlyDetailScreen
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.allaccountbook.uiPersistent.BottomNavBar

@Preview(showBackground = true)
@Composable
fun CategoryDetailScreenPreview() {
    val navController = rememberNavController()
    CategoryDetailScreen(navController)
}

@Composable
fun CategoryDetailScreen(navController: NavController) {

    val bank = "AAA"

    val max = 40000
    val remaining = 30000
    val used = max - remaining
    val percent = if (max == 0) 0 else (remaining * 100 / max)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$bank ", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        Text("전체 금액: ${max}원")
        Text("사용 금액: $used 원")
        Text("남은 금액: $remaining 원")



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = percent / 100f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFFF88), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    "남은 사용량:$percent%",
                    modifier = Modifier.padding(start = 8.dp),
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            navController.navigate("categoryMonthlyDetail")
        }) {
            Text("상세히보기")
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}
