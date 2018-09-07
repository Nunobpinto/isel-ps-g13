package isel.ps.eduwikimobile

import android.app.Application

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.repos.remote.EduWikiRepository
import isel.ps.eduwikimobile.repos.remote.IEduWikiRepository
import isel.ps.eduwikimobile.service.Service

class EduWikiApplication : Application() {

    lateinit var requestQueue: RequestQueue
    lateinit var repository: IEduWikiRepository
    lateinit var controller: AppController

    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
        repository = EduWikiRepository()
        controller = AppController(Service(repository))
    }

}