package isel.leic.ps.eduWikiAPI.domain.outputModel.single.version

import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseVersionOutputModel(
        val version: Int = 1,
        val courseId: Int = 0,
        val organizationId: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val createdBy: String = ""
)