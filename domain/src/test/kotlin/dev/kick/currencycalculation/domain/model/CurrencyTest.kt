package dev.kick.currencycalculation.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CurrencyTest {

    @Test
    fun `getReceivableCurrencies는 KRW JPY PHP만 반환`() {
        // When
        val receivableCurrencies = Currency.getReceivableCurrencies()

        // Then
        assertEquals(
            "수취 가능한 통화는 3개여야 합니다",
            3,
            receivableCurrencies.size
        )
        assertTrue(
            "KRW가 포함되어야 합니다",
            receivableCurrencies.contains(Currency.KRW)
        )
        assertTrue(
            "JPY가 포함되어야 합니다",
            receivableCurrencies.contains(Currency.JPY)
        )
        assertTrue(
            "PHP가 포함되어야 합니다",
            receivableCurrencies.contains(Currency.PHP)
        )
        assertFalse(
            "USD는 포함되지 않아야 합니다",
            receivableCurrencies.contains(Currency.USD)
        )
    }

    @Test
    fun `Currency enum의 속성이 올바르게 설정됨`() {
        // Then
        assertEquals("USD 코드가 올바르게 설정되어야 합니다", "USD", Currency.USD.code)
        assertEquals("USD 국가명이 올바르게 설정되어야 합니다", "미국", Currency.USD.countryName)

        assertEquals("KRW 코드가 올바르게 설정되어야 합니다", "KRW", Currency.KRW.code)
        assertEquals("KRW 국가명이 올바르게 설정되어야 합니다", "한국", Currency.KRW.countryName)

        assertEquals("JPY 코드가 올바르게 설정되어야 합니다", "JPY", Currency.JPY.code)
        assertEquals("JPY 국가명이 올바르게 설정되어야 합니다", "일본", Currency.JPY.countryName)

        assertEquals("PHP 코드가 올바르게 설정되어야 합니다", "PHP", Currency.PHP.code)
        assertEquals("PHP 국가명이 올바르게 설정되어야 합니다", "필리핀", Currency.PHP.countryName)
    }
}

