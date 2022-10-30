package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.common.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object AsteroidAPI {

    //Configure retrofit to parse JSON and use coroutines
    fun getInstance(): AsteroidService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor(
                    Interceptor {
                        val originalRequest = it.request()
                        val newHttpUrl = originalRequest.url.newBuilder()
                            .addQueryParameter("api_key", BuildConfig.API_KEY)
                            .build()
                        val newRequest = originalRequest.newBuilder()
                            .url(newHttpUrl)
                            .build()
                        it.proceed(newRequest)
                    }
                ).also { client ->
                    if (BuildConfig.DEBUG) {
                        val loggingInterceptor = HttpLoggingInterceptor()
                        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                        client.addInterceptor(loggingInterceptor)
                    }
                }.build()
            )
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(AsteroidService::class.java)
    }
}