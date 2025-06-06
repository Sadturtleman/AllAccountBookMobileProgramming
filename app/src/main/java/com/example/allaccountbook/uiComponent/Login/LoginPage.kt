package com.example.allaccountbook.uiComponent.Login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun LoginPage(onCorrectNavigate: () -> Unit, onWrongNavigate: () -> Unit) {

    // example
    val sendUserId = "greenjoa"
    val sendUserPasswd = "1234"


    var userId by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val loginresult by remember {
        derivedStateOf { userId == sendUserId && password == sendUserPasswd }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "모두의 가계부",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Row {
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("User ID") }
            )
        }

        Row {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("User Password") }
            )
        }
        Button(
            onClick = {
                if(loginresult) onCorrectNavigate()
                else onWrongNavigate()
            }
        ){
            Text("로그인")
        }
    }
}

@Preview
@Composable
private fun LoginPrev() {
//    LoginPage()
}