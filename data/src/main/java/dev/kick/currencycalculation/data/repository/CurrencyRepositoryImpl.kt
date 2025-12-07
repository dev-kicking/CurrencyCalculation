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

    override fun fetchExchangeRates(apiKey: String): Flow<Result<ExchangeRates>> = flow {
        try {
            val response = apiService.getExchangeRates(apiKey)
            val result = CurrencyMapper.toExchangeRates(response)
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
