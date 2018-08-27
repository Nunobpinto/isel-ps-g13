package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.FollowingCollection
import isel.ps.eduwikimobile.exceptions.AppException

class FollowingParametersContainer(
        app: EduWikiApplication,
        successCb: (FollowingCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<FollowingCollection>(app, successCb, errorCb)