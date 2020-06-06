package com.paras.baseapplication.base.utils.api

import com.google.gson.GsonBuilder
import com.paras.baseapplication.models.ResponseModel
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by parasj on 30/9/18.
 */
class HttpService private constructor() {

    private var apiService: APIService? = null
    private var retrofit: Retrofit? = null

    init {
        // creating GSON with date format for retrofit.
        val gson = GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

        // creating okHttp client for retrofit.
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)

        // building client from okHttpClient
        val client = okHttpClient.build()

        // building retrofit with OkHttpClient, GSON converter factory, RxJava Adapter factory.
        retrofit = Retrofit.Builder().baseUrl("https://newsapi.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).client(client).build()

        // creating apiService
        apiService = retrofit?.create(APIService::class.java)
    }

    companion object {

        private var INSTANCE: HttpService? =
            HttpService()

        fun getInstance(): HttpService? {
            if (INSTANCE == null) {
                synchronized(HttpService::class) {
                    INSTANCE =
                        HttpService()
                }
            }
            return INSTANCE
        }

    }

    /**
     * This method will do the API call VIA retrofit.
     */
    fun getNewsData(pageNumber: Int): Observable<ResponseModel>? {
        return apiService?.getNewsData(pageNumber)
    }
}