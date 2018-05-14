package isel.leic.ps.eduWikiAPI.domain.model

data class Organization(
        val id: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: Int = 0
)