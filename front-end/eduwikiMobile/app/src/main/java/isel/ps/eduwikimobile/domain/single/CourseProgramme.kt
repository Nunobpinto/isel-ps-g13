package isel.ps.eduwikimobile.domain.single

import android.os.Parcel
import android.os.Parcelable

data class CourseProgramme (
        val courseId: Int = 0,
        val programmeId: Int = 0,
        val version: Int = 0,
        val programmeShortName: String = "",
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val lecturedTerm: String = "",
        val optional: Boolean = false,
        val credits: Int = 0,
        val votes: Int = 0,
        val timestamp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(programmeId)
        parcel.writeInt(version)
        parcel.writeString(programmeShortName)
        parcel.writeString(createdBy)
        parcel.writeString(fullName)
        parcel.writeString(shortName)
        parcel.writeString(lecturedTerm)
        parcel.writeByte(if (optional) 1 else 0)
        parcel.writeInt(credits)
        parcel.writeInt(votes)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseProgramme> {
        override fun createFromParcel(parcel: Parcel): CourseProgramme {
            return CourseProgramme(parcel)
        }

        override fun newArray(size: Int): Array<CourseProgramme?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "course_programme"
    }
}