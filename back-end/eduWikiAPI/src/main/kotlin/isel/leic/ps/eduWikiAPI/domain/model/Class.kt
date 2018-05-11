package isel.leic.ps.eduWikiAPI.domain.model

data class Class(
        val id: Int = -1,
        val version: Int = 1,
        val votes: Int = 1,
        val createdBy: String = "",
        val fullName: String = "",
        val termId: String = ""
)