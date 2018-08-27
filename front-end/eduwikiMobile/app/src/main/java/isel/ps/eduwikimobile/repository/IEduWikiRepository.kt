package isel.ps.eduwikimobile.repository

import isel.ps.eduwikimobile.paramsContainer.LoginParametersContainer
import isel.ps.eduwikimobile.paramsContainer.ParametersContainer
import isel.ps.eduwikimobile.paramsContainer.ResourceParametersContainer

interface IEduWikiRepository {
    fun <T> getEntity(uri: String, klass: Class<T>, params: ParametersContainer<T>)
    fun getResourceFile(uri: String, params: ResourceParametersContainer)
    fun getUser(uri: String, params: LoginParametersContainer)
}