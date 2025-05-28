import android.R.attr.data
import android.R.attr.enabled
import android.R.attr.type
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allaccountbook.uiPersistent.BottomNevBar
import com.example.allaccountbook.uiPersistent.showDate
import kotlinx.coroutines.selects.select

// 추가 뒤로가기
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LendBorrowListScreen(selectedDate : String) {
    var typeOptions = listOf("빌려준 목록", "빌린 목록")
    var selectedType by remember { mutableStateOf(typeOptions[0]) }
    var showUnpaidOnly by remember { mutableStateOf(false) }

    // 예시
    val mockData = listOf(
        Item("책값", "철수", "2025-05-01", false),
        Item("밥값", "영희", "2025-05-02", true),
        Item("택시비", "민수", "2025-05-03", false)
    )

    val filteredData = (if (showUnpaidOnly) mockData.filter { !it.completed } else mockData).toMutableList()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row {
            showDate(selectedDate)
            var expanded by remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.width(100.dp))
            Box(modifier = Modifier.height(28.dp)){
                // (추가) 빌려준 금액 선택시 빌려준 금액만 나오고 빌린 금액 선택시 빌린 금액만 나옴 따로 관리 (초기값 : 빌려준 목록)
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    TextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        typeOptions.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = {
                                selectedType = option
                                expanded = false
                            })
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showUnpaidOnly, onCheckedChange = { showUnpaidOnly = it })
            Text("미완료만 보기")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 헤더
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("사유", fontSize = 16.sp)
            Text("대상", fontSize = 16.sp)
            Text("날짜", fontSize = 16.sp)
            Text("완료여부", fontSize = 16.sp)
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(filteredData) { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.reason)
                    Text(item.target)
                    Text(item.date)
                    Checkbox(
                        checked = item.completed,
                        onCheckedChange = {
                            // (추가) 완료 여부 바뀌고 완료 했을 경우 밑줄
                        }
                    )
                }
            }
        }
        BottomNevBar()
    }

}

// database에서 불러올 것들
data class Item(
    val reason: String,
    val target: String,
    val date: String,
    var completed: Boolean
)

@Preview
@Composable
private fun LBLSPrev() {
    LendBorrowListScreen("2025년 5월")
}