package com.example.allaccountbook

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.allaccountbook.navGraph.StartScreen
import com.example.allaccountbook.ui.theme.AllAccountBookTheme
import dagger.hilt.android.AndroidEntryPoint
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import android.content.pm.PackageInfo
import android.content.pm.Signature
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getHashKey(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),

                1
            )
        }

        enableEdgeToEdge()
        setContent {
            AllAccountBookTheme(darkTheme = false){
                StartScreen()
            }
        }
    }
}

@HiltAndroidApp
class MyApp : Application()



fun getHashKey(context: Context) {
    try {
        val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val sig = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            sig
        } else {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
        }

        val signatures: Array<out Signature?>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.signingInfo?.apkContentsSigners
        } else {
            @Suppress("DEPRECATION")
            packageInfo.signatures
        }

        if (signatures != null) {
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature?.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), Base64.NO_WRAP))
                Log.d("HashKey", "Hash Key: $hashKey")
            }
        }

    } catch (e: Exception) {
        Log.e("HashKey", "Error getting hash key", e)
    }
}
