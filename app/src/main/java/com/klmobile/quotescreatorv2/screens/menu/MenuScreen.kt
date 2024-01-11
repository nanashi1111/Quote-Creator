package com.klmobile.quotescreatorv2.screens.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klmobile.quotescreatorv2.R
import com.klmobile.quotescreatorv2.ui.theme.latoFamily
import com.klmobile.quotescreatorv2.ui.theme.painterFamily

@Preview
@Composable
fun MenuScreen(onBestQuoteClicked: (() -> Unit)? = null ) {
  Box(modifier = Modifier.fillMaxSize()) {
    Image(modifier = Modifier.fillMaxSize(), painter = painterResource(id = R.drawable.bg_splash_4), contentScale = ContentScale.Crop, contentDescription = "Splash")
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.5f))
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 100.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
    ) {
      MenuButton(text = "Best quotes") {
        onBestQuoteClicked?.invoke()
      }
      Spacer(modifier = Modifier.height(25.dp))
      MenuButton(text = "Create My Quote") {}
      Spacer(modifier = Modifier.height(25.dp))
      MenuButton(text = "My gallery") {}
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 100.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Quote Creator", style = TextStyle(
          color = Color.White, fontSize = 40.sp, fontFamily = painterFamily
        )
      )
    }
  }
}

@Composable
fun MenuButton(text: String, onButtonClicked: (() -> Unit)? = null) {
  Button(
    onClick = { onButtonClicked?.invoke() },
    modifier = Modifier
      .fillMaxWidth(fraction = 0.5f)
      .height(45.dp),
    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = Color.White),
    shape = RoundedCornerShape(22.dp), border = BorderStroke(1.dp, Color.White)
  ) {
    Text(text = text, style = TextStyle(color = Color.White, fontFamily = latoFamily, fontWeight = FontWeight.Normal))
  }
}