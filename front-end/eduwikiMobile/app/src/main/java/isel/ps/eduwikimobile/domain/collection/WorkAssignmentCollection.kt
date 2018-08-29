package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.WorkAssignment

data class WorkAssignmentCollection (
        val workAssignmentList: Array<WorkAssignment>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(WorkAssignment)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(workAssignmentList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkAssignmentCollection> {
        override fun createFromParcel(parcel: Parcel): WorkAssignmentCollection {
            return WorkAssignmentCollection(parcel)
        }

        override fun newArray(size: Int): Array<WorkAssignmentCollection?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Work Assignment"
    }
}