package com.burakekmen.rickandmortyguide.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.adapter.RcListCharacterEpisodesAdapter
import com.burakekmen.rickandmortyguide.database.DatabaseHelper
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character.*

class CharacterActivity : AppCompatActivity(), View.OnClickListener {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var utils: Utils? = null
    private var character: CharacterModel? = null
    private var dbHandler: DatabaseHelper? = null
    private var isFavourite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        acilisAyarlariniYap()
        getExtras()

        if (character != null) {
            verileriDoldur()
            listeyeGonder()
        }
    }

    init {
        utils = Utils(this)
        dbHandler = DatabaseHelper(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    fun acilisAyarlariniYap() {
        utils!!.hideStatusBar()
        activity_character_favButton?.setOnClickListener(this)
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
    }


    @SuppressLint("WrongConstant")
    fun listeyeGonder() {
        activity_character_episodeList.adapter = RcListCharacterEpisodesAdapter(character!!.episode!!.toMutableList())
        var myLayoutManager = LinearLayoutManager(this@CharacterActivity, LinearLayoutManager.VERTICAL, false)
        activity_character_episodeList.layoutManager = myLayoutManager
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


    override fun onResume() {
        if (!utils!!.isOnline())
            utils!!.internetConnectionWarningShow()
        super.onResume()

    }

}


