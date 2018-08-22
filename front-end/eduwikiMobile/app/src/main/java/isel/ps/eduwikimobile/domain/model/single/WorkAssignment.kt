package isel.ps.eduwikimobile.domain.model.single

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*

data class WorkAssignment(
        val workAssignmentId: Int = -1,
        val version: Int = 1,
        val votes: Int = 0,
        val phase: String = "",
        val createdBy: String = "",
        val sheetId: String = "",
        val supplementId: String = "",
        val dueDate: String = "",
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val timestamp: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(workAssignmentId)
        parcel.writeInt(version)
        parcel.writeInt(votes)
        parcel.writeString(phase)
        parcel.writeString(createdBy)
        parcel.writeString(sheetId)
        parcel.writeString(supplementId)
        parcel.writeString(dueDate)
        parcel.writeByte(if (individual) 1 else 0)
        parcel.writeByte(if (lateDelivery) 1 else 0)
        parcel.writeByte(if (multipleDeliveries) 1 else 0)
        parcel.writeByte(if (requiresReport) 1 else 0)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkAssignment> {
        override fun createFromParcel(parcel: Parcel): WorkAssignment {
            return WorkAssignment(parcel)
        }

        override fun newArray(size: Int): Array<WorkAssignment?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Work Assignment"
    }
}