package isel.leic.ps.eduWikiAPI.domain.model

data class Programme(
        val id: Int,
        val version: Int,
        val fullName: String,
        val shortName: String,
        val academicDegree: String,
        val totalCredits: Int,
        val duration: Int
)