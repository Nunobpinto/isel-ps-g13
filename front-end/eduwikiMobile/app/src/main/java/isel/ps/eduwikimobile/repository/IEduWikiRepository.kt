package isel.ps.eduwikimobile.repository

import android.content.Context
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.domain.model.collection.*
import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.domain.model.single.UserAction
import isel.ps.eduwikimobile.paramsContainer.ParametersContainer
import isel.ps.eduwikimobile.paramsContainer.ResourceParametersContainer

interface IEduWikiRepository {
    fun <T> getEntity(uri: String, klass: Class<T>, params: ParametersContainer<T>)
    fun getResourceFile(uri: String, params: ResourceParametersContainer)

    fun getUserFollowingItems(ctx: Context, successCb: (FollowingCollection) -> Unit, errorCb: (VolleyError) -> Unit)
}