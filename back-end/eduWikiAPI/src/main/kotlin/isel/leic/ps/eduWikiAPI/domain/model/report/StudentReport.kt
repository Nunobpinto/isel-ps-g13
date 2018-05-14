package isel.leic.ps.eduWikiAPI.domain.model.report;

import javax.validation.constraints.Email

data class StudentReport (
        val reportId: Int = 0,
        val username: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val personalEmail: String = "",
        val studentOrganizationEmail: String = "",
        val gender: String = "",
        val madeBy: String = "",
        val votes: Int = 0
)
