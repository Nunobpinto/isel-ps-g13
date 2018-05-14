package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
@Siren4JEntity(
        name = "organization",
        suppressClassProperty = true,
        uri = "/api/organization",
        links = []
)

class OrganizationOutputModel (
        val organizationId: Int = 0,
        val version: Int = 0,
        @Siren4JProperty(name = "created_by")
        val username: String = "",
        @Siren4JProperty(name = "full_name")
        val fullName: String = "",
        @Siren4JProperty(name = "short_name")
        val shortName: String = "",
        val address: String = "",
        val contact: String = ""
) : BaseResource()