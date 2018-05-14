package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
import java.time.Duration

@Siren4JEntity(
        name = "user",
        suppressClassProperty = true,
        uri = "/api/user",
        links = []
)
class AuthUserOutputModel (
        val username: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val personalEmail: String = "",
        val organizationEmail: String = "",
        val gender: String = "",
        val version: Int = 0,
        @Siren4JProperty(name = "privilege")
        val userPrivilege: String = "",
        @Siren4JProperty(name = "reputation")
        val userReputation: Int = 0
) : BaseResource ()