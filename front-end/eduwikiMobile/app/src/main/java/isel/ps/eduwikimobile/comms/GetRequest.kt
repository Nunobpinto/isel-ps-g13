package isel.ps.eduwikimobile.comms

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse
import java.io.IOException
import java.nio.charset.Charset

data class GetRequest<T>(
        val urlPath: String,
        private val authToken: String,
        private val klass: Class<T>,
        val successListener: (T) -> Unit,
        val errorListener: (VolleyError) -> Unit
) : JsonRequest<T>(Request.Method.GET, urlPath, "", successListener, errorListener) {
    private val mapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun parseNetworkResponse(response: NetworkResponse): Response<T> {
        try {
            val json = String(
                    response.data,
                    Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            return Response.success(mapper.readValue(json, klass), HttpHeaderParser.parseCacheHeaders(response))

        } catch (e: IOException) {
            return Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: T) {
        successListener(response)
    }

    override fun deliverError(error: VolleyError) {
        val jsonError =
                if (error.networkResponse != null)
                    mapper.readValue(String(error.networkResponse.data), ServerErrorResponse::class.java)
                else ServerErrorResponse()
        jsonError.exception = error
        errorListener(jsonError)
    }

    override fun getHeaders(): MutableMap<String, String> {
        val params = HashMap<String, String>()
        params.put("Access-Control-Allow-Origin", "*")
        val auth = "Basic $authToken"
        params.put("tenant-uuid", "61765bb5-9135-4ed4-85ec-be179680f29e")
        params.put("Authorization", auth)
        return params
    }
}