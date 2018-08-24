package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.ui.activities.MainActivity

class ResourceParametersContainer (
        val activity: MainActivity,
        val resourceId: String,
        val app: EduWikiApplication,
        val successCb: () -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer