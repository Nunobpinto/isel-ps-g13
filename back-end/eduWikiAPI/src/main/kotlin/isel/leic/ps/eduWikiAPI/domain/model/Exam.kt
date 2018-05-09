package isel.leic.ps.eduWikiAPI.domain.model;

import java.time.LocalDate;

data class Exam (
        val id: Int = 0,
        val version: Int = 0,
        val createdby: String = "",
        val sheet: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = ""
)
