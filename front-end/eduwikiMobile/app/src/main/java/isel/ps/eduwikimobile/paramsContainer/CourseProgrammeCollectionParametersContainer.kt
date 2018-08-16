package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.CourseProgrammeCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CourseProgrammeCollectionParametersContainer (
        val programmeId: Int,
        val app: EduWikiApplication,
        val successCb: (CourseProgrammeCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer