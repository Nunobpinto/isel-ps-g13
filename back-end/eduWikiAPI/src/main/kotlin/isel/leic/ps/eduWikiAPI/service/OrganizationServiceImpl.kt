package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO

    override fun getOrganizationById(organizationId: Int) = organizationDAO.getOrganization(organizationId)

    override fun createOrganization(organizationInputModel: OrganizationInputModel, user: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteOrganization(organizationId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllOrganizations() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateOrganization() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllOrganizations(): List<Organization> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
