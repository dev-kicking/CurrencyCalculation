package dev.kick.currencycalculation.feature.currency.ui.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface CurrencyUiEffect {
    @Immutable
    data class ShowError(val message: String) : CurrencyUiEffect
}

