package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.single.Organization
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class OrganizationParametersContainer(
        app: EduWikiApplication,
        successCb: (Organization) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<Organization>(app, successCb, errorCb)