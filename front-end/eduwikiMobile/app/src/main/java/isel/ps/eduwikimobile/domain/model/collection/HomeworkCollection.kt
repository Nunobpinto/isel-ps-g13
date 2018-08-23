package isel.ps.eduwikimobile.domain.model.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.model.single.Homework

data class HomeworkCollection(
        val homeworkList: Array<Homework>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(Homework)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(homeworkList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeworkCollection> {
        override fun createFromParcel(parcel: Parcel): HomeworkCollection {
            return HomeworkCollection(parcel)
        }

        override fun newArray(size: Int): Array<HomeworkCollection?> {
            return arrayOfNulls(size)
        }
    }
}