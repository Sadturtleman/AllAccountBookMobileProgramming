// PhoneRegisterScreen.kt
package com.example.allaccountbook.uiComponent.SMS

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.edit

@Composable
fun PhoneRegisterScreen() {
    val context = LocalContext.current
    var phone by remember { mutableStateOf("") }
    var phoneList by remember {
        mutableStateOf(getPhoneListFromPrefs(context))
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Text("📱 문자 파싱 대상 번호 등록", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("추가할 번호") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (phone.isNotBlank()) {
                    val updated = phoneList.toMutableSet()
                    updated.add(phone)
                    savePhoneListToPrefs(context, updated)
                    phoneList = updated
                    phone = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("추가")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (phoneList.isNotEmpty()) {
            Text("✅ 저장된 번호 목록:", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            phoneList.forEach { number ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(number)
                    TextButton(onClick = {
                        val updated = phoneList.toMutableSet().apply { remove(number) }
                        savePhoneListToPrefs(context, updated)
                        phoneList = updated
                    }) {
                        Text("삭제", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}




fun getPhoneFromPrefs(context: Context): String? {
    return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .getString("target_phone", null) ?: ""
}

const val PREF_KEY_PHONE_LIST = "target_phone_list"

fun getPhoneListFromPrefs(context: Context): MutableSet<String> {
    return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .getStringSet(PREF_KEY_PHONE_LIST, emptySet())?.toMutableSet() ?: mutableSetOf()
}

fun savePhoneListToPrefs(context: Context, list: Set<String>) {
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit {
        putStringSet(PREF_KEY_PHONE_LIST, list)
    }
}

