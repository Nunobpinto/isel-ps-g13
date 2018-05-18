package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization

interface OrganizationService {

    /**
     * Main entities queries
     */

    fun getOrganizationById(organizationId: Int) : Organization

    fun getAllOrganizations() : List<Organization>

    fun createOrganization(organizationInputModel: OrganizationInputModel, user: String)

    fun deleteOrganization(organizationId: Int)

    fun deleteAllOrganizations()

    fun updateOrganization()



}