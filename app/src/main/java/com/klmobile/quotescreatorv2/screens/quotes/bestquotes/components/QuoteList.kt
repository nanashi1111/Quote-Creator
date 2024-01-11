package com.klmobile.quotescreatorv2.screens.quotes.bestquotes.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klmobile.domain.entities.BestQuotesView
import com.klmobile.domain.entities.QuoteCategoryView
import com.klmobile.domain.entities.QuoteContentView
import com.klmobile.quotescreatorv2.R
import com.klmobile.quotescreatorv2.ui.theme.appleBerryFamily
import com.klmobile.quotescreatorv2.ui.theme.latoFamily
import com.klmobile.quotescreatorv2.utils.toast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BestQuoteList(
  modifier: Modifier = Modifier,
  state: LazyListState,
  bestQuotes: List<BestQuotesView>,
  onSelected: ((BestQuotesView) -> Unit)? = null
) {
  LazyColumn(modifier = modifier, state = state) {
    items(bestQuotes) {
      BestQuoteItem(it, onSelected)
      Spacer(modifier = Modifier.height(1.dp))
    }
  }
}

@Composable
@Preview
fun BestQuoteItem(bestQuotesView: BestQuotesView = BestQuotesView(QuoteCategoryView("Love"), QuoteContentView("Love", "I miss you")), onSelected: ((BestQuotesView) -> Unit)? = null) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1.5f)
      .background(Color.Magenta)
      .clickable { onSelected?.invoke(bestQuotesView) }
  ) {
    Image(
      modifier = Modifier.fillMaxSize(),
      painter = painterResource(
        id = if (bestQuotesView.quoteCategoryView.resId == 0) {
          R.drawable.bg_02
        } else {
          bestQuotesView.quoteCategoryView.resId
        }
      ),
      contentDescription = null,
      contentScale = ContentScale.Crop
    )
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.5f))
    )
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
      Text(
        text = bestQuotesView.quoteCategoryView.categoryName,
        style = TextStyle(
          fontFamily = appleBerryFamily, fontSize = 30.sp, color = Color.White.copy(alpha = 0.85f), textAlign = TextAlign.Center
        )
      )
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = bestQuotesView.quoteContentView.content, style = TextStyle(
          fontFamily = latoFamily, fontWeight = FontWeight.Normal, fontSize = 15.sp, color = Color.White.copy(alpha = 0.85f)
        )
      )
    }
  }

}