package isel.ps.eduwikimobile.domain.model.single

import android.os.Parcel
import android.os.Parcelable

data class Organization (
        val organizationId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: String = "",
        val votes: Int = 0,
        val timestamp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(organizationId)
        parcel.writeInt(version)
        parcel.writeString(createdBy)
        parcel.writeString(fullName)
        parcel.writeString(shortName)
        parcel.writeString(address)
        parcel.writeString(contact)
        parcel.writeInt(votes)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Organization> {
        override fun createFromParcel(parcel: Parcel): Organization {
            return Organization(parcel)
        }

        override fun newArray(size: Int): Array<Organization?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "organization"
    }
}