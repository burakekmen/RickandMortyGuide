package com.burakekmen.rickandmortyguide.network.api.interfaces

import com.burakekmen.rickandmortyguide.model.youtubemodels.PlaylistResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiInterface {

    @GET("playlists?part=snippet")
    fun tumListeyiGetir(@Query("channelId") channelId:String, @Query("key") apiKey:String): Call<PlaylistResponseModel>

}