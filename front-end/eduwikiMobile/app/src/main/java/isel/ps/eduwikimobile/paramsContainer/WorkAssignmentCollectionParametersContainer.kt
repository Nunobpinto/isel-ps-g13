package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.WorkAssignmentCollection
import isel.ps.eduwikimobile.exceptions.AppException

class WorkAssignmentCollectionParametersContainer (
        val termId: Int,
        val courseId: Int,
        val app: EduWikiApplication,
        val successCb: (WorkAssignmentCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer