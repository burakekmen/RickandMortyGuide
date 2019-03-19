package com.burakekmen.rickandmortyguide.model

import android.os.Parcel
import android.os.Parcelable

data class CharacterResponse(var info: Info, var results: ArrayList<CharacterModel>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Info::class.java.classLoader),
        parcel.readArrayList(CharacterModel::class.java.classLoader) as ArrayList<CharacterModel>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(info, flags)
        parcel.writeList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterResponse> {
        override fun createFromParcel(parcel: Parcel): CharacterResponse {
            return CharacterResponse(parcel)
        }

        override fun newArray(size: Int): Array<CharacterResponse?> {
            return arrayOfNulls(size)
        }
    }
}