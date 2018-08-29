package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.CourseClassCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CoursesOfSpecificClassParametersContainer (
        val classId: Int,
        app: EduWikiApplication,
        successCb: (CourseClassCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<CourseClassCollection>(app, successCb, errorCb)