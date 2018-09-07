package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.TermCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class TermCollectionParametersContainer (
        val courseId: Int,
        app: EduWikiApplication,
        successCb: (TermCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<TermCollection>(app, successCb, errorCb)