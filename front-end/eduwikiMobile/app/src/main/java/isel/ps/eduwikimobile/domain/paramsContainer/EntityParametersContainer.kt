package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class EntityParametersContainer<T> (
        app: EduWikiApplication,
        successCb: (T) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<T>(app, successCb, errorCb)