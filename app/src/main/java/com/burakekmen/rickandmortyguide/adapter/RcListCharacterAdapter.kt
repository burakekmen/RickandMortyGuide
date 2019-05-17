package com.burakekmen.rickandmortyguide.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.model.CharacterResponse
import com.burakekmen.rickandmortyguide.ui.activity.CharacterActivity
import com.burakekmen.rickandmortyguide.viewholder.RcListCharacterViewHolder
import com.squareup.picasso.Picasso

class RcListCharacterAdapter(private var context: Context?, private val activity: Activity?, characterResponse: CharacterResponse?) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RcListCharacterViewHolder>() {

    var response = characterResponse

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcListCharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.character_list_item, parent, false)

        return RcListCharacterViewHolder(inflater)
    }


    override fun onBindViewHolder(holder: RcListCharacterViewHolder, position: Int) {

        val character = getItem(position)!!

        Picasso.get().load(character.image).into(holder.characterImage)
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
        val intent = Intent(activity!!, CharacterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("selectedCharacter", character)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    override fun getItemCount(): Int {
        return response!!.results.size
    }

    private fun getItem(position: Int): CharacterModel? {
        return response!!.results[position]
    }

}