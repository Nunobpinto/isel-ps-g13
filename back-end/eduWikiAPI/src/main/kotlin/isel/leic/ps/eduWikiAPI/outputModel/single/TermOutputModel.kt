package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import java.time.Year

@Siren4JEntity(
        name = "term",
        suppressClassProperty = true,
        //uri = "/api/classes/{classId}/courses/{courseId}/lessons/{lessonId}",
        links = []
)
class TermOutputModel (
        val id: Int = 0,
        @Siren4JProperty(name = "short_name")
        val shortName: String = "",
        val year: Year = Year.now(),
        val type: String = "",
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = ""
)