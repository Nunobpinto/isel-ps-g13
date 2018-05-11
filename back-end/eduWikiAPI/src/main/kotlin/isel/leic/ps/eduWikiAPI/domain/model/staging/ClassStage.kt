package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp

data class ClassStage(
        val classId: Int = 0,
        val className: String = "",
        val termId: Int = 0,
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0)
)