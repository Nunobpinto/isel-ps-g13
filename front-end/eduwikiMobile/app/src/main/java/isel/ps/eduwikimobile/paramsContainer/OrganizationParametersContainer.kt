package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.exceptions.AppException

class OrganizationParametersContainer (
        val app: EduWikiApplication,
        val successCb: (Organization) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer