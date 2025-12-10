package dev.kick.currencycalculation.domain.usecase

import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.model.ExchangeRate
import dev.kick.currencycalculation.domain.model.ExchangeRates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculateReceivedAmountUseCaseTest {

    private lateinit var useCase: CalculateReceivedAmountUseCase
    
    companion object {
        private val TEST_EXCHANGE_RATES = ExchangeRates(
            krw = ExchangeRate(Currency.KRW, 1450.0),
            jpy = ExchangeRate(Currency.JPY, 156.71),
            php = ExchangeRate(Currency.PHP, 59.28),
            timestamp = 1234567890L
        )
        
        private const val DELTA = 0.01
        private const val MIN_AMOUNT = 0.01
        private const val MAX_AMOUNT = 10_000.0
    }

    @Before
    fun setup() {
        useCase = CalculateReceivedAmountUseCase()
    }

    // ========== 정상 계산 테스트 ==========
    
    @Test
    fun `KRW 수취금액 계산 성공`() {
        // Given
        val sendingAmount = 100.0
        val expectedReceivedAmount = 145_000.0

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertTrue("계산 결과는 성공해야 합니다", result.isSuccess)
        val actualAmount = result.getOrNull()
        assertNotNull("수취금액은 null이 아니어야 합니다", actualAmount)
        assertEquals(
            "KRW 수취금액이 올바르게 계산되어야 합니다",
            expectedReceivedAmount,
            actualAmount!!,
            DELTA
        )
    }

    @Test
    fun `JPY 수취금액 계산 성공`() {
        // Given
        val sendingAmount = 50.0
        val expectedReceivedAmount = 7_835.5

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.JPY)

        // Then
        assertTrue("계산 결과는 성공해야 합니다", result.isSuccess)
        val actualAmount = result.getOrNull()
        assertNotNull("수취금액은 null이 아니어야 합니다", actualAmount)
        assertEquals(
            "JPY 수취금액이 올바르게 계산되어야 합니다",
            expectedReceivedAmount,
            actualAmount!!,
            DELTA
        )
    }

    @Test
    fun `PHP 수취금액 계산 성공`() {
        // Given
        val sendingAmount = 200.0
        val expectedReceivedAmount = 11_856.0

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.PHP)

        // Then
        assertTrue("계산 결과는 성공해야 합니다", result.isSuccess)
        val actualAmount = result.getOrNull()
        assertNotNull("수취금액은 null이 아니어야 합니다", actualAmount)
        assertEquals(
            "PHP 수취금액이 올바르게 계산되어야 합니다",
            expectedReceivedAmount,
            actualAmount!!,
            DELTA
        )
    }

    // ========== 경계값 테스트 ==========
    
    @Test
    fun `최소 금액 경계값에서 계산 성공`() {
        // Given
        val sendingAmount = MIN_AMOUNT
        val expectedReceivedAmount = 14.5

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertTrue("최소 금액에서도 계산이 성공해야 합니다", result.isSuccess)
        val actualAmount = result.getOrNull()
        assertNotNull("수취금액은 null이 아니어야 합니다", actualAmount)
        assertEquals(
            "최소 금액의 수취금액이 올바르게 계산되어야 합니다",
            expectedReceivedAmount,
            actualAmount!!,
            DELTA
        )
    }

    @Test
    fun `최대 금액 경계값에서 계산 성공`() {
        // Given
        val sendingAmount = MAX_AMOUNT
        val expectedReceivedAmount = 14_500_000.0

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertTrue("최대 금액에서도 계산이 성공해야 합니다", result.isSuccess)
        val actualAmount = result.getOrNull()
        assertNotNull("수취금액은 null이 아니어야 합니다", actualAmount)
        assertEquals(
            "최대 금액의 수취금액이 올바르게 계산되어야 합니다",
            expectedReceivedAmount,
            actualAmount!!,
            DELTA
        )
    }

    // ========== 검증 실패 테스트 ==========
    
    @Test
    fun `0 이하 금액은 검증 실패`() {
        // Given
        val sendingAmount = 0.0

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertFalse("0 이하 금액은 실패해야 합니다", result.isSuccess)
        val error = result.exceptionOrNull()
        assertTrue(
            "AmountTooSmall 에러가 발생해야 합니다",
            error is CalculateReceivedAmountUseCase.ValidationError.AmountTooSmall
        )
    }

    @Test
    fun `음수 금액은 검증 실패`() {
        // Given
        val sendingAmount = -100.0

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertFalse("음수 금액은 실패해야 합니다", result.isSuccess)
        val error = result.exceptionOrNull()
        assertTrue(
            "AmountTooSmall 에러가 발생해야 합니다",
            error is CalculateReceivedAmountUseCase.ValidationError.AmountTooSmall
        )
    }

    @Test
    fun `최대 금액 초과는 검증 실패`() {
        // Given
        val sendingAmount = MAX_AMOUNT + 0.01

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertFalse("최대 금액 초과는 실패해야 합니다", result.isSuccess)
        val error = result.exceptionOrNull()
        assertTrue(
            "AmountTooLarge 에러가 발생해야 합니다",
            error is CalculateReceivedAmountUseCase.ValidationError.AmountTooLarge
        )
    }

    @Test
    fun `NaN 값은 검증 실패`() {
        // Given
        val sendingAmount = Double.NaN

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertFalse("NaN 값은 실패해야 합니다", result.isSuccess)
        val error = result.exceptionOrNull()
        assertTrue(
            "InvalidAmount 에러가 발생해야 합니다",
            error is CalculateReceivedAmountUseCase.ValidationError.InvalidAmount
        )
    }

    @Test
    fun `무한대 값은 검증 실패`() {
        // Given
        val sendingAmount = Double.POSITIVE_INFINITY

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertFalse("무한대 값은 실패해야 합니다", result.isSuccess)
        val error = result.exceptionOrNull()
        assertTrue(
            "InvalidAmount 에러가 발생해야 합니다",
            error is CalculateReceivedAmountUseCase.ValidationError.InvalidAmount
        )
    }

    @Test
    fun `음수 무한대 값은 검증 실패`() {
        // Given
        val sendingAmount = Double.NEGATIVE_INFINITY

        // When
        val result = useCase(sendingAmount, TEST_EXCHANGE_RATES, Currency.KRW)

        // Then
        assertFalse("음수 무한대 값은 실패해야 합니다", result.isSuccess)
        val error = result.exceptionOrNull()
        assertTrue(
            "InvalidAmount 에러가 발생해야 합니다",
            error is CalculateReceivedAmountUseCase.ValidationError.InvalidAmount
        )
    }

    // ========== 에러 메시지 테스트 ==========
    
    @Test
    fun `모든 에러 타입의 메시지가 올바르게 반환됨`() {
        // Given
        val invalidAmountError = CalculateReceivedAmountUseCase.ValidationError.InvalidAmount
        val tooSmallError = CalculateReceivedAmountUseCase.ValidationError.AmountTooSmall
        val tooLargeError = CalculateReceivedAmountUseCase.ValidationError.AmountTooLarge

        // When & Then
        assertEquals(
            "InvalidAmount 에러 메시지가 올바르게 반환되어야 합니다",
            "송금액이 바르지 않습니다",
            useCase.getErrorMessage(invalidAmountError)
        )
        assertEquals(
            "AmountTooSmall 에러 메시지가 올바르게 반환되어야 합니다",
            "송금액이 바르지 않습니다",
            useCase.getErrorMessage(tooSmallError)
        )
        assertEquals(
            "AmountTooLarge 에러 메시지가 올바르게 반환되어야 합니다",
            "송금액이 바르지 않습니다",
            useCase.getErrorMessage(tooLargeError)
        )
    }
}

