package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.CourseCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CourseCollectionParametersContainer (
        val programmeId: Int? = null,
        val app: EduWikiApplication,
        val successCb: (CourseCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer
