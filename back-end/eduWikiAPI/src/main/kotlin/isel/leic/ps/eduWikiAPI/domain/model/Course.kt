package isel.leic.ps.eduWikiAPI.domain.model

data class Course(
        val id: Int = 0,
        val version: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val lecturedTerm: Int = 0,
        val optional: Boolean = false,
        val credits: Int = 0
)