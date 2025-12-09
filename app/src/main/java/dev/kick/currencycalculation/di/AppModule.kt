package dev.kick.currencycalculation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.kick.currencycalculation.BuildConfig
import dev.kick.currencycalculation.core.di.CurrencyApiKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    @CurrencyApiKey
    fun provideCurrencyApiKey(): String {
        return BuildConfig.CURRENCY_API_KEY
    }
}

