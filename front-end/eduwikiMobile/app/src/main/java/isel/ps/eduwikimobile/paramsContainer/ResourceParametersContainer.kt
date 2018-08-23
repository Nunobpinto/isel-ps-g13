package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.exceptions.AppException

class ResourceParametersContainer (
        val resourceId: String,
        val app: EduWikiApplication,
        val successCb: () -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer