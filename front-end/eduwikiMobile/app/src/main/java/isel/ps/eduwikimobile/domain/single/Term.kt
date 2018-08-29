package isel.ps.eduwikimobile.domain.single

import android.os.Parcelable
import android.os.Parcel

data class Term(
        val termId: Int = 0,
        val shortName: String = "",
        val year: Int = 0,
        val type: String = "",
        val timestamp: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(termId)
        parcel.writeString(shortName)
        parcel.writeInt(year)
        parcel.writeString(type)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Term> {
        override fun createFromParcel(parcel: Parcel): Term {
            return Term(parcel)
        }

        override fun newArray(size: Int): Array<Term?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "term"
    }
}