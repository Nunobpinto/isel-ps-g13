package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.ProgrammeCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ProgrammeCollectionParametersContainer(
        val app: EduWikiApplication,
        val successCb: (ProgrammeCollection) -> Unit,
        val errorCb: (AppException) -> Unit
) : IParametersContainer