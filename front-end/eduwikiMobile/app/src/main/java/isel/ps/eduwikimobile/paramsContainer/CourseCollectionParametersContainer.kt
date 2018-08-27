package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.CourseCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CourseCollectionParametersContainer (
        app: EduWikiApplication,
        successCb: (CourseCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<CourseCollection> (app, successCb, errorCb)
