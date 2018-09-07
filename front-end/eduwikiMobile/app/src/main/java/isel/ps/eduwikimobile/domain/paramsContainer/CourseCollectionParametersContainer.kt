package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.CourseCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class CourseCollectionParametersContainer (
        app: EduWikiApplication,
        successCb: (CourseCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<CourseCollection> (app, successCb, errorCb)
