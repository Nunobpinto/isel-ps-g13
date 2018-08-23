package isel.ps.eduwikimobile.domain.model.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.model.single.Lecture

data class LectureCollection(
        val lectureList: Array<Lecture>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(Lecture)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(lectureList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LectureCollection> {
        override fun createFromParcel(parcel: Parcel): LectureCollection {
            return LectureCollection(parcel)
        }

        override fun newArray(size: Int): Array<LectureCollection?> {
            return arrayOfNulls(size)
        }
    }
}
