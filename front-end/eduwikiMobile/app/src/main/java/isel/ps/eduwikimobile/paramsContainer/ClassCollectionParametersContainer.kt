package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.ClassCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ClassCollectionParametersContainer (
        val app: EduWikiApplication,
        val successCb: (ClassCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer
