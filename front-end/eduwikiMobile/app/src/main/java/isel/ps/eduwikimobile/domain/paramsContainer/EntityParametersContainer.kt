package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.exceptions.AppException

class EntityParametersContainer<T> (
        app: EduWikiApplication,
        successCb: (T) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<T>(app, successCb, errorCb)