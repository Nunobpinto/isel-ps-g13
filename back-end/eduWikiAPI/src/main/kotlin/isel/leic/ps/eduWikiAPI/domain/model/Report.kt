package isel.leic.ps.eduWikiAPI.domain.model

data class Report(
        val reportId: Int,
        val rowId: Int,
        val columnName: String,
        val madeBy: String,
        val votes: Int
)