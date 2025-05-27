import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allaccountbook.uiComponent.ButtonNevBar
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedDate by remember { mutableStateOf("2025년 5월") }
    var showDateDialog by remember { mutableStateOf(false) }
    var usagePercent by remember { mutableStateOf(70f) } // 남은 사용률 양

    var getTotalSavings by remember {mutableStateOf(3540000)} // 저축 총계
    var getTotalInvestments by remember {mutableStateOf(3332000)} // 투자 총계
    var getAvailableBalance by remember {mutableStateOf(555100)} // 사용 가능 금액
    val getTotalAmount by remember {
        derivedStateOf {
            getTotalSavings + getTotalInvestments + getAvailableBalance
        }
    } // 전체 총 금액


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
                Text(selectedDate, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Button(onClick = { showDateDialog = true }) {
                    Text("날짜 선택")
                }
            }

            if (showDateDialog) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDateDialog = false },
                    confirmButton = {
                        OutlinedButton(onClick = {
                            val millis = datePickerState.selectedDateMillis
                            if (millis != null) {
                                val date = Date(millis)
                                val format = SimpleDateFormat("yyyy년 M월", Locale.KOREA)
                                selectedDate = format.format(date)
                            }
                            showDateDialog = false
                        }) {
                            Text("확인")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showDateDialog = false }) {
                            Text("취소")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(label = "전체 총 금액", value = formatWithCommas(getTotalAmount), fontsize = 25)
                InfoRow(label = "저축 총계", value = formatWithCommas(getTotalSavings))
                InfoRow(label = "투자 총계", value = formatWithCommas(getTotalInvestments))
                InfoRow(label = "사용 가능 금액", value = formatWithCommas(getAvailableBalance))
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

            // 추가하기 버튼
            Button(
                onClick = { /* 추가하기 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+추가하기")
            }
        }

        ButtonNevBar()
    }
}

@Composable
fun InfoRow(label: String, value: String, fontsize : Int = 20) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = fontsize.sp, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = fontsize.sp)
    }
}

fun formatWithCommas(amount: Int): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount)
}

@Preview
@Composable
private fun MainScreenPrev() {
    MainScreen()
}