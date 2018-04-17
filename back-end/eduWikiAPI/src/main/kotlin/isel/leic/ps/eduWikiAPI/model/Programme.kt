package isel.leic.ps.eduWikiAPI.model

data class Programme(
        val id: Int,
        val fullName: String,
        val shortName: String,
        val academicDegree: String,
        val totalCredits: Int,
        val duration: Int
)