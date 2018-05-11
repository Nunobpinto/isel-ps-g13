package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
import java.time.LocalDate

@Siren4JEntity(
        name = "exam",
        suppressClassProperty = true,
        uri = "/api/courses/{courseId}/terms/{termId}/exams/{examId}",
        links = []
)

class ExamOutputModel (
        val examId: Int = 0,
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        @Siren4JProperty(name = "full_name")
        val fullName: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        val votes: Int = 0
) : BaseResource()