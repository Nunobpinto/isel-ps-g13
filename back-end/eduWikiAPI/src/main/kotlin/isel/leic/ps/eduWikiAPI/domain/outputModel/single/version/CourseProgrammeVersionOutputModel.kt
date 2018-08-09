package isel.leic.ps.eduWikiAPI.domain.outputModel.single.version

import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeVersionOutputModel(
        val version: Int = 1,
        val courseId: Int = 0,
        val programmeId: Int = 0,
        val lecturedTerm: String = "",
        val optional: Boolean = false,
        val credits: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val createdBy: String = ""
)