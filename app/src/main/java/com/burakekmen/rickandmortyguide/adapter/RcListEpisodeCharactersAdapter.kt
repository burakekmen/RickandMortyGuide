package com.burakekmen.rickandmortyguide.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.ui.activity.CharacterActivity
import com.burakekmen.rickandmortyguide.viewholder.RcListEpisodeCharacterViewHolder
import com.squareup.picasso.Picasso

class RcListEpisodeCharactersAdapter(context: Context?, characterResponse: MutableList<CharacterModel>?) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RcListEpisodeCharacterViewHolder>() {

    private var context = context
    var response = characterResponse

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcListEpisodeCharacterViewHolder {
        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.episode_character_list_item, parent, false)

        return RcListEpisodeCharacterViewHolder(inflater)
    }


    override fun onBindViewHolder(holder: RcListEpisodeCharacterViewHolder, position: Int) {

        var character = getItem(position)

        Picasso.get().load(character!!.image).into(holder.characterImage)
        holder.characterName.text = character.name

        holder.characterCardView.setOnClickListener {
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }


    private fun detaySayfasinaGit(character: CharacterModel?) {

        var intent = Intent(context, CharacterActivity::class.java)
        intent.putExtra("selectedCharacter", character)
        context?.startActivity(intent)
    }


    override fun getItemCount(): Int {
        return response!!.size
    }

    private fun getItem(position: Int): CharacterModel? {
        return response!![position]
    }

}