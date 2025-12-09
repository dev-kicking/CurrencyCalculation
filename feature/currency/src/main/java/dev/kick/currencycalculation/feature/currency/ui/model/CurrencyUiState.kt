package dev.kick.currencycalculation.feature.currency.ui.model

import androidx.compose.runtime.Immutable
import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.model.ExchangeRates

@Immutable
data class CurrencyUiState(
    val selectedReceivingCurrency: Currency = Currency.KRW,
    val exchangeRates: ExchangeRates? = null,
    val sendingAmount: String = "",
    val receivedAmount: Double? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

