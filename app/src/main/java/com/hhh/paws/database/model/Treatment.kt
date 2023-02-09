package com.hhh.paws.database.model

import android.os.Parcel
import android.os.Parcelable

class Treatment() : Parcelable{
    var id: String? = null
    var name: String? = null
    var manufacturer: String? = null
    var date: String? = null
    var veterinarian: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        manufacturer = parcel.readString()
        date = parcel.readString()
        veterinarian = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(manufacturer)
        parcel.writeString(date)
        parcel.writeString(veterinarian)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Treatment> {
        override fun createFromParcel(parcel: Parcel): Treatment {
            return Treatment(parcel)
        }

        override fun newArray(size: Int): Array<Treatment?> {
            return arrayOfNulls(size)
        }
    }
}