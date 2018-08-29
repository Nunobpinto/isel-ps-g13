package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.Course

data class CourseCollection (
        val courseList: Array<Course>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(Course)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(courseList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseCollection> {
        override fun createFromParcel(parcel: Parcel): CourseCollection {
            return CourseCollection(parcel)
        }

        override fun newArray(size: Int): Array<CourseCollection?> {
            return arrayOfNulls(size)
        }
    }
}