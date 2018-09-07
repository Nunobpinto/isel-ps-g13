package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.CourseProgrammeCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class CourseProgrammeCollectionParametersContainer(
        val programmeId: Int,
        app: EduWikiApplication,
        successCb: (CourseProgrammeCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<CourseProgrammeCollection>(app, successCb, errorCb)