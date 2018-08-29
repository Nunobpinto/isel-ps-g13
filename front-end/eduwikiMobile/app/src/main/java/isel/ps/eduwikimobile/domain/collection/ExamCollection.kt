package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.Exam

data class ExamCollection (
        val examList: Array<Exam>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(Exam)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExamCollection> {
        override fun createFromParcel(parcel: Parcel): ExamCollection {
            return ExamCollection(parcel)
        }

        override fun newArray(size: Int): Array<ExamCollection?> {
            return arrayOfNulls(size)
        }
    }
}
