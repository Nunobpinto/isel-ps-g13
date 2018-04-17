package isel.leic.ps.eduWikiAPI.model

data class Course(
        val id: Int,
        val fullName: String,
        val shortName: String,
        val semester: Int,
        val optional: Boolean,
        val credits: Int
)