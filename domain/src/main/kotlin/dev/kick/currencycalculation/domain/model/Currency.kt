package dev.kick.currencycalculation.domain.model

enum class Currency(
    val code: String,
    val countryName: String,
) {
    USD("USD", "미국"),
    KRW("KRW", "한국"),
    JPY("JPY", "일본"),
    PHP("PHP", "필리핀");

    companion object {
        fun getReceivableCurrencies(): List<Currency> {
            return listOf(KRW, JPY, PHP)
        }
    }
}
