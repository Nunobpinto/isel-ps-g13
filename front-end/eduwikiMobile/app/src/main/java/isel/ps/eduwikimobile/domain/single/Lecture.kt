package isel.ps.eduwikimobile.domain.single

import android.os.Parcel
import android.os.Parcelable

data class Lecture(
        val lectureId: Int = 0,
        val version: Int = 0,
        val className: String = "",
        val lecturedTerm: String = "",
        val courseShortName: String = "",
        val createdBy: String = "",
        val weekDay: String = "",
        val begins: String = "",
        val duration: String = "",
        val location: String = "",
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
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(lectureId)
        parcel.writeInt(version)
        parcel.writeString(className)
        parcel.writeString(lecturedTerm)
        parcel.writeString(courseShortName)
        parcel.writeString(createdBy)
        parcel.writeString(weekDay)
        parcel.writeString(begins)
        parcel.writeString(duration)
        parcel.writeString(location)
        parcel.writeInt(votes)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lecture> {
        override fun createFromParcel(parcel: Parcel): Lecture {
            return Lecture(parcel)
        }

        override fun newArray(size: Int): Array<Lecture?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "lecture"
    }

}
