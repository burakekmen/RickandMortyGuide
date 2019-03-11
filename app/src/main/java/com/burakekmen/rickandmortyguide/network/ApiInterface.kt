package com.burakekmen.rickandmortyguide.network

import com.burakekmen.rickandmortyguide.model.CharacterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {


    @GET("character/")
    fun getCharacterPage(@Query("page") page: Int?): Call<CharacterResponse>

    @GET("character/{characterIds}")
    fun getFavouriteCharacters(@Path("characterIds") characterIds: String?): Call<CharacterResponse>

    @GET("character/")
    fun getCharacterSearch(@Query("page") page: Int?, @Query("name") name: String?): Call<CharacterResponse>

    @GET("character/")
    fun getCharacterSearchWithStatus(@Query("page") page: Int?, @Query("status") status: String?): Call<CharacterResponse>

    @GET("character/")
    fun getCharacterSearchWithNameAndStatus(@Query("page") page: Int?, @Query("name") name: String?, @Query("status") status: String?): Call<CharacterResponse>

}