package com.klmobile.quotescreatorv2

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.klmobile.quotescreatorv2.ui.theme.QuoteCreatorV2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val mainViewModel: MainViewModel by viewModels()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //WindowCompat.setDecorFitsSystemWindows(window, false)

    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

//    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
//      val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
//      view.updatePadding(bottom = insets.bottom)
//      WindowInsetsCompat.CONSUMED
//    }
    setContent {
      QuoteCreatorV2Theme {
        val navHostController = rememberNavController()
        val uiController = rememberSystemUiController()
        val isSystemInDarkMode = isSystemInDarkTheme()

        LaunchedEffect(key1 = Unit) {
          mainViewModel.prepareQuotesIfNeeded()
          mainViewModel.test()
        }

        SideEffect {
          uiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = isSystemInDarkMode
          )
        }
        AppNavHost(navigationController = navHostController)
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  QuoteCreatorV2Theme {
    Greeting("Android")
  }
}