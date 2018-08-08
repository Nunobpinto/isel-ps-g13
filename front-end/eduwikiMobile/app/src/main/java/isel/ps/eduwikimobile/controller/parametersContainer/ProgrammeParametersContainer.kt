package isel.ps.eduwikimobile.controller.parametersContainer

import isel.ps.eduwikimobile.domain.model.Programme
import isel.ps.eduwikimobile.domain.model.ProgrammeCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ProgrammeParametersContainer(
        val successCb: (Pair<ProgrammeCollection?, Programme?>) -> Unit,
        val errorCb: (AppException) -> Unit
)