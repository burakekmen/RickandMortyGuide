package com.burakekmen.rickandmortyguide.ui.activity

import android.content.Context
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.burakekmen.rickandmortyguide.BuildConfig
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.enums.SharedPreferenceNameEnum
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var utils: Utils? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)


        acilisHazirlik()
    }


    private fun acilisHazirlik() {
        utils = Utils(this)

        if (utils!!.isOnline()) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
            webviewTanimla()
            preferenceGUpdate()
            eventKaydet()
        } else
            utils!!.internetConnectionWarningShow()
    }


    private fun webviewTanimla() {
        val webViewClient = WebViewClient()
        activity_policy_webview?.webViewClient = webViewClient
        activity_policy_webview?.loadUrl(BuildConfig.PRIVACY_POLICY_URL)
    }


    private fun preferenceGUpdate() {
        val sharedPref = getSharedPreferences(
            SharedPreferenceNameEnum.RaMSharedPereference.toString(),
            Context.MODE_PRIVATE
        )
        val editor = sharedPref!!.edit()
        editor.putBoolean("isConfirmPolicy", true)
        editor.apply()
    }


    fun eventKaydet() {
        mFirebaseAnalytics!!.logEvent("sc_PolicyActivity", null)
    }


}
