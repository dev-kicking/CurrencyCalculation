package dev.kick.currencycalculation.data.repository

import dev.kick.currencycalculation.data.api.CurrencyApiService
import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.model.ExchangeRate
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
            
            if (!response.success) {
                val errorMessage = response.error?.info ?: "API 응답 실패"
                emit(Result.failure(Exception(errorMessage)))
                return@flow
            }
            
            val quotes = response.quotes
            if (quotes == null || quotes.isEmpty()) {
                emit(Result.failure(Exception("환율 정보가 없습니다.")))
                return@flow
            }
            
            val timestamp = response.timestamp ?: System.currentTimeMillis() / 1000
            val krwRate = quotes["USDKRW"] ?: 0.0
            val jpyRate = quotes["USDJPY"] ?: 0.0
            val phpRate = quotes["USDPHP"] ?: 0.0

            if (krwRate == 0.0 || jpyRate == 0.0 || phpRate == 0.0) {
                emit(Result.failure(Exception("필수 환율 정보가 누락되었습니다.")))
                return@flow
            }

            val exchangeRates = ExchangeRates(
                krw = ExchangeRate(Currency.KRW, krwRate),
                jpy = ExchangeRate(Currency.JPY, jpyRate),
                php = ExchangeRate(Currency.PHP, phpRate),
                timestamp = timestamp
            )
            
            emit(Result.success(exchangeRates))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
