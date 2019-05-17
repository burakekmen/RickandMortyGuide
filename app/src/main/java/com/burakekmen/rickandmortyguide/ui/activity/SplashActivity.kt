package com.burakekmen.rickandmortyguide.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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


    fun acilisAyarlarınıYap(){

        utils = Utils(this)

        utils!!.splashWaitSomeMunite()

        val pctrNumber = utils!!.getRandomNumber()
        val pictureResource = "sp$pctrNumber"
        val r = resources.getIdentifier(pictureResource, "drawable", packageName)
        activity_splash_image?.setImageResource(r)
    }


}
