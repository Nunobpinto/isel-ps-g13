package isel.leic.ps.eduWikiAPI.domain.model

data class Course(
        val id: Int = -1,
        val organizationId: Int = 0,
        val version: Int = 1,
        val votes: Int = 1,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val lecturedTerm: Int = 0,
        val programmeId: Int = 0,
        val optional: Boolean = false,
        val credits: Int = 0
)