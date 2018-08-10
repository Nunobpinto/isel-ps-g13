package isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class WorkAssignmentStageOutputModel(
        val stagedId: Int = 0,
        @JsonProperty("createdBy")
        val username: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val sheetId: UUID = UUID.randomUUID(),
        val supplementId: UUID = UUID.randomUUID()
)