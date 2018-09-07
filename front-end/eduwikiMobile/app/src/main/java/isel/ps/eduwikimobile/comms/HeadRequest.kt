package isel.ps.eduwikimobile.comms

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.ps.eduwikimobile.exceptions.ServerErrorResponse
import java.io.IOException

data class HeadRequest(
        val urlPath: String,
        val successListner: (Map<String,String>) -> Unit,
        val errorListener: (VolleyError) -> Unit
) : Request<Map<String,String>>(Request.Method.HEAD, urlPath, errorListener) {
    private val mapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    override fun parseNetworkResponse(response: NetworkResponse): Response<Map<String, String>> {
        try {
            return Response.success(response.headers!!, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: IOException) {
            return Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: Map<String, String>) {
       successListner(response)
    }

    override fun deliverError(error: VolleyError) {
        val jsonError = mapper.readValue(String(error.networkResponse.data), ServerErrorResponse::class.java)
        jsonError.exception = error
        errorListener(jsonError)
    }

}