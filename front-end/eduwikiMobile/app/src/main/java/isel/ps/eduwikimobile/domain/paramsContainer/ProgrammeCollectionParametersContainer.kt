package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.collection.ProgrammeCollection
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse

class ProgrammeCollectionParametersContainer(
        app: EduWikiApplication,
        successCb: (ProgrammeCollection) -> Unit,
        errorCb: (ServerErrorResponse) -> Unit
) : ParametersContainer<ProgrammeCollection> (app, successCb, errorCb)