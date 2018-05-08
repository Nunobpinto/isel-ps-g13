package isel.leic.ps.eduWikiAPI.domain.model

data class Organization(
        val id: Int,
        val version: Int,
        val fullName: String,
        val shortName: String,
        val address: String,
        val contact: String
)