package com.hhh.paws.database.model

import android.os.Parcel
import android.os.Parcelable

class Vaccine() : Parcelable {
    var id: String? = null
    var type: String? = null
    var name: String? = null
    var manufacturer: String? = null
    var dateOfVaccination: String? = null
    var validUntil: String? = null
    var veterinarian: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        type = parcel.readString()
        name = parcel.readString()
        manufacturer = parcel.readString()
        dateOfVaccination = parcel.readString()
        validUntil = parcel.readString()
        veterinarian = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(manufacturer)
        parcel.writeString(dateOfVaccination)
        parcel.writeString(validUntil)
        parcel.writeString(veterinarian)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vaccine> {
        override fun createFromParcel(parcel: Parcel): Vaccine {
            return Vaccine(parcel)
        }

        override fun newArray(size: Int): Array<Vaccine?> {
            return arrayOfNulls(size)
        }
    }
}