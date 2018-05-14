package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(
        name = "class",
        suppressClassProperty = true,
        uri = "/api/classes/{classId}",
        links = []
)
data class ClassOutputModel(
        val classId: Int = 0,
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        @Siren4JProperty(name = "full_name")
        val fullName: String = "",
        val termId: Int = 0,
        val votes: Int = 0
) : BaseResource()