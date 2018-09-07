package isel.ps.eduwikimobile.exceptions

import com.android.volley.VolleyError

data class ServerErrorResponse (
        val title : String = "",
        val detail : String = "",
        val status : Int = 0,
        val type: String = "",
        var exception: VolleyError = VolleyError()
) : VolleyError()