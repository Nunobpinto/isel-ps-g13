package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.single.CourseClass
import isel.ps.eduwikimobile.domain.single.Programme

data class FollowingCollection(
        val programme: Programme? = null,
        val courses: Array<Course>,
        val classes: Array<CourseClass>
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Programme::class.java.classLoader),
            parcel.createTypedArray(Course),
            parcel.createTypedArray(CourseClass)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(programme, flags)
        parcel.writeTypedArray(courses, flags)
        parcel.writeTypedArray(classes, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FollowingCollection> {
        override fun createFromParcel(parcel: Parcel): FollowingCollection {
            return FollowingCollection(parcel)
        }

        override fun newArray(size: Int): Array<FollowingCollection?> {
            return arrayOfNulls(size)
        }
    }
}