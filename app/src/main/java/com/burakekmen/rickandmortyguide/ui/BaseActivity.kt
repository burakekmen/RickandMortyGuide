package com.burakekmen.rickandmortyguide.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import kotlinx.android.synthetic.main.activity_base.*


class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private var utils: Utils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        acilisAyarlariniYap()
    }


    init {
        utils = Utils(this)
    }


    @SuppressLint("ResourceAsColor")
    fun acilisAyarlariniYap() {
        utils!!.hideStatusBar()
        if (!utils!!.isOnline())
            utils!!.internetConnectionWarningShow()

        bottomBarTanimla()
    }


    fun bottomBarTanimla() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity_base_bottom_navigation_bar
                .addItem(BottomNavigationItem(R.drawable.tab1, getString(R.string.tab_1)).setActiveColor(getColor(R.color.tab1Color)))
                .addItem(BottomNavigationItem(R.drawable.tab2, getString(R.string.tab_2)).setActiveColor(getColor(R.color.tab2Color)))
                .addItem(BottomNavigationItem(R.drawable.tab3, getString(R.string.tab_3)).setActiveColor(getColor(R.color.tab3Color)))
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise()
        }else{
            activity_base_bottom_navigation_bar
                .addItem(BottomNavigationItem(R.drawable.tab1, getString(R.string.tab_1)).setActiveColor("#420259"))
                .addItem(BottomNavigationItem(R.drawable.tab2, getString(R.string.tab_2)).setActiveColor("#90542F"))
                .addItem(BottomNavigationItem(R.drawable.tab3, getString(R.string.tab_3)).setActiveColor("#70b347"))
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise()
        }


        activity_base_bottom_navigation_bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {}
            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
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


}
