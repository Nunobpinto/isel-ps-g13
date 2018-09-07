package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.CourseProgramme

data class CourseProgrammeCollection (
        val courseProgrammeList: Array<CourseProgramme>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(CourseProgramme)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(courseProgrammeList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseProgrammeCollection> {
        override fun createFromParcel(parcel: Parcel): CourseProgrammeCollection {
            return CourseProgrammeCollection(parcel)
        }

        override fun newArray(size: Int): Array<CourseProgrammeCollection?> {
            return arrayOfNulls(size)
        }
    }
}