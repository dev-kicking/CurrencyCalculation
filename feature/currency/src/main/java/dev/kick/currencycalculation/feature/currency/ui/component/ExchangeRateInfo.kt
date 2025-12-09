package dev.kick.currencycalculation.feature.currency.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kick.currencycalculation.core.util.NumberFormatter
import dev.kick.currencycalculation.domain.model.Currency
import dev.kick.currencycalculation.domain.model.ExchangeRates
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExchangeRateInfo(
    exchangeRates: ExchangeRates?,
    selectedCurrency: Currency,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            exchangeRates?.let { rates ->
                val exchangeRate = rates.getRate(selectedCurrency)
                val formattedRate = NumberFormatter.formatCurrency(exchangeRate.rate)
                
                Text(
                    text = "환율 : $formattedRate ${selectedCurrency.code} / USD",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val formattedTime = dateFormat.format(Date(rates.timestamp * 1000))
                
                Text(
                    text = "조회시간 : $formattedTime",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } ?: run {
                Text(
                    text = "환율 : 로딩 중...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

