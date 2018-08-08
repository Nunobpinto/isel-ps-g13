package isel.ps.eduwikimobile.domain.model

import android.os.Parcel
import android.os.Parcelable

data class ProgrammeCollection (
        val programmes: List<Programme>
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Programme)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(programmes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProgrammeCollection> {
        override fun createFromParcel(parcel: Parcel): ProgrammeCollection {
            return ProgrammeCollection(parcel)
        }

        override fun newArray(size: Int): Array<ProgrammeCollection?> {
            return arrayOfNulls(size)
        }
    }
}