package isel.leic.ps.eduWikiAPI.domain.outputModel.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class HomeworkStageOutputModel (
        val stagedId: Int = 0,
        val version: Int = 0,
        @JsonProperty( "createdBy")
        val username: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)