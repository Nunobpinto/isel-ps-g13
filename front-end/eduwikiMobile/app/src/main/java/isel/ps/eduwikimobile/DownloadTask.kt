package isel.ps.eduwikimobile

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.widget.Toast
import isel.ps.eduwikimobile.paramsContainer.DownloadFileContainer
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DownloadTask(
        val container: DownloadFileContainer
) : AsyncTask<DownloadFileContainer, Int, String>() {

    override fun doInBackground(vararg objs: DownloadFileContainer): String {
        val urlRequest: URL
        var filename: String = ""
        try {
            urlRequest = URL(objs[0].url)
            val con = urlRequest.openConnection() as HttpURLConnection
            con.setInstanceFollowRedirects(false)
            con.connect()
            val content = con.getHeaderField("Content-Disposition");
            val contentSplit = content.split("filename=");
            filename = contentSplit[1].replace("filename=", "").replace("\"", "").trim()
            val request = DownloadManager.Request(Uri.parse(objs[0].url))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setVisibleInDownloadsUi(true)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
            val manager = objs[0].ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        } catch (e: IOException) {

        }
        return filename
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: String) {
        Toast.makeText(container.ctx, "File Downloaded", Toast.LENGTH_LONG).show()
    }
}