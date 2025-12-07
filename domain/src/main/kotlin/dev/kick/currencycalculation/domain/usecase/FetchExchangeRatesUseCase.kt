package dev.kick.currencycalculation.domain.usecase

import dev.kick.currencycalculation.domain.model.ExchangeRates
import dev.kick.currencycalculation.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchExchangeRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    operator fun invoke(apiKey: String): Flow<Result<ExchangeRates>> {
        return repository.fetchExchangeRates(apiKey)
    }
}

