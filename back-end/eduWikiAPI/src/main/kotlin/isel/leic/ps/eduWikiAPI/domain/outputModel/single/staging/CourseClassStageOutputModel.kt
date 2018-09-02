package isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging

import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseClassStageOutputModel (
        val stagedId: Int = 0,
        val courseId: Int = 0,
        val classId: Int = 0,
        val termId: Int = 0,
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val className: String,
        val termShortName: String,
        val courseShortName: String
)