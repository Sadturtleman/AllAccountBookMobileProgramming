package com.example.allaccountbook.NavGraph


import MainScreen
import android.annotation.SuppressLint
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.allaccountbook.uiComponent.Available.AvailableDetailScreen
import com.example.allaccountbook.Available.CategoryMonthlyDetailScreen
import com.example.allaccountbook.Category.CategoryDetailScreen
import com.example.allaccountbook.uiComponent.Add.AddBorrowItemScreen
import com.example.allaccountbook.uiComponent.Investment.InvestmentDetailScreen
import com.example.allaccountbook.uiComponent.Investment.InvestmentSummaryDetailScreen
import com.example.allaccountbook.uiComponent.Investment.InvestmentTrendDetailScreen
import com.example.allaccountbook.uiComponent.Login.LoginPage
import com.example.allaccountbook.uiComponent.Login.WrongLoginPage
import com.example.allaccountbook.uiComponent.SavingScreen.SavingDetailScreen
import com.example.allaccountbook.uiComponent.SavingScreen.SavingAmountDetailScreen
import com.example.allaccountbook.uiComponent.ShowDaily.DailySpendingDetailScreen
import com.example.allaccountbook.uiComponent.LendBorrow.LendBorrowListScreen
import com.example.allaccountbook.uiComponent.PhoneRegisterScreen

@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.LoginNavGraph(navController: NavHostController) {

    composable("login") { LoginPage(
        onCorrectNavigate = { navController.navigate("main") },
        onWrongNavigate = { navController.navigate("wrongLogin") }
    ) }

    composable("wrongLogin") {
        WrongLoginPage(
            onCorrectNavigate = { navController.navigate("main") },
            onWrongNavigate = { navController.navigate("wrongLogin") }
        )
    }

    composable("main") { MainScreen(navController) }
    composable("savingDetail") { SavingDetailScreen(navController) }

    // 투자 상세 페이지 (전체/성향)
    composable("investmentDetail") { InvestmentDetailScreen(navController) }
    composable("investmentTrendDetail") { InvestmentTrendDetailScreen(navController) }
    composable("investmentSummaryDetail") { InvestmentSummaryDetailScreen(navController) }
    // 사용 가능 금액 상세 페이지
    composable("availableDetail") { AvailableDetailScreen(navController) }

    // 카테고리 상세 페이지
    composable("categoryDetail") { CategoryDetailScreen(navController) }

    // 카테고리 월별 상세 페이지
    composable("categoryMonthlyDetail") { CategoryMonthlyDetailScreen(navController) }

    // 저축 금액 "자세히 보기" 페이지
    composable("savingAmountDetail") { SavingAmountDetailScreen(navController) }

    composable("addBorrow") { AddBorrowItemScreen(navController) }

    composable("lendBorrowList/{selectedDate}") {
        val date = it.arguments?.getString("selectedDate") ?: ""

        val displayDate = convertToKoreanDate(date)
        LendBorrowListScreen(selectedDate = displayDate, navController = navController)
    }

   
    composable("dailyDetail/{date}") { backStackEntry ->
        val date = backStackEntry.arguments?.getString("date") ?: ""
        val displayDate = convertToKoreanDate(date)
        DailySpendingDetailScreen(selectedDate = displayDate, navController = navController)
    }

  
    composable("lendBorrowList/{date}") { backStackEntry ->
        val date = backStackEntry.arguments?.getString("date") ?: ""
        val displayDate = convertToKoreanDate(date)
        LendBorrowListScreen(selectedDate = displayDate, navController = navController)
    }
    composable("phoneRegister") { PhoneRegisterScreen() }
}


fun convertToKoreanDate(date: String): String {
    return try {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.KOREA)
        val sdfKorean = java.text.SimpleDateFormat("yyyy년 MM월 dd일", java.util.Locale.KOREA)
        val d = sdf.parse(date)
        if (d != null) sdfKorean.format(d) else date
    } catch (e: Exception) {
        date
    }
}
