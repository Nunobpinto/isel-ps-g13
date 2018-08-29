package isel.ps.eduwikimobile.domain.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.single.User
import isel.ps.eduwikimobile.exceptions.AppException

class LoginParametersContainer(
        val username: String,
        val password: String,
        app: EduWikiApplication,
        successCb: (User) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<User>(app, successCb, errorCb)