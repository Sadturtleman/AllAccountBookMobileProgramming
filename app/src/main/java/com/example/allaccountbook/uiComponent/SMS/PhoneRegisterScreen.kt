// PhoneRegisterScreen.kt
package com.example.allaccountbook.uiComponent

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit

@Composable
fun PhoneRegisterScreen() {
    val context = LocalContext.current
    var phone by remember { mutableStateOf("") }
    val prefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    var saved by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "문자 파싱 대상 번호 등록",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("등록할 번호 ") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                prefs.edit { putString("target_phone", phone) }
                saved = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("저장")
        }
        if (saved) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "저장되었습니다: $phone",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}



fun getPhoneFromPrefs(context: Context): String? {
    return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .getString("target_phone", null) ?: ""
}
