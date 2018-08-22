package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.TermCollection
import isel.ps.eduwikimobile.exceptions.AppException

class TermCollectionParametersContainer (
        val courseId: Int,
        val app: EduWikiApplication,
        val successCb: (TermCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer