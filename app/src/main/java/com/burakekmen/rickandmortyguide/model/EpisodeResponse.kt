package com.burakekmen.rickandmortyguide.model

import android.os.Parcel
import android.os.Parcelable

data class EpisodeResponse(var info: Info, var results: ArrayList<EpisodeModel>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Info::class.java.classLoader),
        parcel.readArrayList(EpisodeModel::class.java.classLoader) as ArrayList<EpisodeModel>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(info, flags)
        parcel.writeList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EpisodeResponse> {
        override fun createFromParcel(parcel: Parcel): EpisodeResponse {
            return EpisodeResponse(parcel)
        }

        override fun newArray(size: Int): Array<EpisodeResponse?> {
            return arrayOfNulls(size)
        }
    }
}