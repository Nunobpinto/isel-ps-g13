package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.ClassCollection
import isel.ps.eduwikimobile.domain.model.collection.CourseClassCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CourseClassCollectionParametersContainer(
        val termId: Int? = null,
        val courseId: Int? = null,
        app: EduWikiApplication,
        successCb: (CourseClassCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<CourseClassCollection> (app, successCb, errorCb)
