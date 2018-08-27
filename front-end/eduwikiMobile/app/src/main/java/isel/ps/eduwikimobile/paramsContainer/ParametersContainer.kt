package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.exceptions.AppException

abstract class ParametersContainer<T>(
        val app: EduWikiApplication,
        val successCb: (T) -> Unit,
        val errorCb: (AppException) -> Unit
)