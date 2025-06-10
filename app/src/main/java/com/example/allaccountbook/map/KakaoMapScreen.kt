package com.example.allaccountbook.map

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.allaccountbook.R
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.database.model.getName
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import java.lang.Exception

@Composable
fun TransactionMapScreen(transactions: List<TransactionDetail>) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // 생명주기 대응
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.resume()
                Lifecycle.Event.ON_PAUSE -> mapView.pause()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    AndroidView(
        factory = {
            mapView.apply {
                start(object : MapLifeCycleCallback() {
                    override fun onMapDestroy() {}
                    override fun onMapError(error: Exception) {
                        Log.e("KakaoMap", "지도 오류: ${error.message}")
                    }
                }, object : KakaoMapReadyCallback() {
                    override fun onMapReady(kakaoMap: KakaoMap) {
                        // 마커 스타일 정의
                        val styles = LabelStyles.from(
                            "transactionStyle",
                            LabelStyle.from(R.drawable.bookmark)
                                .setTextStyles(32, Color.BLACK, 1, Color.GRAY)
                                .setZoomLevel(15)
                        )
                        val labelStyles = kakaoMap.labelManager?.addLabelStyles(styles)

                        // 트랜잭션 목록 순회하며 좌표 있는 것만 마커 추가
                        transactions.forEach { detail ->
                            val lat = detail.latitude
                            val lng = detail.longitude
                            if (lat != null && lng != null) {
                                val pos = LatLng.from(lat, lng)
                                val name = detail.getName()
                                val text = LabelTextBuilder()
                                text.setTexts(name)
                                kakaoMap.labelManager?.layer?.addLabel(
                                    LabelOptions.from(pos)
                                        .setStyles(labelStyles)
                                        .setTexts(
                                            text
                                        ) // 부제목 생략 가능
                                )
                            }
                        }

                    }
                })
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

