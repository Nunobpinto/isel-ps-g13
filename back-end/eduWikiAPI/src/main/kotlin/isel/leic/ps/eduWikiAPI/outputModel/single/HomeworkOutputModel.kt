package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
import java.time.LocalDate

@Siren4JEntity(
        name = "lecture",
        suppressClassProperty = true,
        uri = "/api/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}",
        links = []
)
class HomeworkOutputModel (
        val homeworkId: Int = 0,
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multiple_deliveries: Boolean = false
) : BaseResource()