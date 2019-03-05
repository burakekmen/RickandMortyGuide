package com.burakekmen.rickandmortyguide.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.ui.fragment.CharacterListFragment
import kotlinx.android.synthetic.main.activity_base.*


class BaseActivity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener {

    private var doubleBackToExitPressedOnce = false
    private var utils: Utils? = null
    private var characterFragment: CharacterListFragment? = null
    private var page = 0 // searchview içerisinde yazıyı göstermek ve apiye doğru isteği atabilmek için kullanıldı
    private var searchQuery = ""
    private var firstOpen = true
    private var sortStatus = ""

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
        spinnerTanimla()

        if (!utils!!.isOnline())
            utils!!.internetConnectionWarningShow()

        activity_base_searchView?.setOnClickListener(this)
        activity_base_searchView?.setOnQueryTextListener(this)
        activity_base_searchView?.onActionViewExpanded()
        activity_base_searchView?.isFocusable = false
        activity_base_searchView?.queryHint = "Bla bla bla .."

        bottomBarTanimla()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        characterFragment = CharacterListFragment()
        fragmentTransaction.add(R.id.activity_base_fragment, characterFragment!!)
        fragmentTransaction.commit()

        page = 1
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
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise()
        } else {
            activity_base_bottom_navigation_bar
                .addItem(BottomNavigationItem(R.drawable.tab1, getString(R.string.tab_1)).setActiveColor("#420259"))
                .addItem(BottomNavigationItem(R.drawable.tab2, getString(R.string.tab_2)).setActiveColor("#90542F"))
                .setFirstSelectedPosition(0)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise()
        }


        activity_base_bottom_navigation_bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {

                if (position == 0) {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    if (characterFragment == null)
                        characterFragment = CharacterListFragment()

                    if (searchQuery == "")
                        characterFragment = CharacterListFragment()

                    fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager.popBackStack()

                    page = 1
                } else if (position == 1) {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    if (characterFragment == null)
                        characterFragment = CharacterListFragment()

                    fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager.popBackStack()

                    page = 2
                }
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

                if (characterFragment == null)
                    characterFragment = CharacterListFragment()

                fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                fragmentManager.popBackStack()

                page = 2
            }
            3 -> {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                if (characterFragment == null)
                    characterFragment = CharacterListFragment()

                fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                fragmentManager.popBackStack()

                page = 3
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
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (!firstOpen) {
            when (sortItem) {
                0 -> {
                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)

                    sortStatus = ""

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager.popBackStack()

                    page = 1
                }
                1 -> {
                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)

                    sortStatus = getString(R.string.alive)

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager.popBackStack()

                    page = 1
                }
                2 -> {
                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)

                    sortStatus = getString(R.string.dead)

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager.popBackStack()

                    page = 1
                }
                3 -> {
                    characterFragment = CharacterListFragment()

                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)
                    sortStatus = getString(R.string.unknown)

                    bundle.putString("sortStatus", sortStatus)
                    characterFragment!!.arguments = bundle

                    fragmentTransaction.replace(R.id.activity_base_fragment, characterFragment!!).commit()
                    fragmentManager.popBackStack()

                    page = 1
                }
            }
        } else
            firstOpen = false


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
