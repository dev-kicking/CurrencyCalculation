package dev.kick.currencycalculation.feature.currency.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.usecase.CalculateReceivedAmountUseCase
import dev.kick.currencycalculation.domain.usecase.FetchExchangeRatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import dev.kick.currencycalculation.core.di.CurrencyApiKey
import dev.kick.currencycalculation.feature.currency.ui.model.CurrencyUiEffect
import dev.kick.currencycalculation.feature.currency.ui.model.CurrencyUiState
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val fetchExchangeRatesUseCase: FetchExchangeRatesUseCase,
    private val calculateReceivedAmountUseCase: CalculateReceivedAmountUseCase,
    @CurrencyApiKey private val apiKey: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<CurrencyUiEffect>(Channel.UNLIMITED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        fetchExchangeRates()
    }

    fun selectReceivingCurrency(currency: Currency) {
        _uiState.value = _uiState.value.copy(
            selectedReceivingCurrency = currency,
            receivedAmount = null,
            errorMessage = null
        )
        calculateReceivedAmount()
    }

    fun updateSendingAmount(amount: String) {
        _uiState.value = _uiState.value.copy(
            sendingAmount = amount,
            receivedAmount = null,
            errorMessage = null
        )
        calculateReceivedAmount()
    }

    fun fetchExchangeRates() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        fetchExchangeRatesUseCase(apiKey)
            .onEach { exchangeRates ->
                _uiState.value = _uiState.value.copy(
                    exchangeRates = exchangeRates,
                    isLoading = false
                )
                calculateReceivedAmount()
            }
            .catch { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "환율 정보를 가져오는데 실패했습니다."
                )
            }
            .launchIn(viewModelScope)
    }

    private fun calculateReceivedAmount() {
        val currentState = _uiState.value
        val exchangeRates = currentState.exchangeRates ?: return
        val sendingAmountText = currentState.sendingAmount.trim()

        if (sendingAmountText.isEmpty()) {
            _uiState.value = currentState.copy(receivedAmount = null, errorMessage = null)
            return
        }

        val sendingAmount = sendingAmountText.toDoubleOrNull()
        if (sendingAmount == null) {
            _uiState.value = currentState.copy(
                receivedAmount = null,
                errorMessage = "송금액이 바르지 않습니다"
            )
            return
        }

        calculateReceivedAmountUseCase(
            sendingAmount = sendingAmount,
            exchangeRates = exchangeRates,
            receivingCurrency = currentState.selectedReceivingCurrency
        )
            .onEach { receivedAmount ->
                _uiState.value = currentState.copy(
                    receivedAmount = receivedAmount,
                    errorMessage = null
                )
            }
            .catch { error ->
                val errorMessage = if (error is CalculateReceivedAmountUseCase.ValidationError) {
                    calculateReceivedAmountUseCase.getErrorMessage(error)
                } else {
                    error.message ?: "계산 중 오류가 발생했습니다."
                }
                _uiState.value = currentState.copy(
                    receivedAmount = null,
                    errorMessage = errorMessage
                )
            }
            .launchIn(viewModelScope)
    }
}

