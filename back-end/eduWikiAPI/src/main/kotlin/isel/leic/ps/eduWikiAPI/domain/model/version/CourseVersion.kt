package isel.leic.ps.eduWikiAPI.domain.model.version

import java.sql.Timestamp

data class CourseVersion (
        val courseId: Int = 0,
        val organizationId: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0),
        val version: Int = 0
)