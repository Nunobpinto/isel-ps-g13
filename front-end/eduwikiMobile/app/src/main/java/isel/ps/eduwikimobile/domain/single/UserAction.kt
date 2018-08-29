package isel.ps.eduwikimobile.domain.single

import android.os.Parcel
import android.os.Parcelable

data class UserAction (
        val action_type: String = "",
        val action_user: String = "",
        val entity_type: String = "",
        val entity_link: String = "",
        val timestamp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(action_type)
        parcel.writeString(action_user)
        parcel.writeString(entity_type)
        parcel.writeString(entity_link)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAction> {
        override fun createFromParcel(parcel: Parcel): UserAction {
            return UserAction(parcel)
        }

        override fun newArray(size: Int): Array<UserAction?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "action_log"
    }
}