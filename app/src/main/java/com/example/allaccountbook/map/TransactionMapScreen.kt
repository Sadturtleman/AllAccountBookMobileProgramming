package com.example.allaccountbook.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allaccountbook.database.model.TransactionDetail
import com.example.allaccountbook.viewmodel.view.TransactionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun TransactionMapScreen(viewModel: TransactionViewModel = hiltViewModel()) {
    val transactions by viewModel.transactions.collectAsState()

    val expenseList = transactions.filterIsInstance<TransactionDetail.Expense>()
        .filter { it.latitude != null && it.longitude != null }

    val defaultLocation = LatLng(37.5665, 126.9780) // 서울 시청
    val firstLocation = expenseList.firstOrNull()?.let {
        LatLng(it.longitude!!, it.latitude!!)
    } ?: defaultLocation

    val cameraPositionState = rememberCameraPositionState()

    // 💡 카메라 위치 이동
    LaunchedEffect(firstLocation) {
        cameraPositionState.animate(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(firstLocation, 14f)
            )
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        expenseList.forEachIndexed { index, expense ->
            val position = LatLng(expense.longitude!!, expense.latitude!!)
            val markerState = rememberMarkerState(position = position)
            Log.d("position", "${expense.latitude} ${expense.longitude}")

            Marker(
                state = markerState,
                title = expense.data.name,
                snippet = "₩${expense.data.price} - ${expense.data.category}"
            )

            // 첫 번째 마커만 자동으로 InfoWindow 표시
            if (index == 0) {
                LaunchedEffect(Unit) {
                    markerState.showInfoWindow()
                }
            }
        }
    }
}
