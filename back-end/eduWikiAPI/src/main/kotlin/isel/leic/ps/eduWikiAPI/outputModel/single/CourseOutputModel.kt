package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(
        name = "course",
        suppressClassProperty = true,
        uri = "/api/courses/{courseId}",
        links = []
)
data class CourseOutputModel(
        val classId: Int = 0,
        val organizationId: Int = 0,
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        @Siren4JProperty(name = "full_name")
        val fullName: String = "",
        @Siren4JProperty(name = "short_name")
        val shortName: String = "",
        val lecturedTermId: Int = 0,
        val optional: Boolean = false,
        val credits: Int = 0
) : BaseResource()