package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse
import isel.ps.eduwikimobile.ui.activities.MainActivity

class ResourceParametersContainer (
        val activity: MainActivity,
        val resourceId: String,
        app: EduWikiApplication,
        successCb: (Unit) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<Unit>(app, successCb, errorCb)