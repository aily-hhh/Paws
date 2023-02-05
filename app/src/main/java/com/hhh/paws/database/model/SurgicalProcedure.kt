package com.hhh.paws.database.model

import android.os.Parcel
import android.os.Parcelable

class SurgicalProcedure() : Parcelable {
    var id: String? = null
    var type: String? = null
    var name: String? = null
    var description: String? = null
    var anesthesia: String? = null
    var date: String? = null
    var veterinarian: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        type = parcel.readString()
        name = parcel.readString()
        description = parcel.readString()
        anesthesia = parcel.readString()
        date = parcel.readString()
        veterinarian = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(anesthesia)
        parcel.writeString(date)
        parcel.writeString(veterinarian)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SurgicalProcedure> {
        override fun createFromParcel(parcel: Parcel): SurgicalProcedure {
            return SurgicalProcedure(parcel)
        }

        override fun newArray(size: Int): Array<SurgicalProcedure?> {
            return arrayOfNulls(size)
        }
    }
}