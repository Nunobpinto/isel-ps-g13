package isel.leic.ps.eduWikiAPI.model

data class Class(
        val fullName: String,
        val id: Int,
        val course: Course,
        val term: String
)