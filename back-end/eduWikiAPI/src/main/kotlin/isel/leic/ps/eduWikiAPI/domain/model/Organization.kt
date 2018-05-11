package isel.leic.ps.eduWikiAPI.domain.model

data class Organization(
        val id: Int = -1,
        val version: Int = 1,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: String = ""
)