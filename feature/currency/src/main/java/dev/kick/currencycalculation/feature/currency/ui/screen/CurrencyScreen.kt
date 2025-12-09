package dev.kick.currencycalculation.feature.currency.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kick.currencycalculation.feature.currency.ui.component.CountryScrollSelector
import dev.kick.currencycalculation.feature.currency.ui.component.CurrencyTitle
import dev.kick.currencycalculation.feature.currency.ui.component.ErrorMessageDisplay
import dev.kick.currencycalculation.feature.currency.ui.component.ExchangeRateInfo
import dev.kick.currencycalculation.feature.currency.ui.component.ReceivedAmountResult
import dev.kick.currencycalculation.feature.currency.ui.component.ReceivingCountryDisplay
import dev.kick.currencycalculation.feature.currency.ui.component.SendingAmountField
import dev.kick.currencycalculation.feature.currency.ui.component.SendingCountryDisplay
import dev.kick.currencycalculation.feature.currency.ui.model.CurrencyUiEffect
import dev.kick.currencycalculation.feature.currency.ui.viewmodel.CurrencyViewModel

@Composable
fun CurrencyScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyViewModel = hiltViewModel(),
    showToast: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CurrencyUiEffect.ShowError -> showToast(effect.message)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CurrencyTitle()
            
            SendingCountryDisplay()
            
            ReceivingCountryDisplay(
                selectedCurrency = uiState.selectedReceivingCurrency
            )
            
            ExchangeRateInfo(
                exchangeRates = uiState.exchangeRates,
                selectedCurrency = uiState.selectedReceivingCurrency,
                isLoading = uiState.isLoading
            )
            
            SendingAmountField(
                amount = uiState.sendingAmount,
                onAmountChange = viewModel::updateSendingAmount
            )
            
            ReceivedAmountResult(
                receivedAmount = uiState.receivedAmount,
                currency = uiState.selectedReceivingCurrency
            )
            
            ErrorMessageDisplay(
                message = uiState.errorMessage
            )
            
            CountryScrollSelector(
                selectedCurrency = uiState.selectedReceivingCurrency,
                onCurrencySelected = viewModel::selectReceivingCurrency,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

