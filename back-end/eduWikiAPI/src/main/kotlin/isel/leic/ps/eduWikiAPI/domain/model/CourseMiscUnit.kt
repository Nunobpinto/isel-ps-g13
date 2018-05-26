package isel.leic.ps.eduWikiAPI.domain.model

import java.sql.Timestamp

class CourseMiscUnit (
    val id: Int? = 0,
    val miscType: String? = null,
    val courseId: Int? = 0,
    val termId: Int? = 0,
    val timestamp: Timestamp = Timestamp(1)
)