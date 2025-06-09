import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.allaccountbook.uiPersistent.BottomNavBar
import com.example.allaccountbook.uiPersistent.CustomDatePickerDialog
import com.example.allaccountbook.uiPersistent.formatWithCommas
import com.example.allaccountbook.uiPersistent.showDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf("2025년 5월") }
    var showDateDialog by remember { mutableStateOf(false) }
    var usagePercent by remember { mutableStateOf(70f) }

    var getTotalSavings by remember { mutableStateOf(3540000) }
    var getTotalInvestments by remember { mutableStateOf(3332000) }
    var getAvailableBalance by remember { mutableStateOf(555100) }

    val getTotalAmount by remember {
        derivedStateOf {
            getTotalSavings + getTotalInvestments + getAvailableBalance
        }
    }

    var getTotalLentAmount by remember { mutableStateOf(0) }
    var getTotalBorrowedAmount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // 날짜 및 선택
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                showDate(selectedDate)
                Button(onClick = { showDateDialog = true }) {
                    Text("날짜 선택")
                }
            }


            CustomDatePickerDialog(
                showDialog = showDateDialog,
                onDismiss = { showDateDialog = false },
                onDateSelected = { selectedDate = it }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(
                    label = "전체 총 금액",
                    value = formatWithCommas(getTotalAmount),
                    fontsize = 25,
                    modifier = Modifier.clickable { /* 화면 이동 */ }
                )

                InfoRow(
                    label = "저축 총계",
                    value = formatWithCommas(getTotalSavings),
                    modifier = Modifier.clickable {
                        navController.navigate("savingDetail")
                    }
                )


                InfoRow(
                    label = "투자 총계",
                    value = formatWithCommas(getTotalInvestments),
                    modifier = Modifier.clickable { navController.navigate("investmentDetail")/* 화면 이동 */ }
                )

                InfoRow(
                    label = "사용 가능 금액",
                    value = formatWithCommas(getAvailableBalance),
                    modifier = Modifier.clickable {  navController.navigate("availableDetail")/* 화면 이동 */ }
                )

                InfoRow(
                    label = "빌린 전체 금액",
                    value = formatWithCommas(getTotalLentAmount),
                    modifier = Modifier.clickable { navController.navigate("lendBorrowList/${selectedDate}") }
                )

                InfoRow(
                    label = "빌려준 금액",
                    value = formatWithCommas(getTotalBorrowedAmount),
                    modifier = Modifier.clickable { navController.navigate("lendBorrowList/${selectedDate}") }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = usagePercent / 100f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFFF88), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "남은 사용률 : ${usagePercent.toInt()}%",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Button(
                onClick = {
                    navController.navigate("addBorrow") 
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+추가하기")
            }
        }

        BottomNavBar(
            onHomeNavigate = { navController.navigate("home") },
            onDateNavigate = { navController.navigate("date") },
            onMapNavigate = { navController.navigate("map") }
        )
    }
}


@Composable
fun InfoRow(
    label: String,
    value: String,
    fontsize : Int = 20,
    modifier : Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = fontsize.sp, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = fontsize.sp)
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    val navController = rememberNavController()
    MainScreen(navController)
}
