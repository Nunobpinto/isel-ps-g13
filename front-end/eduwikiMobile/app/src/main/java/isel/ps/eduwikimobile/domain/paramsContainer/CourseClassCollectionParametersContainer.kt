package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.CourseClassCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class CourseClassCollectionParametersContainer(
        val termId: Int? = null,
        val courseId: Int? = null,
        app: EduWikiApplication,
        successCb: (CourseClassCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<CourseClassCollection> (app, successCb, errorCb)
