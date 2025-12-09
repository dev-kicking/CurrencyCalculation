package dev.kick.currencycalculation.feature.currency.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorMessageDisplay(
    message: String?,
    modifier: Modifier = Modifier
) {
    if (message != null) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

