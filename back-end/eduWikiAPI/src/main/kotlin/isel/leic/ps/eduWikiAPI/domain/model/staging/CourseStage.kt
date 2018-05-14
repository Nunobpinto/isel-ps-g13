package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp

data class CourseStage (
        val courseId: Int = 0,
        val organizationId: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0)
)