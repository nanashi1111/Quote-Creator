package com.klmobile.quotescreatorv2

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.klmobile.quotescreatorv2.Screen.Companion.SCREEN_BEST_QUOTES
import com.klmobile.quotescreatorv2.Screen.Companion.SCREEN_CATEGORY_DETAIL
import com.klmobile.quotescreatorv2.Screen.Companion.SCREEN_MENU
import com.klmobile.quotescreatorv2.screens.bestquotes.BestQuotesScreen
import com.klmobile.quotescreatorv2.screens.quotes.bestquotes.BestQuotesViewModel
import com.klmobile.quotescreatorv2.screens.categorydetail.CategoryDetailViewModel
import com.klmobile.quotescreatorv2.screens.menu.MenuScreen
import com.klmobile.quotescreatorv2.screens.quotes.QuoteSharedViewModel
import com.klmobile.quotescreatorv2.screens.quotes.categorydetail.CategoryDetailScreen

sealed class Screen(val name: String) {
  companion object {
    const val SCREEN_MENU = "menu"
    const val SCREEN_BEST_QUOTES = "best_quotes"
    const val SCREEN_CATEGORY_DETAIL = "category_detail"
  }
}

@Composable
fun AppNavHost(navigationController: NavHostController) {
  val sharedQuoteViewModel = hiltViewModel<QuoteSharedViewModel>()
  NavHost(
    navController = navigationController,
    startDestination = SCREEN_MENU
  ) {
    composable(SCREEN_MENU) {
      MenuScreen(onBestQuoteClicked = {
        navigationController.navigate(SCREEN_BEST_QUOTES)
      })
    }
    composable(SCREEN_BEST_QUOTES) {
      val bestQuotesViewModel = hiltViewModel<BestQuotesViewModel>()
      BestQuotesScreen(
        navigationController = navigationController,
        sharedViewModel = sharedQuoteViewModel,
        bestQuotesViewModel = bestQuotesViewModel
      )
    }
    composable(SCREEN_CATEGORY_DETAIL) {
      val categoryDetailViewModel = hiltViewModel<CategoryDetailViewModel>()
      CategoryDetailScreen(
        navHostController = navigationController,
        quoteSharedViewModel = sharedQuoteViewModel,
        categoryDetailViewModel = categoryDetailViewModel
      )
    }
  }

}