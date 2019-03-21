package com.burakekmen.rickandmortyguide.model

import android.os.Parcel
import android.os.Parcelable

data class EpisodeModel(var id: Int, var name: String, var air_date: String, var episode: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(air_date)
        parcel.writeString(episode)
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
