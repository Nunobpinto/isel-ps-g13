package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.CourseProgrammeCollection
import isel.ps.eduwikimobile.exceptions.AppException

class CourseProgrammeCollectionParametersContainer(
        val programmeId: Int,
        app: EduWikiApplication,
        successCb: (CourseProgrammeCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<CourseProgrammeCollection>(app, successCb, errorCb)