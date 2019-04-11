package com.burakekmen.rickandmortyguide.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.adapter.RcListFavouriteAdapter
import com.burakekmen.rickandmortyguide.database.DatabaseHelper
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.network.ApiClient
import com.burakekmen.rickandmortyguide.network.ApiInterface
import kotlinx.android.synthetic.main.fragment_favourite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavouriteFragment : Fragment(), View.OnClickListener {

    private var apiInterface: ApiInterface? = null
    private var utils: Utils? = null
    private var flayout: View? = null
    private var dbHandler: DatabaseHelper? = null
    private var favouriteList = mutableListOf<Int>()
    private var singleLineFavouriteIds = ""
    private var fragmentDurdurulduMu = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acilisHazirlikYap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (flayout == null)
            flayout = inflater.inflate(R.layout.fragment_favourite, container, false)

        return flayout
    }


    private fun acilisHazirlikYap() {
        utils = Utils(context!!)
        dbHandler = DatabaseHelper(context!!)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)

        fragment_favourite_sadFace?.setOnClickListener(this)
    }


    override fun onResume() {
        if (fragmentDurdurulduMu) {
            fragmentDurdurulduMu = false

            if (utils!!.isOnline())
                favorileriGetir()
            else
                utils!!.internetConnectionWarningShow()
        }
        super.onResume()
    }


    override fun onPause() {
        fragmentDurdurulduMu = true
        super.onPause()

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fragment_favourite_sadFace -> {
                utils!!.actionErrorDialogShow("I am so sad! \nBecause\n You have not favourite character!")
                //Toast.makeText(context, "I am so sad because You have not favourite character!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun favorileriGetir() {
        favouriteList = dbHandler!!.getAllFavourites()

        if (favouriteList.size >= 1) {
            setFavouriteListToOneLineString()

            if (favouriteList.size > 1)
                getFavourite(true)
            else
                getFavourite(false)

            fragment_favourite_sadFace.visibility = View.GONE
            fragment_favourite_rcList.visibility = View.VISIBLE

        } else {
            //utils!!.actionErrorDialogShow("You have not favourite character yet!")
            fragment_favourite_rcList.visibility = View.GONE
            fragment_favourite_sadFace.visibility = View.VISIBLE
        }

    }


    private fun getFavourite(isMulti: Boolean) {
        utils!!.waitDialogShow()

        if (isMulti) {
            apiInterface?.getFavouriteCharacters(singleLineFavouriteIds.trim())
                ?.enqueue(object : Callback<Array<CharacterModel>> {

                    override fun onResponse(
                        call: Call<Array<CharacterModel>>?,
                        response: Response<Array<CharacterModel>>?
                    ) {

                        if (response!!.isSuccessful) {

                            val response = response.body()


                            val characterFavouriteListAdapter =
                                RcListFavouriteAdapter(context, response!!.toMutableList())
                            listeyeGonder(characterFavouriteListAdapter)

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
            apiInterface?.getFavouriteCharacter(singleLineFavouriteIds.trim())
                ?.enqueue(object : Callback<CharacterModel> {

                    override fun onResponse(
                        call: Call<CharacterModel>?,
                        response: Response<CharacterModel>?
                    ) {

                        if (response!!.isSuccessful) {

                            val favouriteCharaacter = mutableListOf<CharacterModel>()
                            favouriteCharaacter.add(response.body()!!)


                            val characterFavouriteListAdapter =
                                RcListFavouriteAdapter(context, favouriteCharaacter)
                            listeyeGonder(characterFavouriteListAdapter)

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


    private fun setFavouriteListToOneLineString() {
        singleLineFavouriteIds = favouriteList.joinToString()
    }


    @SuppressLint("WrongConstant")
    fun listeyeGonder(adapter: RcListFavouriteAdapter) {
        fragment_favourite_rcList.adapter = adapter
        val fragmentActivity = activity!!
        val myLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            fragmentActivity,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        fragment_favourite_rcList.layoutManager = myLayoutManager
    }


}
