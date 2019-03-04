package com.burakekmen.rickandmortyguide.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.network.ApiInterface


class CharacterListFragment : Fragment() {

    private var apiInterface: ApiInterface?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }


}
