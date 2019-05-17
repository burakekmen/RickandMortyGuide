package com.burakekmen.rickandmortyguide.network.api.clients

import com.burakekmen.rickandmortyguide.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val BASE_URL = BuildConfig.API_BASE_URL
    val YOUTUBE_BASE_URL = BuildConfig.YOUTUBE_API_URL

    private var retrofit: Retrofit? = null

    val client: Retrofit?
        get(){
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}