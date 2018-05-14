package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp
import java.time.LocalDate

data class HomeworkStage (
        val classMiscUnitId: Int = 0,
        val sheet: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp(0)
)