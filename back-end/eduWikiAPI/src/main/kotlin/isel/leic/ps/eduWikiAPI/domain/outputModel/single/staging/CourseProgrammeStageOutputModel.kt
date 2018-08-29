package isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeStageOutputModel (
        val stagedId: Int = 0,
        val courseId: Int = 0,
        val programmeId: Int = 0,
        val createdBy: String = "",
        val lecturedTerm: String = "",
        val optional: Boolean = false,
        val credits: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val courseShortName: String = ""
)