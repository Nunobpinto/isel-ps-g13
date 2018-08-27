package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.ProgrammeCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ProgrammeCollectionParametersContainer(
        app: EduWikiApplication,
        successCb: (ProgrammeCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<ProgrammeCollection> (app, successCb, errorCb)