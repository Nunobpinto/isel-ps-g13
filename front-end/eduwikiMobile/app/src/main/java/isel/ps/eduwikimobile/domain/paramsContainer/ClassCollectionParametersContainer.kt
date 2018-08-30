package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.ClassCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ClassCollectionParametersContainer (
        val termId: Int? = null,
        val courseId: Int? = null,
        app: EduWikiApplication,
        successCb: (ClassCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<ClassCollection> (app, successCb, errorCb)