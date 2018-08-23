package isel.ps.eduwikimobile.domain.model.single

import android.os.Parcel
import android.os.Parcelable

data class CourseClass (
        val courseId: Int = 0,
        val createdBy: String = "",
        val timestamp: String = "",
        val votes: Int = 0,
        val lecturedTerm: String = "",
        val classId: Int = 0,
        val className: String = "",
        val courseShortName: String = "",
        val termId: Int = 0,
        val courseClassId: Int,
        val courseFullName: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeString(createdBy)
        parcel.writeString(timestamp)
        parcel.writeInt(votes)
        parcel.writeString(lecturedTerm)
        parcel.writeInt(classId)
        parcel.writeString(className)
        parcel.writeString(courseShortName)
        parcel.writeInt(termId)
        parcel.writeInt(courseClassId)
        parcel.writeString(courseFullName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseClass> {
        override fun createFromParcel(parcel: Parcel): CourseClass {
            return CourseClass(parcel)
        }

        override fun newArray(size: Int): Array<CourseClass?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Course Class"
    }
}