package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import com.fasterxml.jackson.annotation.JsonProperty
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_SHEET
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class HomeworkVersionOutputModel(
        @JsonProperty("createdBy")
        val username: String = "",
        val version: Int = 1,
        val homeworkId: Int = 0,
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)