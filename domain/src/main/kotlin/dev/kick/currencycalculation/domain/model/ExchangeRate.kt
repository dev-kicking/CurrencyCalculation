package dev.kick.currencycalculation.domain.model

data class ExchangeRate(
    val currency: Currency,
    val rate: Double
) {
    fun calculateAmount(usdAmount: Double): Double {
        return usdAmount * rate
    }
}

data class ExchangeRates(
    val krw: ExchangeRate,
    val jpy: ExchangeRate,
    val php: ExchangeRate,
    val timestamp: Long
) {
    fun getRate(currency: Currency): ExchangeRate {
        return when (currency) {
            Currency.KRW -> krw
            Currency.JPY -> jpy
            Currency.PHP -> php
            Currency.USD -> ExchangeRate(Currency.USD, 1.0)
        }
    }
}
