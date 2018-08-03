package isel.leic.ps.g13.eduwikimobile.comms

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.IOException
import java.nio.charset.Charset
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



data class HttpRequest<T>(
        private val urlPath: String,
        private val authToken: String,
        private val klass: Class<T>,
        private val successListener: (T) -> Unit,
        private val errorListener: (VolleyError) -> Unit
) : JsonRequest<T>(Request.Method.GET, urlPath, "", successListener, errorListener) {
    private val mapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                    response?.data!!,
                    Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            Response.success(mapper.readValue(json, klass), HttpHeaderParser.parseCacheHeaders(response))

        } catch (e: IOException) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: T) {
        successListener(response)
    }

    override fun deliverError(error: VolleyError?) {
        errorListener(error!!)
    }

    override fun getHeaders(): MutableMap<String, String> {
        val params = HashMap<String, String>()
        params.put("Access-Control-Allow-Origin", "*")
        val auth = "Basic $authToken"
        params.put("Authorization", auth)
        return params
    }
}