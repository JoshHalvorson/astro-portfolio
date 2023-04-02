package dev.joshhalvorson.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DownloadApiModule {
    @Provides
    @Singleton
    fun provideDownloadApi() = Retrofit.Builder()
        .baseUrl("https://www.astroportfolio.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DownloadApi::class.java)
}