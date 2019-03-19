package com.burakekmen.rickandmortyguide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.burakekmen.rickandmortyguide.R

class RcListCharacterEpisodesAdapter(val dataSet: MutableList<String>) :
    RecyclerView.Adapter<RcListCharacterEpisodesAdapter.CharacterEpisdesVH>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterEpisdesVH {

        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.character_episode_list_item, parent, false)

        return CharacterEpisdesVH(inflater)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: CharacterEpisdesVH, position: Int) {
        val episode = dataSet[position]
        holder.name.text = episode
    }

    inner class CharacterEpisdesVH(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.character_episode_list_item_episodeName)
    }
}