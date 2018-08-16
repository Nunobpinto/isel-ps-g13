package isel.ps.eduwikimobile

import android.app.Application

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.service.IService
import isel.ps.eduwikimobile.service.Service

class EduWikiApplication : Application() {

    lateinit var requestQueue: RequestQueue
    lateinit var service: IService
    var programme: Programme? = null
    var programmeId: Int? = null
    var organization: Organization? = null

    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
        service = Service()
    }

}