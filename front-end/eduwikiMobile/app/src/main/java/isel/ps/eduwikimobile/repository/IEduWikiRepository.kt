package isel.ps.eduwikimobile.repository

import android.content.Context
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.domain.model.collection.ClassCollection
import isel.ps.eduwikimobile.domain.model.collection.CourseCollection
import isel.ps.eduwikimobile.domain.model.collection.CourseProgrammeCollection
import isel.ps.eduwikimobile.domain.model.collection.ProgrammeCollection
import isel.ps.eduwikimobile.domain.model.single.Organization

interface IEduWikiRepository {
    fun getAllProgrammes(ctx: Context, successCb: (ProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllCourses(ctx: Context, successCb: (CourseCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getCoursesOfSpeficiProgramme(programmeId: Int, ctx: Context, successCb: (CourseProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllClasses(ctx: Context, successCb: (ClassCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getOrganization(ctx: Context, successCb: (Organization) -> Unit, errorCb: (VolleyError) -> Unit)
}