package com.burakekmen.rickandmortyguide.model

import android.os.Parcel
import android.os.Parcelable

data class LocationResponse(var info: Info, var locationModel: ArrayList<LocationModel>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Info::class.java.classLoader),
        TODO("locationModel")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(info, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationResponse> {
        override fun createFromParcel(parcel: Parcel): LocationResponse {
            return LocationResponse(parcel)
        }

        override fun newArray(size: Int): Array<LocationResponse?> {
            return arrayOfNulls(size)
        }
    }
}
