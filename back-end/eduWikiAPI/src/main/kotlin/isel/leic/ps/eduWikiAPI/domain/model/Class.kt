package isel.leic.ps.eduWikiAPI.domain.model

data class Class(
        val id: Int = 0,
        val version: Int = 0,
        val courseId: Int = 0,
        val classId: Int = 0,
        val termId: String,
        val fullName: String = ""
)