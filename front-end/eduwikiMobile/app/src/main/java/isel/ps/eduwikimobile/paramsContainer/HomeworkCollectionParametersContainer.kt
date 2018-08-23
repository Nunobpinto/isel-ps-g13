package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.HomeworkCollection
import isel.ps.eduwikimobile.exceptions.AppException

class HomeworkCollectionParametersContainer (
        val courseId: Int,
        val classId: Int,
        val app: EduWikiApplication,
        val successCb: (HomeworkCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer