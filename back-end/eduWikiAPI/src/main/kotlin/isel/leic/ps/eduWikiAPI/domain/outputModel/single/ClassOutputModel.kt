package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassOutputModel(
        val classId: Int = 0,
        val programmeId: Int = 0,
        val programmeShortName: String,
        val version: Int = 0,
        val createdBy: String = "",
        val className: String = "",
        val termId: Int = 0,
        val lecturedTerm: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)