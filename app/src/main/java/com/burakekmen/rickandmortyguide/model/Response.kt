package com.burakekmen.rickandmortyguide.model

import android.os.Parcel
import android.os.Parcelable

data class Response (var info:Info, var results:MutableList<Result>):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Info::class.java.classLoader),
        TODO("results")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(info, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Response> {
        override fun createFromParcel(parcel: Parcel): Response {
            return Response(parcel)
        }

        override fun newArray(size: Int): Array<Response?> {
            return arrayOfNulls(size)
        }
    }
}