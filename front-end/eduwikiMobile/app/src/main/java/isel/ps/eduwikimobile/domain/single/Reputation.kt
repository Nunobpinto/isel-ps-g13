package isel.ps.eduwikimobile.domain.single

import android.os.Parcel
import android.os.Parcelable

data class Reputation (
        val points: Int = 0,
        val role: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(points)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reputation> {
        override fun createFromParcel(parcel: Parcel): Reputation {
            return Reputation(parcel)
        }

        override fun newArray(size: Int): Array<Reputation?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "reputation"
    }
}