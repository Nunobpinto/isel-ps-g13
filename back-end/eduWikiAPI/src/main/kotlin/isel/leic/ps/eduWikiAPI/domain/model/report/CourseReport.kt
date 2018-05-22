package isel.leic.ps.eduWikiAPI.domain.model.report

data class CourseReport (
        val reportId: Int = 0,
        val courseId: Int = 0,
        val courseFullName: String = "",
        val courseShortName: String = "",
        val reportedBy: String = "",
        val votes: Int = 0
)