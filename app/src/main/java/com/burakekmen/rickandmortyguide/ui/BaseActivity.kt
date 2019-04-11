package com.burakekmen.rickandmortyguide.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.enums.SharedPreferenceNameEnum
import com.burakekmen.rickandmortyguide.ui.fragment.CharacterListFragment
import com.burakekmen.rickandmortyguide.ui.fragment.EpisodeFragment
import com.burakekmen.rickandmortyguide.ui.fragment.FavouriteFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_base.*


class BaseActivity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener {

    lateinit var mAdView: AdView
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    var fragmentManager: FragmentManager? = null
    var fragmentTransaction: FragmentTransaction? = null
    private var doubleBackToExitPressedOnce = false
    private var utils: Utils? = null
    private var characterFragment: CharacterListFragment? = null
    private var favouritesFragment: FavouriteFragment? = null
    private var episodeFragment: EpisodeFragment? = null
    private var page = 0 // searchview içerisinde yazıyı göstermek ve apiye doğru isteği atabilmek için kullanıldı
    private var searchQuery = ""
    private var firstOpen = true
    private var sortStatus = ""
    private var userToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        acilisAyarlariniYap()
    }


    init {
        utils = Utils(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }


    @SuppressLint("ResourceAsColor")
    fun acilisAyarlariniYap() {
        utils!!.hideStatusBar()
        spinnerTanimla()

        if (!utils!!.isOnline()) {
            bottomBarTanimla()
        } else {
            activity_base_searchView?.setOnClickListener(this)
            activity_base_searchView?.setOnQueryTextListener(this)
            activity_base_searchView?.onActionViewExpanded()
            activity_base_searchView?.isFocusable = false
            activity_base_searchView?.queryHint = "Bla bla bla .."

            bottomBarTanimla()

            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager!!.beginTransaction()
            characterFragment = CharacterListFragment()
            fragmentTransaction!!.add(R.id.activity_base_fragment, characterFragment!!).commit()

            page = 1

            mFirebaseAnalytics!!.logEvent("sc_Characters", null)
            getToken()

            setAdBanner()
        }
    }


    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                userToken = task.result!!.token
                Log.i("Firebase_Token", userToken)
            })


        if (getString(R.string.isRelease).toBoolean())
            FirebaseMessaging.getInstance().isAutoInitEnabled = true

    }

    fun bottomBarTanimla() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity_base_bottom_navigation_bar
                .addItem(
                    BottomNavigationItem(
                        R.drawable.tab1,
                        getString(R.string.tab_1)
                    ).setActiveColor(getColor(R.color.tab1Color))
                )
                .addItem(
                    BottomNavigationItem(
                        R.drawable.tab2,
                        getString(R.string.tab_2)
                    ).setActiveColor(getColor(R.color.tab2Color))
                )
                .addItem(
                    BottomNavigationItem(
                        R.drawable.tab3,
                        getString(R.string.tab_3)
                    ).setActiveColor(getColor(R.color.tab3Color))
                )
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise()
        } else {
            activity_base_bottom_navigation_bar
                .addItem(BottomNavigationItem(R.drawable.tab1, getString(R.string.tab_1)).setActiveColor("#420259"))
                .addItem(BottomNavigationItem(R.drawable.tab2, getString(R.string.tab_2)).setActiveColor("#90542F"))
                .addItem(BottomNavigationItem(R.drawable.tab3, getString(R.string.tab_3)).setActiveColor("#F26D2E"))
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise()
        }


        activity_base_bottom_navigation_bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {

                var transaction = fragmentManager!!.beginTransaction()
                if (position == 0) {
                    if (characterFragment == null)
                        characterFragment = CharacterListFragment()

                    transaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()

                    page = 1
                    activity_base_searchView?.visibility = View.VISIBLE
                    activity_base_spinnerSort.visibility = View.VISIBLE

                    mFirebaseAnalytics!!.logEvent("sc_Characters", null)
                    rateAppShow()
                    utils!!.privacyPolicyShow()

                } else if (position == 1) {
                    if (favouritesFragment == null)
                        favouritesFragment = FavouriteFragment()

                    transaction.replace(R.id.activity_base_fragment, favouritesFragment!!).commit()

                    page = 2
                    activity_base_searchView?.visibility = View.GONE
                    activity_base_spinnerSort.visibility = View.GONE

                    mFirebaseAnalytics!!.logEvent("sc_Favourites", null)
                    rateAppShow()
                    utils!!.privacyPolicyShow()

                } else if (position == 2) {
                    if (episodeFragment == null)
                        episodeFragment = EpisodeFragment()

                    transaction.replace(R.id.activity_base_fragment, episodeFragment!!).commit()

                    page = 3
                    activity_base_searchView?.visibility = View.GONE
                    activity_base_spinnerSort.visibility = View.GONE

                    mFirebaseAnalytics!!.logEvent("sc_Episodes", null)
                    rateAppShow()
                    utils!!.privacyPolicyShow()
                }

                fragmentManager!!.popBackStack()
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
    }


    fun spinnerTanimla() {
        ArrayAdapter.createFromResource(
            this,
            R.array.sort_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            activity_base_spinnerSort!!.adapter = adapter
        }


        activity_base_spinnerSort?.onItemSelectedListener = this
    }


    fun fragmentOpen() {
        when (page) {
            1 -> {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                characterFragment = CharacterListFragment()

                val bundle = Bundle()
                bundle.putString("searchQuery", searchQuery)
                bundle.putString("sortStatus", sortStatus)
                characterFragment!!.arguments = bundle

                fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                fragmentManager.popBackStack()

                page = 1
            }
            2 -> {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                favouritesFragment = FavouriteFragment()

                fragmentTransaction.replace(R.id.activity_base_fragment, favouritesFragment!!).commit()
                fragmentManager.popBackStack()

                page = 2
            }
            3 -> {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                episodeFragment = EpisodeFragment()

                fragmentTransaction.replace(R.id.activity_base_fragment, episodeFragment!!).commit()
                fragmentManager.popBackStack()

                page = 2
            }
        }
    }


    private fun setAdBanner() {

        MobileAds.initialize(this, "ca-app-pub-1757058856747719~8412082886")

        if (getString(R.string.isRelease).toBoolean()) {
            //activity_base_adBanner!!.adUnitId = getString(R.string.adBaneerId_Release)
            val adRequest = AdRequest.Builder().build()
            activity_base_adBanner!!.loadAd(adRequest)
        } else {
            //activity_base_adBanner!!.adUnitId = getString(R.string.adBaneerId_Test)
            val adRequest = AdRequest.Builder().build()
            activity_base_adBanner!!.loadAd(adRequest)
        }


        activity_base_adBanner!!.adListener = object : AdListener() {
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

    override fun onQueryTextSubmit(query: String?): Boolean {

        searchQuery = query!!
        fragmentOpen()

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        searchQuery = query!!

        return false
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        var sortItem = parent!!.selectedItemPosition
        var transaction = fragmentManager?.beginTransaction()

        if (!firstOpen) {
            when (sortItem) {
                0 -> {

                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)

                    sortStatus = ""

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    transaction!!.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager!!.popBackStack()

                    page = 1
                }
                1 -> {
                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)

                    sortStatus = getString(R.string.alive)

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    transaction!!.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager!!.popBackStack()

                    page = 1
                }
                2 -> {
                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)

                    sortStatus = getString(R.string.dead)

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    transaction!!.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager!!.popBackStack()

                    page = 1
                }
                3 -> {
                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)
                    sortStatus = getString(R.string.unknown)

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    transaction!!.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager!!.popBackStack()

                    page = 1
                }
            }
        } else
            firstOpen = false


    }


    private fun rateAppShow() {
        val sharedPref =
            getSharedPreferences(SharedPreferenceNameEnum.RaMSharedPereference.toString(), Context.MODE_PRIVATE)
        val editor = sharedPref!!.edit()
        val isRateApp = sharedPref.getBoolean("isRateApp", false)
        val totalViewCount = sharedPref.getInt("totalCharacterView", 0)


        if (totalViewCount >= 10) {
            if (!isRateApp) {
                utils!!.rateUsDialogShow()
            }
        }
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true

            Snackbar.make(
                activity_base_layout, // Parent view
                "Please click Back button again for exit!", // Message to show
                Snackbar.LENGTH_SHORT // How long to display the message.
            ).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
            return
        }
    }


    override fun onResume() {
        if (!utils!!.isOnline())
            utils!!.internetConnectionWarningShow()

        super.onResume()
    }


}
