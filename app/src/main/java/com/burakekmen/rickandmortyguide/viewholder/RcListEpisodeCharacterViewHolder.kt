package com.burakekmen.rickandmortyguide.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.burakekmen.rickandmortyguide.R

class RcListEpisodeCharacterViewHolder (itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    var characterName = itemView.findViewById<TextView>(R.id.activity_episode_character_item_name)!!
    var characterImage = itemView.findViewById<ImageView>(R.id.activity_episode_character_item_image)!!
    var characterCardView = itemView.findViewById<CardView>(R.id.activity_episode_character_item_cardView)!!
}