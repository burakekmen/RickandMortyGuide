package com.burakekmen.rickandmortyguide.model.youtubemodels

import android.os.Parcel
import android.os.Parcelable
import com.burakekmen.rickandmortyguide.model.youtubemodels.thumbnailsmodels.*

data class Thumbnail(var default: ThumbnailDefault, var medium: ThumbnailMedium, var high: ThumbnailHigh, var standart: ThumbnailStandart, var maxres: ThumbnailMaxRes):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ThumbnailDefault::class.java.classLoader),
        parcel.readParcelable(ThumbnailMedium::class.java.classLoader),
        parcel.readParcelable(ThumbnailHigh::class.java.classLoader),
        parcel.readParcelable(ThumbnailStandart::class.java.classLoader),
        parcel.readParcelable(ThumbnailMaxRes::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(default, flags)
        parcel.writeParcelable(medium, flags)
        parcel.writeParcelable(high, flags)
        parcel.writeParcelable(standart, flags)
        parcel.writeParcelable(maxres, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thumbnail> {
        override fun createFromParcel(parcel: Parcel): Thumbnail {
            return Thumbnail(parcel)
        }

        override fun newArray(size: Int): Array<Thumbnail?> {
            return arrayOfNulls(size)
        }
    }
}