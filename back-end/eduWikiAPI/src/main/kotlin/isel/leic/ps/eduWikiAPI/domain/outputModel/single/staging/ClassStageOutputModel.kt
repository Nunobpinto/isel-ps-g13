package isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassStageOutputModel(
        val stagedId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val className: String = "",
        val lecturedTerm: String = "",
        val termId: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)