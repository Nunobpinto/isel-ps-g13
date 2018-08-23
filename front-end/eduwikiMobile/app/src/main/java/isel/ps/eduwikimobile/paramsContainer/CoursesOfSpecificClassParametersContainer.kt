package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.CourseClassCollection
import isel.ps.eduwikimobile.domain.model.collection.CourseCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CoursesOfSpecificClassParametersContainer (
        val classId: Int,
        val app: EduWikiApplication,
        val successCb: (CourseClassCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer