package isel.leic.ps.eduWikiAPI.domain.model;

import java.time.Year

data class Term (
        val id: Int = 0,
        val shortName: String = "",
        val year: Year = Year.now(),
        val type: String = "",
        val version: Int = 0,
        val createdBy: String = ""
)