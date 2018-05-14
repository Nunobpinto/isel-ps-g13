package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp

data class CourseClassStage (
        val courseId: Int = 0,
        val classId: Int = 0,
        val termId: Int = 0,
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0)
)