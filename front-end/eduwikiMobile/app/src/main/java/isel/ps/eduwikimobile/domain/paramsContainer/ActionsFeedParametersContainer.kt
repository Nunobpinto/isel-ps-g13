package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.UserActionCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class ActionsFeedParametersContainer (
        app: EduWikiApplication,
        successCb: (UserActionCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<UserActionCollection>(app, successCb, errorCb)