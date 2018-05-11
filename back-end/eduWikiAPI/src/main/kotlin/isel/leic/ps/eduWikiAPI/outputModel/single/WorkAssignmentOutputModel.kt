package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
import java.time.LocalDate

@Siren4JEntity(
        name = "work_items",
        suppressClassProperty = true,
        uri = "/api/courses/{courseId}/terms/{termId}/workitems/{workItemId}",
        links = []
)

class WorkAssignmentOutputModel(
        val workAssignmentId: Int = 0,
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        val supplement: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false
) : BaseResource()