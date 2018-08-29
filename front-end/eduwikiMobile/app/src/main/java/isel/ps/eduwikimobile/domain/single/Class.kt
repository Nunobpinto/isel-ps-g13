package isel.ps.eduwikimobile.domain.single

import android.os.Parcel
import android.os.Parcelable

data class Class (
        val classId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val className: String = "",
        val termId: Int = 0,
        val lecturedTerm: String = "",
        val votes: Int = 0,
        val timestamp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(classId)
        parcel.writeInt(version)
        parcel.writeString(createdBy)
        parcel.writeString(className)
        parcel.writeInt(termId)
        parcel.writeString(lecturedTerm)
        parcel.writeInt(votes)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Class> {
        override fun createFromParcel(parcel: Parcel): Class {
            return Class(parcel)
        }

        override fun newArray(size: Int): Array<Class?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "class"
    }
}