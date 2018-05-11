package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

@Siren4JEntity(
        name = "lecture",
        suppressClassProperty = true,
        uri = "/api/classes/{classId}/courses/{courseId}/lessons/{lessonId}",
        links = []
)
class LectureOutputModel (
    val lectureId: Int = 0,
    val version: Int = 0,
    @Siren4JProperty(name = "created_by")
    val username: String = "",
    val weekDay: DayOfWeek = DayOfWeek.MONDAY,
    val begins: LocalDate = LocalDate.now(),
    val duration: Duration = Duration.ZERO,
    val location: String = ""
) : BaseResource()
