package com.burakekmen.rickandmortyguide.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.adapter.RcListCharacterEpisodesAdapter
import com.burakekmen.rickandmortyguide.database.DatabaseHelper
import com.burakekmen.rickandmortyguide.enums.SharedPreferenceNameEnum
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.model.EpisodeModel
import com.burakekmen.rickandmortyguide.network.ApiClient
import com.burakekmen.rickandmortyguide.network.ApiInterface
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterActivity : AppCompatActivity(), View.OnClickListener {

    private var sharedPref: SharedPreferences? = null
    private lateinit var mInterstitialAd: InterstitialAd
    private var apiInterface: ApiInterface? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var utils: Utils? = null
    private var character: CharacterModel? = null
    private var dbHandler: DatabaseHelper? = null
    private var isFavourite = false
    private var episodeIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        acilisAyarlariniYap()
        getExtras()

        if (character != null) {
            verileriDoldur()
        }
    }

    init {
        utils = Utils(this)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        dbHandler = DatabaseHelper(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    fun acilisAyarlariniYap() {
        utils!!.hideStatusBar()
        activity_character_favButton?.setOnClickListener(this)

        sharedPref =
            getSharedPreferences(SharedPreferenceNameEnum.RaMSharedPereference.toString(), Context.MODE_PRIVATE)
        val editor = sharedPref!!.edit()
        var viewCount = sharedPref!!.getInt("characterViewCount", 0) + 1
        var totalViewCount = sharedPref!!.getInt("totalCharacterView", 0) + 1
        editor!!.putInt("characterViewCount", viewCount).apply()
        editor.putInt("totalCharacterView", totalViewCount).apply()


        MobileAds.initialize(this, "ca-app-pub-1757058856747719~8412082886")
        mInterstitialAd = InterstitialAd(this)

        if (getString(R.string.isRelease).toBoolean())
            mInterstitialAd.adUnitId = getString(R.string.adId_Release)
        else
            mInterstitialAd.adUnitId = getString(R.string.adId_Test)
        adListenerTanimla()
    }


    fun getExtras() {

        var bundle = intent.extras

        if (bundle != null) {
            character = bundle.getParcelable("selectedCharacter")
            eventKaydet()

            isFavourite = dbHandler!!.isHaveFavouriteCharacter(character!!.id.toString())

            if (isFavourite)
                activity_character_favButton?.setImageResource(R.drawable.ic_like)
            else
                activity_character_favButton?.setImageResource(R.drawable.ic_like_inactive)
        }
    }


    fun verileriDoldur() {
        Picasso.get().load(character!!.image).into(activity_character_image)
        activity_character_txtName?.text = character!!.name
        activity_character_status?.text = character!!.status
        activity_character_gender?.text = character!!.gender
        activity_character_lastLocation?.text = character!!.location.name
        activity_character_origin?.text = character!!.origin.name
        activity_character_species?.text = character!!.species

        if (character!!.status.toLowerCase() == "alive") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity_character_status.setTextColor(getColor(R.color.alive_color))
            } else {
                activity_character_status.setTextColor(resources!!.getColor(R.color.alive_color))
            }
        } else if (character!!.status.toLowerCase() == "dead") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity_character_status.setTextColor(getColor(R.color.dead_color))
            } else {
                activity_character_status.setTextColor(resources!!.getColor(R.color.dead_color))
            }
        } else if (character!!.status.toLowerCase() == "unknown") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity_character_status.setTextColor(getColor(R.color.unknown_color))
            } else {
                activity_character_status.setTextColor(resources!!.getColor(R.color.unknown_color))
            }
        }


        if (utils!!.isOnline()) {
            if (character!!.episode!!.size > 1) {
                getEpisodeIdsFromList()
                getCharacterEpisodes(true)
                activity_character_episodeList.visibility = View.VISIBLE
            } else {
                if (character!!.episode!!.size == 1) {
                    getEpisodeIdsFromList()
                    getCharacterEpisodes(false)
                    activity_character_episodeList.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun getEpisodeIdsFromList() {
        for (i in 0 until character!!.episode!!.size) {
            var episode = character!!.episode!![i]
            var index = episode.lastIndexOf('/')

            episodeIds.add(episode.substring(index + 1))
        }
    }


    private fun getCharacterEpisodes(isMulti: Boolean) {
        utils!!.waitDialogShow()

        if (isMulti) {
            apiInterface?.getCharacterMultiEpisode(episodeIds.joinToString())
                ?.enqueue(object : Callback<Array<EpisodeModel>> {

                    override fun onResponse(
                        call: Call<Array<EpisodeModel>>?,
                        response: Response<Array<EpisodeModel>>?
                    ) {

                        if (response!!.isSuccessful) {

                            val response = response.body()


                            val characterEpisodeListAdapter =
                                RcListCharacterEpisodesAdapter(response!!.toMutableList(), applicationContext)
                            listeyeGonder(characterEpisodeListAdapter)

                            utils?.waitDialogHide()
                        } else {
                            utils?.waitDialogHide()
                        }
                    }

                    override fun onFailure(call: Call<Array<EpisodeModel>>?, t: Throwable?) {

                        Log.e("RICK HATA", t!!.message)
                        utils?.waitDialogHide()
                    }

                })
        } else {
            apiInterface?.getCharacterEpisode(episodeIds.joinToString())
                ?.enqueue(object : Callback<EpisodeModel> {

                    override fun onResponse(
                        call: Call<EpisodeModel>?,
                        response: Response<EpisodeModel>?
                    ) {

                        if (response!!.isSuccessful) {

                            val characterEpisode = mutableListOf<EpisodeModel>()
                            characterEpisode.add(response.body()!!)

                            val characterEpisodesAdapter =
                                RcListCharacterEpisodesAdapter(characterEpisode, applicationContext)
                            listeyeGonder(characterEpisodesAdapter)

                            utils?.waitDialogHide()
                        } else {
                            utils?.waitDialogHide()
                        }
                    }

                    override fun onFailure(call: Call<EpisodeModel>?, t: Throwable?) {

                        Log.e("RICK HATA", t!!.message)
                        utils?.waitDialogHide()
                    }

                })
        }
    }


    @SuppressLint("WrongConstant")
    fun listeyeGonder(adapter: RcListCharacterEpisodesAdapter) {
        activity_character_episodeList.adapter = adapter
        var myLayoutManager = LinearLayoutManager(this@CharacterActivity, LinearLayoutManager.VERTICAL, false)
        activity_character_episodeList.layoutManager = myLayoutManager
        activity_character_episodeList.setHasFixedSize(true)

        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_character_favButton -> {
                if (!dbHandler!!.isHaveFavouriteCharacter(character!!.id.toString())) {
                    addFavourite(character!!.id)
                } else {
                    removeFavourite(character!!.id)
                }

                if (dbHandler!!.isHaveFavouriteCharacter(character!!.id.toString()))
                    activity_character_favButton?.setImageResource(R.drawable.ic_like)
                else
                    activity_character_favButton?.setImageResource(R.drawable.ic_like_inactive)
            }
        }
    }


    @SuppressLint("ShowToast")
    private fun addFavourite(characterId: Int) {
        var sonuc = dbHandler!!.addFavourite(characterId.toString())

        if (sonuc)
            utils!!.actionFavouriteSuccessDialogShow("This character is your favourite character now")
    }

    private fun removeFavourite(characterId: Int) {
        var sonuc = dbHandler!!.removeFavourite(characterId.toString())

        if (sonuc)
            utils!!.actionFavouriteSuccessDialogShow("This character is NOT your favourite character now")
    }


    fun eventKaydet() {
        mFirebaseAnalytics!!.logEvent("sc_CharacterDetail", null)
        mFirebaseAnalytics!!.logEvent(character!!.id.toString() + " - " + character!!.name, null)
    }


    fun adListenerTanimla() {
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                val editor = sharedPref!!.edit()
                var viewCount = sharedPref!!.getInt("characterViewCount", 0)

                if (viewCount >= 4) {
                    if (mInterstitialAd.isLoaded) {
                        editor.putInt("characterViewCount", 0).apply()
                        mInterstitialAd.show()
                    } else {
                        mInterstitialAd.loadAd(AdRequest.Builder().build())
                    }
                }
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
            }
        }
    }


    override fun onResume() {
        if (!utils!!.isOnline())
            utils!!.internetConnectionWarningShow()
        super.onResume()

    }


}


