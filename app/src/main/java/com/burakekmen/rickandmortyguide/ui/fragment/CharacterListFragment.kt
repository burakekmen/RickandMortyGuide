package com.burakekmen.rickandmortyguide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.adapter.RcListCharacterAdapter
import com.burakekmen.rickandmortyguide.model.CharacterResponse
import com.burakekmen.rickandmortyguide.network.ApiClient
import com.burakekmen.rickandmortyguide.network.ApiInterface
import kotlinx.android.synthetic.main.fragment_character_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CharacterListFragment : Fragment() {

    private var apiInterface: ApiInterface?=null
    private var utils: Utils? = null
    private var flayout: View? = null
    private var pageCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (flayout == null)
            flayout = inflater.inflate(R.layout.fragment_character_list, container, false)



        acilisHazirlik()

        if (utils!!.isOnline()) {
            getCharacters()
        } else
            utils!!.internetConnectionWarningShow()


        return flayout
    }


    fun acilisHazirlik() {
        utils = Utils(context!!)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)
    }


    fun getCharacters() {
        utils!!.waitDialogShow()

        apiInterface?.getCharacterPage(pageCount)?.enqueue(object : Callback<CharacterResponse> {

            override fun onResponse(call: Call<CharacterResponse>?, response: Response<CharacterResponse>?) {

                if (response!!.isSuccessful) {

                    var characterResponse = CharacterResponse(response.body()!!.info, response.body()!!.results)

                    var characterListAdapter = RcListCharacterAdapter(context, characterResponse)
                    listeyeGonder(characterListAdapter)

                    utils?.waitDialogHide()
                } else {
                    utils?.waitDialogHide()
                }
            }

            override fun onFailure(call: Call<CharacterResponse>?, t: Throwable?) {

                Log.e("RICK HATA", t!!.message)
                utils?.waitDialogHide()
            }

        })
    }


    fun listeyeGonder(adapter: RcListCharacterAdapter) {
        fragment_character_rcList.adapter = adapter
        var fragmentActivity = activity!!
        var myLayoutManager = LinearLayoutManager(fragmentActivity, LinearLayoutManager.VERTICAL, false)
        fragment_character_rcList.layoutManager = myLayoutManager
    }


}
