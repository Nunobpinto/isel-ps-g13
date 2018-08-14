package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseClassOutputModel(
        val courseId: Int = 0,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val votes: Int = 0,
        val lecturedTerm: String = "",
        val classId: Int = 0,
        val className: String = "",
        val courseName: String = "",
        val termId: Int = 0,
        val courseClassId: Int
)