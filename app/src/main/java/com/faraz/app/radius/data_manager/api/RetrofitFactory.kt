package com.faraz.app.radius.data_manager.api

import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit




object RetrofitFactory {

    val retrofit: Retrofit? = null
    val BASE_URL = "https://my-json-server.typicode.com/"

   inline fun<reified T> getRetrofitClient(baseUrl : String) : T {



       val okHttpClient = OkHttpClient.Builder()
               .connectTimeout(30, TimeUnit.SECONDS)
               .readTimeout(30, TimeUnit.SECONDS)
               .retryOnConnectionFailure(true)

               // Logging Interceptor
               .addNetworkInterceptor(
                       HttpLoggingInterceptor(
                               HttpLoggingInterceptor.Logger {
                                   Log.i("APIINTERFACE", ": $it")
                               }
                       ).setLevel(HttpLoggingInterceptor.Level.BASIC))

               .build()

            return Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(T::class.java)

    }
}