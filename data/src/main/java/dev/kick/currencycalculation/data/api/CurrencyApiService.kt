package dev.kick.currencycalculation.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("live")
    suspend fun getExchangeRates(
        @Query("access_key") accessKey: String
    ): CurrencyApiResponse
}
