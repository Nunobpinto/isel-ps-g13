package isel.leic.ps.eduWikiAPI.domain.model.report;

data class ClassReport(
        val reportId: Int = 0,
        val classId: Int = 0,
        val className: String = "",
        val termId: Int = 0,
        val createdBy: String = "",
        val votes: Int = 0
)
