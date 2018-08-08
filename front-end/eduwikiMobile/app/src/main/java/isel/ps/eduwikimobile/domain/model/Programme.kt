package isel.ps.eduwikimobile.domain.model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp

data class Programme (
        val programmeId: Int = 0,
        val version: Int = 1,
        val username: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            TODO("timestamp")) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(programmeId)
        parcel.writeInt(version)
        parcel.writeString(username)
        parcel.writeString(fullName)
        parcel.writeString(shortName)
        parcel.writeString(academicDegree)
        parcel.writeInt(totalCredits)
        parcel.writeInt(duration)
        parcel.writeInt(votes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Programme> {
        override fun createFromParcel(parcel: Parcel): Programme {
            return Programme(parcel)
        }

        override fun newArray(size: Int): Array<Programme?> {
            return arrayOfNulls(size)
        }
    }
}