package com.plcoding.doodlekong.di

import android.content.Context
import com.google.gson.Gson
import com.plcoding.doodlekong.data.remote.api.SetupApi
import com.plcoding.doodlekong.repository.DefaultSetupRepository
import com.plcoding.doodlekong.repository.SetupRepository
import com.plcoding.doodlekong.util.Constants.HTTP_BASE_URL
import com.plcoding.doodlekong.util.Constants.HTTP_BASE_URL_LOCALHOST
import com.plcoding.doodlekong.util.Constants.USE_LOCALHOST
import com.plcoding.doodlekong.util.DispatcherProvider
import com.plcoding.doodlekong.util.clientId
import com.plcoding.doodlekong.util.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSetupRepository(
            setupApi: SetupApi,
            @ApplicationContext context: Context
    ): SetupRepository = DefaultSetupRepository(setupApi, context)

    @Singleton
    @Provides
    fun provideOkHttpClient(clientId: String): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain.request().url.newBuilder()
                            .addQueryParameter("client_id", clientId)
                            .build()
                    val request = chain.request().newBuilder()
                            .url(url)
                            .build()
                    chain.proceed(request)
                }
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
    }

    @Singleton
    @Provides
    fun provideClientId(@ApplicationContext context: Context): String {
        return runBlocking { context.dataStore.clientId() }
    }

    @Singleton
    @Provides
    fun provideSetupApi(okHttpClient: OkHttpClient): SetupApi {
        return Retrofit.Builder()
                .baseUrl(if (USE_LOCALHOST) HTTP_BASE_URL_LOCALHOST else HTTP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(SetupApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApplicationContext(
            @ApplicationContext context: Context
    ) = context

    @Singleton
    @Provides
    fun provideGsonInstance(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }
}