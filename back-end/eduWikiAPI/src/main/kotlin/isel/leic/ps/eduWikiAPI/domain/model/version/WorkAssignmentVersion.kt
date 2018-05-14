package isel.leic.ps.eduWikiAPI.domain.model.version

import java.sql.Timestamp
import java.time.LocalDate
import java.util.function.BooleanSupplier

data class WorkAssignmentVersion (
        val courseMiscUnitId: Int = 0,
        val sheet: String = "", //TODO
        val supplement: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp(0),
        val version: Int = 0
)