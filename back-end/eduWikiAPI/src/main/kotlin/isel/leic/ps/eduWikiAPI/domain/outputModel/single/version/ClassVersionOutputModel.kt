package isel.leic.ps.eduWikiAPI.domain.outputModel.single.version

import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassVersionOutputModel(
        val version: Int = 1,
        val classId: Int = 0,
        val termId: Int = 0,
        val className: String = "",
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val lecturedTerm: String,
        val programmeId: Int = 0,
        val programmeShortName: String
)