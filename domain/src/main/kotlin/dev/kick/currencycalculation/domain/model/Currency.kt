package dev.kick.currencycalculation.domain.model

enum class Currency(
    val code: String,
    val displayName: String,
    val countryName: String
) {
    USD("USD", "USD", "미국"),
    KRW("KRW", "KRW", "한국"),
    JPY("JPY", "JPY", "일본"),
    PHP("PHP", "PHP", "필리핀");

    companion object {
        fun fromCode(code: String): Currency? {
            return values().find { it.code == code }
        }
        
        fun getReceivableCurrencies(): List<Currency> {
            return listOf(KRW, JPY, PHP)
        }
    }
}
