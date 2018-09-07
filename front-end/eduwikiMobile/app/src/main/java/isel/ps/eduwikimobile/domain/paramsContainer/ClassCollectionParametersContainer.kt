package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.ClassCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class ClassCollectionParametersContainer (
        val termId: Int? = null,
        val courseId: Int? = null,
        app: EduWikiApplication,
        successCb: (ClassCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<ClassCollection> (app, successCb, errorCb)
