package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.single.Programme
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class UserProgrammeParametersContainer(
        app: EduWikiApplication,
        successCb: (Programme) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<Programme>(app, successCb, errorCb)