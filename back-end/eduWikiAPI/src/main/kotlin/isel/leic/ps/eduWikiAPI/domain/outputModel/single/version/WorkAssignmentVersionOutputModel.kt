package isel.leic.ps.eduWikiAPI.domain.outputModel.single.version

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class WorkAssignmentVersionOutputModel (
        val version: Int = 1,
        val workAssignmentId: Int = 0,
        val phase: String = "",
        val sheetId: UUID? = UUID.randomUUID(),
        val supplementId: UUID? = UUID.randomUUID(),
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val courseShortName: String = "",
        val termShortName: String = ""
)