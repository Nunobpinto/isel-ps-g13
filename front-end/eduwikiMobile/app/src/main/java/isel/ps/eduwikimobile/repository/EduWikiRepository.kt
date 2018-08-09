package isel.ps.eduwikimobile.repository

import android.content.Context
import android.net.ConnectivityManager
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.API_URL
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.comms.HttpRequest
import isel.ps.eduwikimobile.domain.model.Programme
import java.util.*

class EduWikiRepository() : IEduWikiRepository {

    private val ALL_PROGRAMMES = API_URL + "/programmes/1"

    override fun getAllProgrammes(ctx: Context, successCb: (Programme) -> Unit, errorCb: (VolleyError) -> Unit) {
        if(!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        val tag = UUID.randomUUID().toString()
        val req = HttpRequest(
                ALL_PROGRAMMES,
                "emU6MTIzNA==",
                Programme::class.java,
                { programme -> successCb(programme)},
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}