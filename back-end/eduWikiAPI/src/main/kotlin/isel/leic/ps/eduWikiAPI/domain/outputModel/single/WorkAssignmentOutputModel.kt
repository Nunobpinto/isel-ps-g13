package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class WorkAssignmentOutputModel(
        val workAssignmentId: Int = -1,
        val version: Int = 1,
        val votes: Int = 0,
        val createdBy: String = "",
        val sheetId: UUID = UUID.randomUUID(),
        val supplementId: UUID = UUID.randomUUID(),
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)