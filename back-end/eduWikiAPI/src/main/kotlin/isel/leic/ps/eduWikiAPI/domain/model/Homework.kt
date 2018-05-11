package isel.leic.ps.eduWikiAPI.domain.model;

import java.time.LocalDate

data class Homework (
        val id: Int = -1,
        val version: Int = 1,
        val votes: Int = 1,
        val createdBy: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multiple_deliveries: Boolean = false
)
