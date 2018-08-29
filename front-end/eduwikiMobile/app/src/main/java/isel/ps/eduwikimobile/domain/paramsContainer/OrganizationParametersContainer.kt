package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.single.Organization
import isel.ps.eduwikimobile.exceptions.AppException

class OrganizationParametersContainer(
        app: EduWikiApplication,
        successCb: (Organization) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<Organization>(app, successCb, errorCb)