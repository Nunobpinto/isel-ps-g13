package isel.ps.eduwikimobile

import android.app.Application
import android.app.FragmentController

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.Course
import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.repository.EduWikiRepository
import isel.ps.eduwikimobile.repository.IEduWikiRepository
import isel.ps.eduwikimobile.service.IService
import isel.ps.eduwikimobile.service.Service

class EduWikiApplication : Application() {

    lateinit var requestQueue: RequestQueue
    lateinit var service: IService
    lateinit var repository: IEduWikiRepository
    lateinit var controller: AppController

    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
        repository = EduWikiRepository()
        service = Service(repository)
        controller = AppController(service)

    }

}