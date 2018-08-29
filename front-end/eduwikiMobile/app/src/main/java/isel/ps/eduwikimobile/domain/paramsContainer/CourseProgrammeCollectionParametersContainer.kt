package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.CourseProgrammeCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CourseProgrammeCollectionParametersContainer(
        val programmeId: Int,
        app: EduWikiApplication,
        successCb: (CourseProgrammeCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<CourseProgrammeCollection>(app, successCb, errorCb)