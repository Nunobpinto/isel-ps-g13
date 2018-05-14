package isel.leic.ps.eduWikiAPI.domain.model

data class Class(
        val id: Int = 0,
        val version: Int = 0,
        val votes: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val termId: String = ""
)