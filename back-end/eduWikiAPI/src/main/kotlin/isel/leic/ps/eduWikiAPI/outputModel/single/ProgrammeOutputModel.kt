package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
import java.time.Duration

@Siren4JEntity(
        name = "programme",
        suppressClassProperty = true,
        uri = "/api/programmers/{programmeId}",
        links = []
)

class ProgrammeOutputModel(
        val programmeId: Int = 0,
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        @Siren4JProperty(name = "full_name")
        val fullName: String = "",
        @Siren4JProperty(name = "short_name")
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Duration = Duration.ZERO
) : BaseResource()