package com.klmobile.quotescreatorv2.screens.quotes.categorydetail.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.klmobile.domain.entities.QuoteContentView
import com.klmobile.quotescreatorv2.R
import com.klmobile.quotescreatorv2.ui.theme.appleBerryFamily
import com.klmobile.quotescreatorv2.utils.copy
import com.klmobile.quotescreatorv2.utils.quoteBackgroundList
import com.klmobile.quotescreatorv2.utils.toast

@Composable
@Preview
fun QuoteItem(
  quoteContentView: QuoteContentView = QuoteContentView("", "I love you so much"),
  onShareClick: ((QuoteContentView) -> Unit)? = null,
  onCopyClick: ((QuoteContentView) -> Unit)? = null,
  onEditClick: ((QuoteContentView) -> Unit)? = null
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f)
  ) {
    Image(modifier = Modifier.fillMaxSize(), painter = painterResource(id = quoteContentView.resId), contentDescription = null)
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(0.5f))
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(25.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = quoteContentView.content,
        style = TextStyle(fontFamily = appleBerryFamily, color = Color.White, fontSize = 25.sp, textAlign = TextAlign.Center)
      )
    }
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 5.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom
    ) {

      Row(
        modifier = Modifier
          .width(50.dp)
          .height(50.dp)
          .clickable { onShareClick?.invoke(quoteContentView) },
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
      ) {
        FaIcon(faIcon = FaIcons.Share, size = 24.dp, tint = Color.White)
      }
      Spacer(modifier = Modifier.width(15.dp))
      Row(
        modifier = Modifier
          .width(50.dp)
          .height(50.dp)
          .clickable { onCopyClick?.invoke(quoteContentView) },
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
      ) {
        FaIcon(faIcon = FaIcons.Copy, size = 24.dp, tint = Color.White)
      }
      Spacer(modifier = Modifier.width(15.dp))
      Row(
        modifier = Modifier
          .width(50.dp)
          .height(50.dp)
          .clickable { onEditClick?.invoke(quoteContentView) },
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
      ) {
        FaIcon(faIcon = FaIcons.Edit, size = 24.dp, tint = Color.White)
      }
      Spacer(modifier = Modifier.width(15.dp))
    }
  }

}

@Composable
fun QuoteList(modifier: Modifier, quotes: List<QuoteContentView>) {
  LazyColumn(modifier = modifier) {
    itemsIndexed(quotes) { index, quote ->
      Column(modifier = Modifier.fillMaxWidth()) {
        val activity = LocalContext.current as Activity
        QuoteItem(
          quoteContentView = quote,
          onCopyClick = {
            activity.copy(it.content)
            activity.toast(R.string.copied)
          }
        )
        Spacer(modifier = Modifier.height(1.dp))
      }
    }
  }
}
