package dev.kick.currencycalculation.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ExchangeRateTest {

    companion object {
        private const val DELTA = 0.01
    }

    @Test
    fun `calculateAmount는 USD 금액에 환율을 곱해 반환`() {
        // Given
        val exchangeRate = ExchangeRate(Currency.KRW, 1450.0)
        val usdAmount = 100.0
        val expectedAmount = 145_000.0

        // When
        val result = exchangeRate.calculateAmount(usdAmount)

        // Then
        assertEquals(
            "USD 금액에 환율을 곱한 값이 올바르게 계산되어야 합니다",
            expectedAmount,
            result,
            DELTA
        )
    }

    @Test
    fun `소수점이 포함된 USD 금액 계산`() {
        // Given
        val exchangeRate = ExchangeRate(Currency.JPY, 156.71)
        val usdAmount = 10.5
        val expectedAmount = 1645.455

        // When
        val result = exchangeRate.calculateAmount(usdAmount)

        // Then
        assertEquals(
            "소수점이 포함된 USD 금액도 올바르게 계산되어야 합니다",
            expectedAmount,
            result,
            DELTA
        )
    }

    @Test
    fun `ExchangeRates getRate는 올바른 환율 반환`() {
        // Given
        val exchangeRates = ExchangeRates(
            krw = ExchangeRate(Currency.KRW, 1450.0),
            jpy = ExchangeRate(Currency.JPY, 156.71),
            php = ExchangeRate(Currency.PHP, 59.28),
            timestamp = 1234567890L
        )

        // When & Then
        val krwRate = exchangeRates.getRate(Currency.KRW)
        assertEquals(
            "KRW 환율이 올바르게 반환되어야 합니다",
            1450.0,
            krwRate.rate,
            DELTA
        )
        assertEquals("KRW Currency가 올바르게 반환되어야 합니다", Currency.KRW, krwRate.currency)

        val jpyRate = exchangeRates.getRate(Currency.JPY)
        assertEquals(
            "JPY 환율이 올바르게 반환되어야 합니다",
            156.71,
            jpyRate.rate,
            DELTA
        )
        assertEquals("JPY Currency가 올바르게 반환되어야 합니다", Currency.JPY, jpyRate.currency)

        val phpRate = exchangeRates.getRate(Currency.PHP)
        assertEquals(
            "PHP 환율이 올바르게 반환되어야 합니다",
            59.28,
            phpRate.rate,
            DELTA
        )
        assertEquals("PHP Currency가 올바르게 반환되어야 합니다", Currency.PHP, phpRate.currency)

        val usdRate = exchangeRates.getRate(Currency.USD)
        assertEquals(
            "USD 환율은 1.0이어야 합니다",
            1.0,
            usdRate.rate,
            DELTA
        )
        assertEquals("USD Currency가 올바르게 반환되어야 합니다", Currency.USD, usdRate.currency)
    }
}

