package dev.kick.currencycalculation.data.mapper

import dev.kick.currencycalculation.data.api.CurrencyApiResponse
import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.model.ExchangeRate
import dev.kick.currencycalculation.domain.model.ExchangeRates

object CurrencyMapper {
    
    fun toExchangeRates(response: CurrencyApiResponse): ExchangeRates {
        if (!response.success) {
            val errorMessage = response.error?.info ?: "API 응답 실패"
            throw Exception(errorMessage)
        }
        
        val quotes = response.quotes
        if (quotes == null || quotes.isEmpty()) {
            throw Exception("환율 정보가 없습니다.")
        }
        
        val timestamp = response.timestamp ?: System.currentTimeMillis() / 1000
        val krwRate = quotes["USDKRW"] ?: 0.0
        val jpyRate = quotes["USDJPY"] ?: 0.0
        val phpRate = quotes["USDPHP"] ?: 0.0

        if (krwRate == 0.0 || jpyRate == 0.0 || phpRate == 0.0) {
            throw Exception("필수 환율 정보가 누락되었습니다.")
        }

        return ExchangeRates(
            krw = ExchangeRate(Currency.KRW, krwRate),
            jpy = ExchangeRate(Currency.JPY, jpyRate),
            php = ExchangeRate(Currency.PHP, phpRate),
            timestamp = timestamp
        )
    }
}

