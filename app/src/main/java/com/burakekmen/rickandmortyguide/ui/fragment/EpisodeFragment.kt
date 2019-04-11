package com.burakekmen.rickandmortyguide.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.adapter.RcListCharacterEpisodesAdapter
import com.burakekmen.rickandmortyguide.model.EpisodeResponse
import com.burakekmen.rickandmortyguide.network.ApiClient
import com.burakekmen.rickandmortyguide.network.ApiInterface
import kotlinx.android.synthetic.main.fragment_episode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EpisodeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var apiInterface: ApiInterface? = null
    private var utils: Utils? = null
    private var fLayout: View? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acilisHazirlikYap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (fLayout == null)
            fLayout = inflater.inflate(R.layout.fragment_episode, container, false)

        return fLayout
    }


    private fun acilisHazirlikYap() {
        utils = Utils(context!!)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        spinnerTanimla()
    }


    private fun spinnerTanimla() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.episode_season_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fragment_episode_seasonSpinner!!.adapter = adapter
        }


        fragment_episode_seasonSpinner!!.onItemSelectedListener = this
    }


    private fun getEpisodes(seasonPosition: Int) {

        var season = if (seasonPosition < 10)
            "S0$seasonPosition"
        else
            "S$seasonPosition"

        apiInterface?.getCharacterEpisodeWithSeason(season)
            ?.enqueue(object : Callback<EpisodeResponse> {

                override fun onResponse(
                    call: Call<EpisodeResponse>?,
                    response: Response<EpisodeResponse>?
                ) {

                    if (response!!.isSuccessful) {

                        val episodes = response.body()!!
                        val episodesAdapter =
                            RcListCharacterEpisodesAdapter(episodes.results, context!!)
                        listeyeGonder(episodesAdapter)

                        utils?.waitDialogHide()
                    } else {
                        utils?.waitDialogHide()
                    }
                }

                override fun onFailure(call: Call<EpisodeResponse>?, t: Throwable?) {

                    Log.e("RICK HATA", t!!.message)
                    utils?.waitDialogHide()
                }

            })

    }


    @SuppressLint("WrongConstant")
    fun listeyeGonder(adapter: RcListCharacterEpisodesAdapter) {
        fragment_episode_rcList.adapter = adapter
        var myLayoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        fragment_episode_rcList.layoutManager = myLayoutManager
        fragment_episode_rcList.setHasFixedSize(true)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        getEpisodes(position + 1)

//        when(position){
//            0 ->{
//                getEpisodes(position+1)
//            }
//            1 ->{
//                getEpisodes(position+1)
//            }
//            2->{
//                getEpisodes(position+1)
//            }
//        }
    }


}
