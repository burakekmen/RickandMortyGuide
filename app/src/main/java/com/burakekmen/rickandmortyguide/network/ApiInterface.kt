package com.burakekmen.rickandmortyguide.network

import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.model.CharacterResponse
import com.burakekmen.rickandmortyguide.model.EpisodeModel
import com.burakekmen.rickandmortyguide.model.EpisodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {


    @GET("character/{characterId}")
    fun getSingleCharacter(@Path("characterId") characterId: String?): Call<CharacterModel>

    @GET("character/{charactersId}")
    fun getMultiCharacter(@Path("charactersId") charactersId: String?): Call<Array<CharacterModel>>

    @GET("character/")
    fun getCharacterPage(@Query("page") page: Int?): Call<CharacterResponse>

    @GET("character/{characterId}")
    fun getFavouriteCharacter(@Path("characterId") characterId: String?): Call<CharacterModel>

    @GET("character/{characterIds}")
    fun getFavouriteCharacters(@Path("characterIds") characterIds: String?): Call<Array<CharacterModel>>

    @GET("character/")
    fun getCharacterSearch(@Query("page") page: Int?, @Query("name") name: String?): Call<CharacterResponse>

    @GET("character/")
    fun getCharacterSearchWithStatus(@Query("page") page: Int?, @Query("status") status: String?): Call<CharacterResponse>

    @GET("character/")
    fun getCharacterSearchWithNameAndStatus(@Query("page") page: Int?, @Query("name") name: String?, @Query("status") status: String?): Call<CharacterResponse>

    @GET("episode/{episodeId}")
    fun getCharacterEpisode(@Path("episodeId") episodeId: String?): Call<EpisodeModel>

    @GET("episode/{episodesId}")
    fun getCharacterMultiEpisode(@Path("episodesId") episodesId: String?): Call<Array<EpisodeModel>>

    @GET("episode/")
    fun getCharacterEpisodeWithSeason(@Query("episode") episodeQuery: String?): Call<EpisodeResponse>

}