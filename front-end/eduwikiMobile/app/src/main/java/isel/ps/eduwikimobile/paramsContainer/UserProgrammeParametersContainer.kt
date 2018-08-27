package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.exceptions.AppException

class UserProgrammeParametersContainer(
        app: EduWikiApplication,
        successCb: (Programme) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<Programme>(app, successCb, errorCb)