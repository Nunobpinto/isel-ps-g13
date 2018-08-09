package isel.ps.eduwikimobile.repository

import android.content.Context
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.domain.model.Programme
import isel.ps.eduwikimobile.domain.model.ProgrammeCollection

interface IEduWikiRepository {
    fun getAllProgrammes(ctx: Context, successCb: (Programme) -> Unit, errorCb: (VolleyError) -> Unit)
}