package isel.ps.eduwikimobile.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.API_URL
import isel.ps.eduwikimobile.comms.DownloadAsyncTask
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.comms.HttpRequest
import isel.ps.eduwikimobile.domain.model.collection.*
import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.domain.model.single.Programme
import java.util.*
import isel.ps.eduwikimobile.ui.activities.MainActivity
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.paramsContainer.DownloadFileContainer
import isel.ps.eduwikimobile.paramsContainer.ParametersContainer
import isel.ps.eduwikimobile.paramsContainer.ResourceParametersContainer

class EduWikiRepository : IEduWikiRepository {

    override fun <T> getEntity(uri: String, klass: Class<T>, params: ParametersContainer<T>) {
        if (!isConnected(params.app)) {
            return params.errorCb(AppException("There is no connection"))
        }
        makeRequest(
                params.app,
                uri,
                "YnJ1bm86MTIzNA==",
                klass,
                params.successCb,
                { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
        )
    }

    override fun getResourceFile(uri: String, params: ResourceParametersContainer) {
        if (!isConnected(params.app)) {
            return params.errorCb(AppException("There is no connection"))
        }
        downloadFileRequest(uri, params.activity)
    }

    override fun getUserFollowingItems(ctx: Context, successCb: (FollowingCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        //makeRequest(ctx, USER + "/feed", "emU6MTIzNA==", UserActionCollection::class.java, successCb, errorCb)
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun <T> makeRequest(
            ctx: Context,
            url: String,
            userToken: String,
            klass: Class<T>,
            successCb: (T) -> Unit,
            errorCb: (VolleyError) -> Unit
    ): String {
        val tag = UUID.randomUUID().toString()
        val req = HttpRequest(
                url,
                userToken,
                klass,
                { result -> successCb(result) },
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
        return tag
    }

    private fun downloadFileRequest(
            url: String,
            activity: MainActivity
    ) {
        activity.url = url
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission status", "You have permission")
            DownloadAsyncTask().execute(DownloadFileContainer(url, activity))
        } else {
            Log.e("Permission status", "You have asked for permission")
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

    }
}