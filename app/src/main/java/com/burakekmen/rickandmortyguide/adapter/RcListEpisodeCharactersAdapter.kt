package com.burakekmen.rickandmortyguide.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.model.CharacterModel
import com.burakekmen.rickandmortyguide.ui.activity.CharacterActivity
import com.burakekmen.rickandmortyguide.viewholder.RcListEpisodeCharacterViewHolder
import com.squareup.picasso.Picasso

class RcListEpisodeCharactersAdapter(private var context: Context?, private val activity: Activity?, characterResponse: MutableList<CharacterModel>?) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RcListEpisodeCharacterViewHolder>() {

    var response = characterResponse

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcListEpisodeCharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.episode_character_list_item, parent, false)

        return RcListEpisodeCharacterViewHolder(inflater)
    }


    override fun onBindViewHolder(holder: RcListEpisodeCharacterViewHolder, position: Int) {

        val character = getItem(position)!!

        Picasso.get().load(character.image).into(holder.characterImage)
        holder.characterName.text = character.name

        holder.characterCardView.setOnClickListener {
            detaySayfasinaGit(character)
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
        return response!!.size
    }

    private fun getItem(position: Int): CharacterModel? {
        return response!![position]
    }

}