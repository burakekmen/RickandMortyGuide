package com.burakekmen.rickandmortyguide.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import kotlinx.android.synthetic.main.activity_splash.*



class SplashActivity : AppCompatActivity() {

    private var utils: Utils?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        acilisAyarlarınıYap()
    }



    init {
        utils = Utils(this)
    }


    fun acilisAyarlarınıYap(){
        utils!!.splashWaitSomeMunite()

        val pctrNumber = utils!!.getRandomNumber()
        val pictureResource = "sp$pctrNumber"
        val r = resources.getIdentifier(pictureResource, "drawable", packageName)
        activity_splash_image?.setImageResource(r)
    }



}
