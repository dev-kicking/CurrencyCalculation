package dev.kick.currencycalculation.feature.currency.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kick.currencycalculation.domain.model.Currency

@Composable
fun ReceivingCountryDisplay(
    selectedCurrency: Currency,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "수취국가 : ${selectedCurrency.countryName}(${selectedCurrency.code})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

