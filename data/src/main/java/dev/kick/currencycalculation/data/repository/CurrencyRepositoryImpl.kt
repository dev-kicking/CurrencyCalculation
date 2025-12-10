package dev.kick.currencycalculation.data.repository

import dev.kick.currencycalculation.data.api.CurrencyApiService
import dev.kick.currencycalculation.data.mapper.CurrencyMapper
import dev.kick.currencycalculation.domain.model.ExchangeRates
import dev.kick.currencycalculation.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrencyApiService
) : CurrencyRepository {

    override fun fetchExchangeRates(apiKey: String): Flow<ExchangeRates> = flow {
        val response = apiService.getExchangeRates(apiKey)
        val exchangeRates = CurrencyMapper.toExchangeRates(response)
        emit(exchangeRates)
    }
}
