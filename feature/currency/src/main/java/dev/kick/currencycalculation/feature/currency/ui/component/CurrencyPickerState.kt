package dev.kick.currencycalculation.feature.currency.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.kick.currencycalculation.domain.model.Currency

@Stable
class CurrencyPickerState {
    var selectedCurrency by mutableStateOf<Currency?>(null)
}

@Composable
fun rememberCurrencyPickerState() = remember { CurrencyPickerState() }