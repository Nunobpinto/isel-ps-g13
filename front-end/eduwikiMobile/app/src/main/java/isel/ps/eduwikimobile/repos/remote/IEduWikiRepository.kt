package isel.ps.eduwikimobile.repos.remote

import isel.ps.eduwikimobile.domain.paramsContainer.LoginParametersContainer
import isel.ps.eduwikimobile.domain.paramsContainer.ParametersContainer
import isel.ps.eduwikimobile.domain.paramsContainer.ResourceParametersContainer

interface IEduWikiRepository {
    fun <T> getEntity(uri: String, klass: Class<T>, params: ParametersContainer<T>)
    fun getResourceFile(uri: String, params: ResourceParametersContainer)
    fun getUser(uri: String, params: LoginParametersContainer)
}