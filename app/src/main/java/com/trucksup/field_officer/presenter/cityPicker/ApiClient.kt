package com.trucksup.field_officer.presenter.cityPicker

import com.google.gson.GsonBuilder
import com.trucksup.field_officer.data.services.ApiService
import com.trucksup.field_officer.presenter.utils.CommonApplication
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient() {

    val getClient: ApiService
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .addNetworkInterceptor(provideCacheInterceptor())
                .readTimeout(100, TimeUnit.SECONDS).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(CommonApplication.BASE_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiService::class.java)

        }


    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build()
            response.newBuilder()
               // .header(CACHE_CONTROL, cacheControl.toString())
                .removeHeader("Pragma")
                .build()
        }
    }


}