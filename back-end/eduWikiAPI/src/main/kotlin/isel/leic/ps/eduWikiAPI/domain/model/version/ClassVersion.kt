package isel.leic.ps.eduWikiAPI.domain.model.version

import java.sql.Timestamp

data class ClassVersion (
    val classId: Int = 0,
    val className: String = "",
    val termId: Int = 0,
    val createdBy: String = "",
    val voteCount: Int = 0,
    val timestamp: Timestamp = Timestamp(0),
    val version: Int = 0
)