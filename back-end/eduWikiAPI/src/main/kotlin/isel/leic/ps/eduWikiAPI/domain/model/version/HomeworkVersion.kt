package isel.leic.ps.eduWikiAPI.domain.model.version

import java.time.LocalDate

data class HomeworkVersion (
      val homeworkId: Int = 0,
      val sheet: String = "",
      val dueDate: LocalDate = LocalDate.now(),
      val createdBy: String = "",
      val version: Int = 0,
      val lateDelivery: Boolean = false,
      val multipleDeliveries: Boolean = false
)