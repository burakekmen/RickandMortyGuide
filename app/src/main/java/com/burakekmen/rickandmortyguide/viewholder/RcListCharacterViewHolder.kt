package com.burakekmen.rickandmortyguide.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.burakekmen.rickandmortyguide.R

class RcListCharacterViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    var characterName = itemView.findViewById<TextView>(R.id.character_list_item_name)!!
    var characterImage = itemView.findViewById<ImageView>(R.id.character_list_item_image)!!
    var characterStatus = itemView.findViewById<TextView>(R.id.character_item_status)!!
    var characterSpecies = itemView.findViewById<TextView>(R.id.character_item_species)!!
    var characterGender = itemView.findViewById<TextView>(R.id.character_item_gender)!!
    var characterOrigin = itemView.findViewById<TextView>(R.id.character_item_origin)!!
    var characterLastLocation = itemView.findViewById<TextView>(R.id.character_item_lastLocation)!!
    var cardView = itemView.findViewById<androidx.cardview.widget.CardView>(R.id.character_list_item_cardView)!!
}