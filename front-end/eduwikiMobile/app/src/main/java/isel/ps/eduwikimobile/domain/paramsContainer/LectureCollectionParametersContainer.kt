package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.LectureCollection
import isel.ps.eduwikimobile.exceptions.AppException

class LectureCollectionParametersContainer (
        val courseId: Int,
        val classId: Int,
        app: EduWikiApplication,
        successCb: (LectureCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<LectureCollection>(app, successCb, errorCb)