package dev.kick.currencycalculation.domain.usecase

import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.model.ExchangeRates
import javax.inject.Inject

class CalculateReceivedAmountUseCase @Inject constructor() {
    
    private companion object {
        const val MIN_AMOUNT = 0.0
        const val MAX_AMOUNT = 10_000.0
    }
    
    sealed class ValidationError : Exception() {
        object InvalidAmount : ValidationError()
        object AmountTooSmall : ValidationError()
        object AmountTooLarge : ValidationError()
    }
    
    operator fun invoke(
        sendingAmount: Double,
        exchangeRates: ExchangeRates,
        receivingCurrency: Currency
    ): Result<Double> {
        val validationResult = validateAmount(sendingAmount)
        if (validationResult != null) {
            return Result.failure(validationResult)
        }
        
        val exchangeRate = exchangeRates.getRate(receivingCurrency)
        val receivedAmount = exchangeRate.calculateAmount(sendingAmount)
        
        return Result.success(receivedAmount)
    }
    
    private fun validateAmount(amount: Double): ValidationError? {
        if (amount.isNaN() || amount.isInfinite()) {
            return ValidationError.InvalidAmount
        }
        
        if (amount <= MIN_AMOUNT) {
            return ValidationError.AmountTooSmall
        }
        
        if (amount > MAX_AMOUNT) {
            return ValidationError.AmountTooLarge
        }
        
        return null
    }
    
    fun getErrorMessage(error: ValidationError): String {
        return when (error) {
            is ValidationError.InvalidAmount,
            is ValidationError.AmountTooSmall,
            is ValidationError.AmountTooLarge -> "송금액이 바르지 않습니다"
        }
    }
}

