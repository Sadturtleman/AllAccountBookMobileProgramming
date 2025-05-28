package com.example.allaccountbook.uiPersistent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNevBar() {
    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )

        // 하단 메뉴 고정
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* (추가) home으로 이동 */ }) { Text("home") }
            Button(onClick = { /* (추가) 날짜별로 이동 */ }) { Text("날짜별") }
            Button(onClick = { /* (추가) 카드별으로 이동 */ }) { Text("카드별") }
            Button(onClick = { /* (추가) map으로 이동 */ }) { Text("map") }
        }
    }
}