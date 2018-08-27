package isel.ps.eduwikimobile.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.comms.DownloadAsyncTask
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.comms.HttpRequest
import isel.ps.eduwikimobile.comms.Session
import isel.ps.eduwikimobile.domain.model.single.User
import java.util.*
import isel.ps.eduwikimobile.ui.activities.MainActivity
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.paramsContainer.DownloadFileContainer
import isel.ps.eduwikimobile.paramsContainer.LoginParametersContainer
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
                Session().getAuthToken(params.app),
                klass,
                params.successCb,
                { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
        )
    }

    override fun getUser(uri: String, params: LoginParametersContainer) {
        if (!isConnected(params.app)) {
            return params.errorCb(AppException("There is no connection"))
        }
        val token = Session().encodeToString(params.username, params.password)
        makeRequest(
                params.app,
                uri,
                token,
                User::class.java,
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

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun <T> makeRequest(
            ctx: Context,
            url: String,
            authToken: String,
            klass: Class<T>,
            successCb: (T) -> Unit,
            errorCb: (VolleyError) -> Unit
    ): String {
        val tag = UUID.randomUUID().toString()
        val req = HttpRequest(
                url,
                authToken,
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