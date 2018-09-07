package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.HomeworkCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class HomeworkCollectionParametersContainer (
        val courseId: Int,
        val classId: Int,
        app: EduWikiApplication,
        successCb: (HomeworkCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<HomeworkCollection> (app, successCb, errorCb)