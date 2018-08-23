package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.LectureCollection
import isel.ps.eduwikimobile.exceptions.AppException

class LectureCollectionParametersContainer (
        val courseId: Int,
        val classId: Int,
        val app: EduWikiApplication,
        val successCb: (LectureCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer