package isel.ps.eduwikimobile.domain.model.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.model.single.CourseClass

data class CourseClassCollection (
        val courseClassList: Array<CourseClass>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(CourseClass)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(courseClassList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseClassCollection> {
        override fun createFromParcel(parcel: Parcel): CourseClassCollection {
            return CourseClassCollection(parcel)
        }

        override fun newArray(size: Int): Array<CourseClassCollection?> {
            return arrayOfNulls(size)
        }
    }
}