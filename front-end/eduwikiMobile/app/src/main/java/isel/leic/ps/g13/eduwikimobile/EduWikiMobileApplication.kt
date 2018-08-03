package isel.leic.ps.g13.eduwikimobile

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class EduWikiMobileApplication : Application() {
    @Volatile lateinit var requestQueue: RequestQueue

    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
    }
}

