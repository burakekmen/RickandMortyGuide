package com.burakekmen.rickandmortyguide.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.adapter.RcListEpisodeCharactersAdapter
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.model.EpisodeModel
import com.burakekmen.rickandmortyguide.network.api.clients.ApiClient
import com.burakekmen.rickandmortyguide.network.api.interfaces.ApiInterface
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_episode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EpisodeActivity : AppCompatActivity() {

    private var apiInterface: ApiInterface? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var utils: Utils? = null
    private var characterIds = mutableListOf<String>()
    private var selectedEpisode: EpisodeModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)

        acilisHazirlikYap()
        getExtras()

        if (selectedEpisode != null)
            verileriDoldur()
        else
            utils!!.actionErrorDialogShow(getString(R.string.actionErrorDialogMessage))
    }


    private fun acilisHazirlikYap() {
        utils = Utils(this)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        utils!!.hideStatusBar()
    }

    private fun getExtras() {
        val bundle = intent.extras

        if (bundle != null) {
            selectedEpisode = bundle.getParcelable("selectedEpisode")
            eventKaydet()
        }
    }


    private fun verileriDoldur() {
        activity_episode_txtEpisodeName.text = selectedEpisode!!.name
        activity_episode_txtEpisodeSeason.text = selectedEpisode!!.episode

        getCharacterIdsFromList()

        if (utils!!.isOnline()) {
            if (characterIds.size > 1) {
                getEpisodeCharacters(true)
            } else {
                getEpisodeCharacters(false)
            }

            setAdBanner()
        } else {
            utils!!.internetConnectionWarningShow()
        }
    }


    private fun getCharacterIdsFromList() {
        for (i in 0 until selectedEpisode!!.characters.size) {
            val character = selectedEpisode!!.characters[i]
            val index = character.lastIndexOf('/')

            characterIds.add(character.substring(index + 1))
        }
    }


    private fun getEpisodeCharacters(isMulti: Boolean) {
        utils!!.waitDialogShow()

        if (isMulti) {
            apiInterface?.getMultiCharacter(characterIds.joinToString())
                ?.enqueue(object : Callback<Array<CharacterModel>> {

                    override fun onResponse(
                        call: Call<Array<CharacterModel>>?,
                        response: Response<Array<CharacterModel>>?
                    ) {

                        if (response!!.isSuccessful) {

                            val response = response.body()


                            val episodeCharactersListAdapter =
                                RcListEpisodeCharactersAdapter(applicationContext!!, this@EpisodeActivity, response!!.toMutableList())
                            listeyeGonder(episodeCharactersListAdapter)

                            utils?.waitDialogHide()
                        } else {
                            utils?.waitDialogHide()
                        }
                    }

                    override fun onFailure(call: Call<Array<CharacterModel>>?, t: Throwable?) {

                        Log.e("RICK HATA", t!!.message)
                        utils?.waitDialogHide()
                    }

                })
        } else {
            apiInterface?.getSingleCharacter(characterIds.joinToString())
                ?.enqueue(object : Callback<CharacterModel> {

                    override fun onResponse(
                        call: Call<CharacterModel>?,
                        response: Response<CharacterModel>?
                    ) {

                        if (response!!.isSuccessful) {

                            val episodeCharacter = mutableListOf<CharacterModel>()
                            episodeCharacter.add(response.body()!!)

                            val episodeCharacterAdapter =
                                RcListEpisodeCharactersAdapter(applicationContext!!, this@EpisodeActivity, episodeCharacter)
                            listeyeGonder(episodeCharacterAdapter)

                            utils?.waitDialogHide()
                        } else {
                            utils?.waitDialogHide()
                        }
                    }

                    override fun onFailure(call: Call<CharacterModel>?, t: Throwable?) {

                        Log.e("RICK HATA", t!!.message)
                        utils?.waitDialogHide()
                    }

                })
        }
    }


    @SuppressLint("WrongConstant")
    fun listeyeGonder(adapter: RcListEpisodeCharactersAdapter) {
        activity_episode_characterRcList.adapter = adapter
        val mGridLayoutManager = GridLayoutManager(this@EpisodeActivity, 2)
        activity_episode_characterRcList.layoutManager = mGridLayoutManager
        activity_episode_characterRcList.setHasFixedSize(false)
    }


    private fun setAdBanner() {

        MobileAds.initialize(this, "ca-app-pub-1757058856747719~8412082886")

        if (getString(R.string.isRelease).toBoolean()) {
            //activity_base_adBanner!!.adUnitId = getString(R.string.adBaneerId_Release)
            val adRequest = AdRequest.Builder().build()
            activity_episode_adBanner!!.loadAd(adRequest)
        } else {
            //activity_base_adBanner!!.adUnitId = getString(R.string.adBaneerId_Test)
            val adRequest = AdRequest.Builder().build()
            activity_episode_adBanner!!.loadAd(adRequest)
        }


        activity_episode_adBanner!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                //
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }


    private fun eventKaydet() {
        mFirebaseAnalytics!!.logEvent("sc_EpisodeDetail_$selectedEpisode!!.id.toString()", null)
    }


    override fun onResume() {
        super.onResume()

        if (!utils!!.isOnline())
            utils!!.internetConnectionWarningShow()
    }



    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}
