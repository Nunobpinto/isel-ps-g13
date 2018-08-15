package isel.ps.eduwikimobile.repository

import android.content.Context
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.domain.model.collection.CourseCollection
import isel.ps.eduwikimobile.domain.model.collection.ProgrammeCollection

interface IEduWikiRepository {
    fun getAllProgrammes(ctx: Context, successCb: (ProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllCourses(ctx: Context, successCb: (CourseCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getCoursesOfSpeficiProgramme(programmeId: Int, ctx: Context, successCb: (CourseCollection) -> Unit, errorCb: (VolleyError) -> Unit)
}