package dev.kick.currencycalculation.feature.currency.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.kick.currencycalculation.domain.model.Currency
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

// 참고 코드 https://www.multiplatform.network/component/jetpack-compose/doc/number-picker
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountryScrollSelector(
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 3,
    itemHeight: Dp = 80.dp,
    dividerColor: Color = Color(0xFF9E9E9E),
) {
    val items = Currency.getReceivableCurrencies()
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2

    val selectedIndex = items.indexOf(selectedCurrency).takeIf { it >= 0 } ?: 0
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + selectedIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collectLatest { currency ->
                onCurrencySelected(currency)
            }
    }

    LaunchedEffect(selectedCurrency) {
        val index = items.indexOf(selectedCurrency)
        if (index >= 0) {
            val targetIndex =
                listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + index
            if (targetIndex != listState.firstVisibleItemIndex && !listState.isScrollInProgress) {
                listState.animateScrollToItem(targetIndex)
            }
        }
    }

    Box(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                val item = getItem(index)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${item.countryName}(${item.code})",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = itemHeight * visibleItemsMiddle)
                .height(1.dp),
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = (itemHeight * visibleItemsMiddle) + itemHeight)
                .height(1.dp),
            color = dividerColor
        )
    }
}

private fun Modifier.fadingEdge(
    brush: Brush,
) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }