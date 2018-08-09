package isel.ps.eduwikimobile.controller.parametersContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.Programme
import isel.ps.eduwikimobile.domain.model.ProgrammeCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ProgrammeParametersContainer(
        val app: EduWikiApplication,
        val successCb: (Programme) -> Unit,
        val errorCb: (AppException) -> Unit
)