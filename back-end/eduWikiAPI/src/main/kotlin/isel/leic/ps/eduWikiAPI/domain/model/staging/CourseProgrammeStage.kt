package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp

data class CourseProgrammeStage (
    val courseId: Int = 0,
    val programmeId: Int = 0,
    val lecturedTerm: String = "",
    val optional: Boolean = false,
    val credits: Int = 0,
    val createdBy: String = "",
    val votes: Int = 0,
    val timestamp: Timestamp = Timestamp(0)
)