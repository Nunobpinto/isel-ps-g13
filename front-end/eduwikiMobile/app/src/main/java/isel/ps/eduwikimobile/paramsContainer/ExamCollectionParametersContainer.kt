package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.ExamCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ExamCollectionParametersContainer (
        val termId: Int,
        val courseId: Int,
        val app: EduWikiApplication,
        val successCb: (ExamCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer