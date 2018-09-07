package isel.ps.eduwikimobile.domain.single

import android.os.Parcel
import android.os.Parcelable

data class Homework(
        val homeworkId: Int = 0,
        val version: Int = 0,
        val homeworkName: String = "",
        val createdBy: String = "",
        val sheetId: String? = null,
        val dueDate: String = "",
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val votes: Int = 0,
        val className: String = "",
        val lecturedTerm: String = "",
        val courseShortName: String = "",
        val timestamp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(homeworkId)
        parcel.writeInt(version)
        parcel.writeString(homeworkName)
        parcel.writeString(createdBy)
        parcel.writeString(sheetId)
        parcel.writeString(dueDate)
        parcel.writeByte(if (lateDelivery) 1 else 0)
        parcel.writeByte(if (multipleDeliveries) 1 else 0)
        parcel.writeInt(votes)
        parcel.writeString(className)
        parcel.writeString(lecturedTerm)
        parcel.writeString(courseShortName)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Homework> {
        override fun createFromParcel(parcel: Parcel): Homework {
            return Homework(parcel)
        }

        override fun newArray(size: Int): Array<Homework?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "homework"
    }

}