package com.burakekmen.rickandmortyguide.model.youtubemodels.thumbnailsmodels

import android.os.Parcel
import android.os.Parcelable

data class ThumbnailStandart(var url:String, var width:Int, var height:Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeInt(width)
        parcel.writeInt(height)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ThumbnailDefault> {
        override fun createFromParcel(parcel: Parcel): ThumbnailDefault {
            return ThumbnailDefault(parcel)
        }

        override fun newArray(size: Int): Array<ThumbnailDefault?> {
            return arrayOfNulls(size)
        }
    }
}