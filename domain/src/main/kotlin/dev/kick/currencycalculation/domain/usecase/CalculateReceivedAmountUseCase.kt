package dev.kick.currencycalculation.domain.usecase

import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.model.ExchangeRates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    ): Flow<Double> = flow {
        validateAmount(sendingAmount)
        
        val exchangeRate = exchangeRates.getRate(receivingCurrency)
        val receivedAmount = exchangeRate.calculateAmount(sendingAmount)
        
        emit(receivedAmount)
    }
    
    private fun validateAmount(amount: Double) {
        if (amount.isNaN() || amount.isInfinite()) {
            throw ValidationError.InvalidAmount
        }
        
        if (amount <= MIN_AMOUNT) {
            throw ValidationError.AmountTooSmall
        }
        
        if (amount > MAX_AMOUNT) {
            throw ValidationError.AmountTooLarge
        }
    }
    
    fun getErrorMessage(error: ValidationError): String {
        return when (error) {
            is ValidationError.InvalidAmount,
            is ValidationError.AmountTooSmall,
            is ValidationError.AmountTooLarge -> "송금액이 바르지 않습니다"
        }
    }
}

