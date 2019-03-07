package com.burakekmen.rickandmortyguide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.network.ApiClient
import com.burakekmen.rickandmortyguide.network.ApiInterface


class FavouriteFragment : Fragment() {

    private var apiInterface: ApiInterface? = null
    private var utils: Utils? = null
    private var flayout: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (flayout == null)
            flayout = inflater.inflate(R.layout.fragment_favourite, container, false)

        acilisHazirlikYap()

        return flayout
    }


    private fun acilisHazirlikYap() {
        utils = Utils(context!!)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)
    }


    private fun favorileriGetir() {

    }

}
