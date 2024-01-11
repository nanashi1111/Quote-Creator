package com.klmobile.quotescreatorv2.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.klmobile.quotescreatorv2.R
import com.klmobile.quotescreatorv2.ui.theme.colorFromHex
import com.klmobile.quotescreatorv2.ui.theme.latoFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppToolbar(title:String = stringResource(id = R.string.categories), onBackClick: (() -> Unit)? = null) {
  TopAppBar(
    title = {
      Text(
        text = title, style = TextStyle(
          fontFamily = latoFamily, fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.White
        )
      )
    },
    navigationIcon = {
      Row(
        modifier = Modifier
          .width(50.dp)
          .height(50.dp)
          .clickable { onBackClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
      ) {
        FaIcon(faIcon = FaIcons.ChevronLeft, size = 24.dp, tint = Color.White)
      }
    },
    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = colorFromHex("1d233a"))
  )
}