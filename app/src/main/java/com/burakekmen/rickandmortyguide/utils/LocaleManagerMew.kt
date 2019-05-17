package com.burakekmen.rickandmortyguide.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleManagerMew {

    val SELECTED_LANGUAGE = "MEW_CURRENT_-- USER_LANGUAGE"
    var mSharedPreference: SharedPreferences? = null

    var mEnglishFlag = "en"
    var mTurkishFlag = "tr"
    var mGermanFlag = "de"
    var mPortugueseFlag = "pt"

    fun setLocale(context: Context?): Context {
        return updateResources(context!!, getCurrentLanguage(context)!!)
    }

    inline fun setNewLocale(context: Context, language: String) {

        persistLanguagePreference(context, language)
        updateResources(context, language)
    }

    inline fun getCurrentLanguage(context: Context?): String? {

        var mCurrentLanguage: String?

        if (mSharedPreference == null)
            mSharedPreference = PreferenceHelper.defaultPrefs(context!!)

        mCurrentLanguage = mSharedPreference!!.getString(SELECTED_LANGUAGE, "en")

        return mCurrentLanguage
    }

    fun persistLanguagePreference(context: Context, language: String) {
        if (mSharedPreference == null)
            mSharedPreference = PreferenceHelper.defaultPrefs(context)

        val editor = mSharedPreference!!.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()

    }

    fun updateResources(context: Context, language: String): Context {

        var contextFun = context

        var locale = Locale(language)
        Locale.setDefault(locale)

        var resources = context.resources
        var configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale)
            contextFun = context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return contextFun
    }

}