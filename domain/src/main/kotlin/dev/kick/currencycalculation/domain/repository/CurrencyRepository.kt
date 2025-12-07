package dev.kick.currencycalculation.domain.repository

import dev.kick.currencycalculation.domain.model.ExchangeRates
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun fetchExchangeRates(apiKey: String): Flow<Result<ExchangeRates>>
}
