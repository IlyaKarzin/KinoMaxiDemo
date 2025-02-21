package ru.maxi.kinomaxi.demo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.maxi.kinomaxi.demo.AppConfig
import ru.maxi.kinomaxi.demo.BuildConfig
import ru.maxi.kinomaxi.demo.accountDetails.data.AccountDetailsApiService
import ru.maxi.kinomaxi.demo.authorization.data.AuthorizationApiService
import ru.maxi.kinomaxi.demo.data.DataStoreRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @IntoSet
    fun provideUrlRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            val url = chain.request().url.newBuilder().addQueryParameter("language", "ru").build()
            val request = chain.request().newBuilder().url(url).build()
            chain.proceed(request)
        }
    }

    @Provides
    @IntoSet
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val url = chain.request().url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .build()
            val request = chain.request().newBuilder().url(url).build()
            chain.proceed(request)
        }
    }

    @Provides
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    @IntoSet
    fun provideSessionInvalidInterceptor(
        dataStoreRepository: dagger.Lazy<DataStoreRepository>
    ): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            if (response.code == 401) {
                CoroutineScope(Dispatchers.IO).launch {
                    dataStoreRepository.get().clearUserPreferences()
                }
            }
            response
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        interceptors.forEach { builder.addInterceptor(it) }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        val jsonMediaType = "application/json; charset=UTF8".toMediaType()
        return Retrofit.Builder().client(httpClient).baseUrl(AppConfig.API_BASE_URL)
            .addConverterFactory(json.asConverterFactory(jsonMediaType)).build()
    }

    @Provides
    @Singleton
    fun provideAuthorizationApiService(retrofit: Retrofit): AuthorizationApiService {
        return retrofit.create(AuthorizationApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAccountDetailsApiService(retrofit: Retrofit): AccountDetailsApiService {
        return retrofit.create(AccountDetailsApiService::class.java)
    }

}