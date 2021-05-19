package com.plcoding.doodlekong.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.plcoding.doodlekong.data.remote.api.SetupApi
import com.plcoding.doodlekong.data.remote.ws.CustomGsonMessageAdapter
import com.plcoding.doodlekong.data.remote.ws.DrawingApi
import com.plcoding.doodlekong.data.remote.ws.FlowStreamAdapter
import com.plcoding.doodlekong.repository.DefaultSetupRepository
import com.plcoding.doodlekong.repository.SetupRepository
import com.plcoding.doodlekong.util.Constants
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object ActivityModule {

    @ActivityRetainedScoped
    @Provides
    fun provideSetupRepository(
        setupApi: SetupApi,
        @ApplicationContext context: Context
    ): SetupRepository = DefaultSetupRepository(setupApi, context)

    @ActivityRetainedScoped
    @Provides
    fun provideDrawingApi(
        app: Application,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): DrawingApi {
        return Scarlet.Builder()
            .backoffStrategy(LinearBackoffStrategy(Constants.RECONNECT_INTERVAL))
            .lifecycle(AndroidLifecycle.ofApplicationForeground(app))
            .webSocketFactory(
                okHttpClient.newWebSocketFactory(
                    if(Constants.USE_LOCALHOST) Constants.WS_BASE_URL_LOCALHOST else Constants.WS_BASE_URL
                )
            )
            .addStreamAdapterFactory(FlowStreamAdapter.Factory)
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory(gson))
            .build()
            .create()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideSetupApi(okHttpClient: OkHttpClient): SetupApi {
        return Retrofit.Builder()
            .baseUrl(if (Constants.USE_LOCALHOST) Constants.HTTP_BASE_URL_LOCALHOST else Constants.HTTP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(SetupApi::class.java)
    }
}