package com.burakekmen.rickandmortyguide.model

import android.os.Parcel
import android.os.Parcelable

data class EpisodeModel(var id: Int, var name: String, var air_date: String, var episode: String, var characters:ArrayList<String>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(String::class.java.classLoader) as ArrayList<String>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(air_date)
        parcel.writeString(episode)
        parcel.writeList(characters)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EpisodeModel> {
        override fun createFromParcel(parcel: Parcel): EpisodeModel {
            return EpisodeModel(parcel)
        }

        override fun newArray(size: Int): Array<EpisodeModel?> {
            return arrayOfNulls(size)
        }
    }
}
