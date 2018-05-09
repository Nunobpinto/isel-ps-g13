package isel.leic.ps.eduWikiAPI.domain.model;

import java.time.LocalDate

data class Homework (
        val id: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multiple_deliveries: Boolean = false
)
