package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.ExamCollection
import isel.ps.eduwikimobile.domain.single.Exam
import isel.ps.eduwikimobile.exceptions.AppException

class ExamCollectionParametersContainer(
        val termId: Int,
        val courseId: Int,
        app: EduWikiApplication,
        successCb: (ExamCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<ExamCollection>(app, successCb, errorCb)