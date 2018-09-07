package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

abstract class ParametersContainer<T>(
        val app: EduWikiApplication,
        val successCb: (T) -> Unit,
        val errorCb: (ServerErrorResponse) -> Unit
)