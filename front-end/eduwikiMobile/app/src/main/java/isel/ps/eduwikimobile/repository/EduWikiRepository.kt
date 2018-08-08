package isel.ps.eduwikimobile.repository

import android.content.Context
import android.net.ConnectivityManager
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.API_URL
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.comms.HttpRequest
import isel.ps.eduwikimobile.domain.model.Programme
import isel.ps.eduwikimobile.domain.model.ProgrammeCollection
import java.util.*

class EduWikiRepository : IEduWikiRepository {

    private val ALL_PROGRAMMES = API_URL + "/programmes"

    override fun getAllProgrammes(ctx: Context, successCb: (ProgrammeCollection, String) -> Unit, errorCb: (VolleyError) -> Unit) {
        if(!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        val tag = UUID.randomUUID().toString()
        val req = HttpRequest(
                ALL_PROGRAMMES,
                ProgrammeCollection::class.java,
                { programmes -> successCb(programmes, tag)},
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