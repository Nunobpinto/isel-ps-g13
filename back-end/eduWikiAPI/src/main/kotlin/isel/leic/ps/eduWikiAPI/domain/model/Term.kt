package isel.leic.ps.eduWikiAPI.domain.model;

import java.sql.Timestamp

data class Term (
        val id: Int = 0,
        val shortName: String = "",
        val year: String = "",
        val type: String = ""
)