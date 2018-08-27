package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.TermCollection
import isel.ps.eduwikimobile.domain.model.single.Term
import isel.ps.eduwikimobile.exceptions.AppException

class TermCollectionParametersContainer (
        val courseId: Int,
        app: EduWikiApplication,
        successCb: (TermCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<TermCollection>(app, successCb, errorCb)