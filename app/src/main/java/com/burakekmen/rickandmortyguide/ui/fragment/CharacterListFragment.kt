package com.burakekmen.rickandmortyguide.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
    private var searchQuery = ""
    private var status = ""

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

        val bundle = this.arguments
        if (bundle != null) run {
            searchQuery = bundle.getString("searchQuery")!!
            status = bundle.getString("sortStatus")!!
        }
    }


    fun getCharacters() {
        utils!!.waitDialogShow()


        if (searchQuery == "") {
            if (status == "") {
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
            } else {
                apiInterface?.getCharacterSearchWithStatus(pageCount, status)
                    ?.enqueue(object : Callback<CharacterResponse> {

                        override fun onResponse(
                            call: Call<CharacterResponse>?,
                            response: Response<CharacterResponse>?
                        ) {

                            if (response!!.isSuccessful) {

                                var characterResponse =
                                    CharacterResponse(response.body()!!.info, response.body()!!.results)

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
        } else {
            if (status == "") {


                apiInterface?.getCharacterSearch(pageCount, searchQuery)?.enqueue(object : Callback<CharacterResponse> {

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
            } else {
                apiInterface?.getCharacterSearchWithNameAndStatus(pageCount, searchQuery, status)
                    ?.enqueue(object : Callback<CharacterResponse> {

                        override fun onResponse(
                            call: Call<CharacterResponse>?,
                            response: Response<CharacterResponse>?
                        ) {

                            if (response!!.isSuccessful) {

                                var characterResponse =
                                    CharacterResponse(response.body()!!.info, response.body()!!.results)

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
        }
    }


    @SuppressLint("WrongConstant")
    fun listeyeGonder(adapter: RcListCharacterAdapter) {
        fragment_character_rcList.adapter = adapter
        var fragmentActivity = activity!!
        var myLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            fragmentActivity,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        fragment_character_rcList.layoutManager = myLayoutManager
    }


}
