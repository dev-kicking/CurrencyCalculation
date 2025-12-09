package dev.kick.currencycalculation.core.util

import java.text.DecimalFormat

object NumberFormatter {
    
    private val decimalFormat = DecimalFormat("#,##0.00")
    
    fun formatCurrency(amount: Double): String {
        return decimalFormat.format(amount)
    }
}

