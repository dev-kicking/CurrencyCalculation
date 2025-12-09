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
import dev.kick.currencycalculation.core.util.NumberFormatter
import dev.kick.currencycalculation.domain.model.Currency

@Composable
fun ReceivedAmountResult(
    receivedAmount: Double?,
    currency: Currency,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = if (receivedAmount != null) {
                "수취금액은 ${NumberFormatter.formatCurrency(receivedAmount)} ${currency.code} 입니다."
            } else {
                "수취금액은 - 입니다."
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

