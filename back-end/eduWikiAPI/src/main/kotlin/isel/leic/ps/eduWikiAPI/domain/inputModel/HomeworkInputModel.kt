package isel.leic.ps.eduWikiAPI.domain.inputModel

import java.time.LocalDate
import java.util.*

class HomeworkInputModel (
        val sheetId: UUID = UUID.randomUUID(),
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val createdBy: String = ""
)