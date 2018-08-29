package isel.ps.eduwikimobile.domain.collection

import android.os.Parcel
import android.os.Parcelable
import isel.ps.eduwikimobile.domain.single.UserAction

data class UserActionCollection (
        val username: String = "",
        val actions: Array<UserAction>
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArray(UserAction)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeTypedArray(actions, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserActionCollection> {
        override fun createFromParcel(parcel: Parcel): UserActionCollection {
            return UserActionCollection(parcel)
        }

        override fun newArray(size: Int): Array<UserActionCollection?> {
            return arrayOfNulls(size)
        }
    }
}