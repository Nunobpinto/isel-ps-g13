package isel.ps.eduwikimobile.repos.remote

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.comms.GetRequest
import isel.ps.eduwikimobile.comms.HeadRequest
import isel.ps.eduwikimobile.comms.Session
import isel.ps.eduwikimobile.domain.single.User
import java.util.*
import isel.ps.eduwikimobile.ui.activities.MainActivity
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.domain.paramsContainer.LoginParametersContainer
import isel.ps.eduwikimobile.domain.paramsContainer.ParametersContainer
import isel.ps.eduwikimobile.domain.paramsContainer.ResourceParametersContainer
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse
import java.util.concurrent.TimeoutException
import kotlin.collections.ArrayList

class EduWikiRepository : IEduWikiRepository {

    var tagList: ArrayList<String> = arrayListOf()

    override fun <T> getEntity(uri: String, klass: Class<T>, params: ParametersContainer<T>) {
        if (!isConnected(params.app)) {
            return params.errorCb(
                    ServerErrorResponse(
                            title = "No connectiviy",
                            detail = "There is no connectivity, try again later!"
                    )
            )
        }
        makeGetRequest(
                params.app,
                uri,
                Session().getAuthToken(params.app),
                klass,
                params.successCb,
                { error: ServerErrorResponse -> params.errorCb(error) }
        )
    }

    override fun getUser(uri: String, params: LoginParametersContainer) {
        if (!isConnected(params.app)) {
            return params.errorCb(
                    ServerErrorResponse(
                            title = "No connectiviy",
                            detail = "There is no connectivity, try again later!"
                    )
            )
        }
        val token = Session().encodeToString(params.username, params.password)
        makeGetRequest(
                params.app,
                uri,
                token,
                User::class.java,
                params.successCb,
                { error: ServerErrorResponse -> params.errorCb(error) }
        )
    }

    override fun getResourceFile(uri: String, params: ResourceParametersContainer) {
        if (!isConnected(params.app)) {
            return params.errorCb(
                    ServerErrorResponse(
                            title = "No connectiviy",
                            detail = "There is no connectivity, try again later!"
                    ))
        }
        makeHeadRequest(
                params.app,
                uri,
                { map ->
                    checkPermissions(map["Content-Disposition"]!!, params.activity, uri, params.app)
                    params.successCb(Unit)
                },
                { error: ServerErrorResponse -> params.errorCb(error) }
        )
    }

    override fun cancelPendingRequests(app: EduWikiApplication) {
        tagList.forEach {
            app.requestQueue.cancelAll(it)
        }
        tagList.clear()
    }

//    private fun handlerException(error: ServerErrorResponse): ServerErrorResponse {
//        if (error.exception is TimeoutException)
//            return ServerErrorResponse(
//                    "Server isn't responding..."
//
//            )
//        return AppException(error.message ?: "An error ocurred! Try again later!")
//    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun <T> makeGetRequest(
            ctx: Context,
            url: String,
            authToken: String,
            klass: Class<T>,
            successCb: (T) -> Unit,
            errorCb: (ServerErrorResponse) -> Unit
    ): String {
        val tag = UUID.randomUUID().toString()
        tagList.add(tag)
        val req = GetRequest(
                url,
                authToken,
                klass,
                { result -> successCb(result) },
                errorCb as (VolleyError) -> Unit
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
        return tag
    }

    private fun makeHeadRequest(
            ctx: Context,
            url: String,
            successCb: (Map<String, String>) -> Unit,
            errorCb: (ServerErrorResponse) -> Unit
    ): String {
        val tag = UUID.randomUUID().toString()
        tagList.add(tag)
        val req = HeadRequest(
                url,
                successCb,
                errorCb as (VolleyError) -> Unit
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
        return tag
    }

    private fun checkPermissions(
            contentDisposition: String,
            activity: MainActivity,
            uri: String,
            app: EduWikiApplication
    ) {
        activity.url = uri
        activity.header = contentDisposition
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission status", "You have permission")
            downloadFile(contentDisposition, uri, app)
        } else {
            Log.e("Permission status", "You have asked for permission")
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    override fun downloadFile(header: String, uri: String, app: EduWikiApplication) {
        val contentSplit = header.split("filename=")
        val filename = contentSplit[1].replace("filename=", "").replace("\"", "").trim()
        val request = DownloadManager.Request(Uri.parse(uri))
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        val manager = app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

}