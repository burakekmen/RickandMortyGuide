package com.burakekmen.rickandmortyguide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.model.EpisodeModel

class RcListCharacterEpisodesAdapter(val episodeResponse: MutableList<EpisodeModel>) :
    RecyclerView.Adapter<RcListCharacterEpisodesAdapter.CharacterEpisdesVH>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterEpisdesVH {

        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.character_episode_list_item, parent, false)

        return CharacterEpisdesVH(inflater)
    }

    override fun getItemCount() = episodeResponse.size

    override fun onBindViewHolder(holder: CharacterEpisdesVH, position: Int) {
        val episode = episodeResponse[position]
        holder.episodeName.text = episode.name
        holder.episodeSeason.text = episode.episode
        holder.episodeAirDate.text = episode.air_date
    }

    inner class CharacterEpisdesVH(view: View) : RecyclerView.ViewHolder(view) {
        val episodeName = view.findViewById<TextView>(R.id.character_episode_list_item_episodeName)
        val episodeSeason = view.findViewById<TextView>(R.id.character_episode_list_item_episodeSeason)
        val episodeAirDate = view.findViewById<TextView>(R.id.character_episode_list_item_episodeAirDate)
    }
}