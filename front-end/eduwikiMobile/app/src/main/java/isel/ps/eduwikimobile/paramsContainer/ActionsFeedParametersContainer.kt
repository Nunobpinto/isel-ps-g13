package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.UserActionCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ActionsFeedParametersContainer (
        app: EduWikiApplication,
        successCb: (UserActionCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<UserActionCollection>(app, successCb, errorCb)