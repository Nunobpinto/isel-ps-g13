package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.Term

data class TermCollection (
        val termList: Array<Term>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(Term)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(termList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TermCollection> {
        override fun createFromParcel(parcel: Parcel): TermCollection {
            return TermCollection(parcel)
        }

        override fun newArray(size: Int): Array<TermCollection?> {
            return arrayOfNulls(size)
        }
    }
}