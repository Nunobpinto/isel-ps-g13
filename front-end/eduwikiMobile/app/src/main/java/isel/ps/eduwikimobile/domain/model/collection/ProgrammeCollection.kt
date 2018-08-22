package isel.ps.eduwikimobile.domain.model.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.model.single.Programme

data class ProgrammeCollection (
        val programmeList: Array<Programme>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(Programme)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(programmeList, flags)
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