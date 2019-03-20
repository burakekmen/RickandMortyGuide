package com.burakekmen.rickandmortyguide.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.ui.activity.CharacterActivity
import com.burakekmen.rickandmortyguide.viewholder.RcListCharacterViewHolder
import com.squareup.picasso.Picasso

class RcListFavouriteAdapter(context: Context?, favourites: MutableList<CharacterModel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RcListCharacterViewHolder>() {

    private var context = context
    private var response = favourites

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcListCharacterViewHolder {
        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.character_list_item, parent, false)

        return RcListCharacterViewHolder(inflater)
    }


    override fun onBindViewHolder(holder: RcListCharacterViewHolder, position: Int) {

        var character = getItem(position)

        Picasso.get().load(character!!.image).into(holder.characterImage)
        holder.characterName.text = character.name
        holder.characterStatus.text = character.status
        holder.characterSpecies.text = character.species
        holder.characterGender.text = character.gender
        holder.characterOrigin.text = character.origin.name
        holder.characterLastLocation.text = character.location.name

        holder.cardView.setOnClickListener {
            detaySayfasinaGit(character)
        }


        if (character.status.toLowerCase() == "alive") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.characterStatus.setTextColor(context!!.getColor(R.color.alive_color))
            } else {
                holder.characterStatus.setTextColor(context!!.resources!!.getColor(R.color.alive_color))
            }
        } else if (character.status.toLowerCase() == "dead") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.characterStatus.setTextColor(context!!.getColor(R.color.dead_color))
            } else {
                holder.characterStatus.setTextColor(context!!.resources!!.getColor(R.color.dead_color))
            }
        } else if (character.status.toLowerCase() == "unknown") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.characterStatus.setTextColor(context!!.getColor(R.color.unknown_color))
            } else {
                holder.characterStatus.setTextColor(context!!.resources!!.getColor(R.color.unknown_color))
            }
        }

    }


    private fun detaySayfasinaGit(character: CharacterModel?) {

        var intent = Intent(context, CharacterActivity::class.java)
        intent.putExtra("selectedCharacter", character)
        context?.startActivity(intent)
    }


    override fun getItemCount(): Int {
        return response.size
    }

    private fun getItem(position: Int): CharacterModel? {
        return response[position]
    }

}