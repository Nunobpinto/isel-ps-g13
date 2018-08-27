package isel.ps.eduwikimobile.domain.model.single

import android.os.Parcel
import android.os.Parcelable

data class Exam (
        val examId: Int = 0,
        val version: Int = 1,
        val votes: Int = 0,
        val createdBy: String = "",
        val sheetId: String? = null,
        val dueDate: String = "",
        val type: String = "",
        val phase: String = "",
        val location: String = "",
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
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(examId)
        parcel.writeInt(version)
        parcel.writeInt(votes)
        parcel.writeString(createdBy)
        parcel.writeString(sheetId)
        parcel.writeString(dueDate)
        parcel.writeString(type)
        parcel.writeString(phase)
        parcel.writeString(location)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exam> {
        override fun createFromParcel(parcel: Parcel): Exam {
            return Exam(parcel)
        }

        override fun newArray(size: Int): Array<Exam?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "exam"
    }
}