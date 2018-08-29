package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.Class

data class ClassCollection (
        val classList: Array<Class>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(Class)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(classList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClassCollection> {
        override fun createFromParcel(parcel: Parcel): ClassCollection {
            return ClassCollection(parcel)
        }

        override fun newArray(size: Int): Array<ClassCollection?> {
            return arrayOfNulls(size)
        }
    }
}