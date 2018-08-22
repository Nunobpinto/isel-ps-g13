package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.ClassCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CourseClassCollectionParametersContainer(
        val termId: Int,
        val courseId: Int,
        val app: EduWikiApplication,
        val successCb: (ClassCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer
