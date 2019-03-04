package com.burakekmen.rickandmortyguide.network

import com.burakekmen.rickandmortyguide.model.Location
import com.burakekmen.rickandmortyguide.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("character/")
    fun getCharacterPage(@Query("page") page: Int?): Call<MutableList<Result>>

    @GET("location/{locationId}")
    fun getLocationDetail(@Path("locationId") locationId: Int?): Call<Location>
}