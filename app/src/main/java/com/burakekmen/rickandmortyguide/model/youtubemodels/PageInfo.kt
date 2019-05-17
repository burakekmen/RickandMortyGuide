package com.burakekmen.rickandmortyguide.model.youtubemodels

import android.os.Parcel
import android.os.Parcelable

data class PageInfo(var totalResults:Int, var resultsPerPage:Int) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(totalResults)
        parcel.writeInt(resultsPerPage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PageInfo> {
        override fun createFromParcel(parcel: Parcel): PageInfo {
            return PageInfo(parcel)
        }

        override fun newArray(size: Int): Array<PageInfo?> {
            return arrayOfNulls(size)
        }
    }
}