package dev.kick.currencycalculation.data.api

data class CurrencyApiResponse(
    val success: Boolean,
    val terms: String? = null,
    val privacy: String? = null,
    val timestamp: Long? = null,
    val source: String? = null,
    val quotes: Map<String, Double>? = null,
    val error: ErrorInfo? = null
)

data class ErrorInfo(
    val code: Int,
    val info: String
)
