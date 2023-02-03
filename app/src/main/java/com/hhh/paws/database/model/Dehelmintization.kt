package com.hhh.paws.database.model

import android.os.Parcel
import android.os.Parcelable

class Dehelmintization() : Parcelable {
    var id: String? = null
    var name: String? = null
    var manufacturer: String? = null
    var dose: String? = null
    var date: String? = null
    var time: String? = null
    var veterinarian: String? = null
    var description: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        manufacturer = parcel.readString()
        dose = parcel.readString()
        date = parcel.readString()
        time = parcel.readString()
        veterinarian = parcel.readString()
        description = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(manufacturer)
        parcel.writeString(dose)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(veterinarian)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dehelmintization> {
        override fun createFromParcel(parcel: Parcel): Dehelmintization {
            return Dehelmintization(parcel)
        }

        override fun newArray(size: Int): Array<Dehelmintization?> {
            return arrayOfNulls(size)
        }
    }
}