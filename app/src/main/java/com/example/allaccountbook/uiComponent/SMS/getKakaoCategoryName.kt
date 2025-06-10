package com.example.allaccountbook.uiComponent.SMS

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder

suspend fun getKakaoCategoryName(
    placeName : String,
    apiKey : String
): String? {
    val encoded = URLEncoder.encode(placeName, "UTF-8")
    val url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=$encoded"

    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "KakaoAK $apiKey")
        .build()

    val response = withContext(Dispatchers.IO) {
        OkHttpClient().newCall(request).execute()
    }

    if (!response.isSuccessful) return null
    val json = JSONObject(response.body?.string() ?: return null)
    val docs = json.getJSONArray("documents")
    if (docs.length() == 0) return null

    return docs.getJSONObject(0).optString("category_group_name") ?: return null
}