package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.WorkAssignmentCollection
import isel.ps.eduwikimobile.exceptions.AppException

class WorkAssignmentCollectionParametersContainer (
        val termId: Int,
        val courseId: Int,
        app: EduWikiApplication,
        successCb: (WorkAssignmentCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<WorkAssignmentCollection>(app, successCb, errorCb)