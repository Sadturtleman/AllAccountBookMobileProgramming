package com.example.allaccountbook.navGraph

import android.annotation.SuppressLint
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.allaccountbook.uiComponent.MainScreen
import com.example.allaccountbook.uiComponent.SMS.PhoneRegisterScreen
import com.example.allaccountbook.uiComponent.add.AddBorrowItemScreen
import com.example.allaccountbook.uiComponent.available.AvailableDetailScreen
import com.example.allaccountbook.uiComponent.available.CategoryDetailScreen
import com.example.allaccountbook.uiComponent.available.CategoryMonthlyDetailScreen
import com.example.allaccountbook.uiComponent.investment.InvestmentDetailScreen
import com.example.allaccountbook.uiComponent.investment.InvestmentSummaryDetailScreen
import com.example.allaccountbook.uiComponent.investment.InvestmentTrendDetailScreen
import com.example.allaccountbook.uiComponent.lendBorrow.LendBorrowListScreen
import com.example.allaccountbook.uiComponent.login.LoginPage
import com.example.allaccountbook.uiComponent.login.WrongLoginPage
import com.example.allaccountbook.uiComponent.savingScreen.SavingAmountDetailScreen
import com.example.allaccountbook.uiComponent.savingScreen.SavingDetailScreen
import com.example.allaccountbook.uiComponent.showDaily.DailySpendingDetailScreen


@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.LoginNavGraph(navController: NavHostController) {

    composable("login") {
        LoginPage(
            onCorrectNavigate = { navController.navigate("main") },
            onWrongNavigate = { navController.navigate("wrongLogin") }
        )
    }

    composable("wrongLogin") {
        WrongLoginPage(
            onCorrectNavigate = { navController.navigate("main") },
            onWrongNavigate = { navController.navigate("wrongLogin") }
        )
    }

    composable("main") { MainScreen(navController = navController) }
    composable("savingDetail") { SavingDetailScreen(navController) }

    // 투자 상세 페이지 (전체/성향)
    composable("investmentDetail/{selectedDate}") {
        val date = it.arguments?.getString("selectedDate") ?: ""
        val displayDate = convertToKoreanDate(date)

        InvestmentDetailScreen(selectedDate = displayDate, navController = navController)
    }
    composable("investmentTrendDetail/{selectedDate}/{category}") {
        val date = it.arguments?.getString("selectedDate") ?: ""
        val displayDate = convertToKoreanDate(date)

        val category = it.arguments?.getString("category") ?: ""
        InvestmentTrendDetailScreen(
            selectedDate = displayDate,
            category = category,
            navController = navController
        )
    }
    composable("investmentSummaryDetail/{selectedDate}/{name}") {
        val date = it.arguments?.getString("selectedDate") ?: ""
        val displayDate = convertToKoreanDate(date)

        val name = it.arguments?.getString("name") ?: ""
        InvestmentSummaryDetailScreen(
            selectedDate = displayDate,
            name = name,
            navController = navController
        )
    }
    // 사용 가능 금액 상세 페이지
    composable("availableDetail/{selectedDate}") {
        val date = it.arguments?.getString("selectedDate") ?: ""
        val displayDate = convertToKoreanDate(date)


        AvailableDetailScreen(navController, selectedDate = displayDate)
    }

    // 카테고리 상세 페이지
    composable("categoryDetail") { CategoryDetailScreen(navController) }

    // 카테고리 월별 상세 페이지
    composable("categoryMonthlyDetail/{selectedDate}/{category}") {
        val date = it.arguments?.getString("selectedDate") ?: ""
        val displayDate = convertToKoreanDate(date)

        val category = it.arguments?.getString("category") ?: ""
        CategoryMonthlyDetailScreen(
            navController = navController,
            selectedDate = displayDate,
            selectedCategory = category
        )
    }

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
