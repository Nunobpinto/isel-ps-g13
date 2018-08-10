package isel.ps.eduwikimobile

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import isel.ps.eduwikimobile.repository.EduWikiRepository
import isel.ps.eduwikimobile.repository.IEduWikiRepository

class EduWikiApplication : Application() {

    lateinit var requestQueue: RequestQueue
    lateinit var remoteRepository: IEduWikiRepository

    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
        remoteRepository = EduWikiRepository()
    }

}