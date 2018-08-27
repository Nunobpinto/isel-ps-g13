package isel.ps.eduwikimobile.domain.model.single

import android.os.Parcel
import android.os.Parcelable

data class Course (
        var courseId: Int = 0,
        val organizationId: Int = 0,
        val programmeId: Int? = null,
        val version: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val lecturedTerm: String? = null,
        val optional: Boolean? = null,
        val credits: Int? = null,
        val votes: Int = 0,
        val timestamp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(organizationId)
        parcel.writeValue(programmeId)
        parcel.writeInt(version)
        parcel.writeString(createdBy)
        parcel.writeString(fullName)
        parcel.writeString(shortName)
        parcel.writeString(lecturedTerm)
        parcel.writeValue(optional)
        parcel.writeValue(credits)
        parcel.writeInt(votes)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Course> {
        override fun createFromParcel(parcel: Parcel): Course {
            return Course(parcel)
        }

        override fun newArray(size: Int): Array<Course?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "course"
    }
}

