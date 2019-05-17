package com.burakekmen.rickandmortyguide.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.model.EpisodeModel
import com.burakekmen.rickandmortyguide.ui.activity.EpisodeActivity

class RcListCharacterEpisodesAdapter(private val episodeResponse: MutableList<EpisodeModel>?, private var context: Context?, private val activity: Activity?) :
    RecyclerView.Adapter<RcListCharacterEpisodesAdapter.CharacterEpisdesVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterEpisdesVH {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.character_episode_list_item, parent, false)

        return CharacterEpisdesVH(inflater)
    }

    override fun getItemCount() = episodeResponse!!.size

    override fun onBindViewHolder(holder: CharacterEpisdesVH, position: Int) {
        val episode = episodeResponse!![position]
        holder.episodeName.text = episode.name
        holder.episodeSeason.text = episode.episode
        holder.episodeAirDate.text = episode.air_date


        holder.episodeCardView.setOnClickListener {
            val intent = Intent(activity!!, EpisodeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("selectedEpisode", episode)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    inner class CharacterEpisdesVH(view: View) : RecyclerView.ViewHolder(view) {
        val episodeName = view.findViewById<TextView>(R.id.character_episode_list_item_episodeName)!!
        val episodeSeason = view.findViewById<TextView>(R.id.character_episode_list_item_episodeSeason)!!
        val episodeAirDate = view.findViewById<TextView>(R.id.character_episode_list_item_episodeAirDate)!!
        val episodeCardView = view.findViewById<CardView>(R.id.character_episode_list_item_cardView)!!
    }
}

