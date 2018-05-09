package isel.leic.ps.eduWikiAPI.domain.model.report

data class OrganizationReport (
        val reportId: Int = 0,
        val organizationId: Int = 0,
        val organizationFullName: String = "",
        val organizationShortName: String = "",
        val organizationAddress: String = "",
        val organizationContact: Int = 0,
        val madeBy:String = "",
        val votes: Int = 0
)