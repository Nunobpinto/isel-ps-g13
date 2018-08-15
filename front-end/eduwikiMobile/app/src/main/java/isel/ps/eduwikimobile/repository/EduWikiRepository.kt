package isel.ps.eduwikimobile.repository

import android.content.Context
import android.net.ConnectivityManager
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.API_URL
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.comms.HttpRequest
import isel.ps.eduwikimobile.domain.model.collection.ClassCollection
import isel.ps.eduwikimobile.domain.model.collection.CourseCollection
import isel.ps.eduwikimobile.domain.model.collection.CourseProgrammeCollection
import isel.ps.eduwikimobile.domain.model.collection.ProgrammeCollection
import isel.ps.eduwikimobile.domain.model.single.Organization
import java.util.*

class EduWikiRepository() : IEduWikiRepository {

    companion object {
        val ALL_PROGRAMMES_URL = API_URL + "/programmes"
        val ALL_COURSES_URL = API_URL + "/courses"
        val ALL_CLASSES_URL = API_URL + "/classes"
        val ORGANIZATIONS = API_URL + "/organizations"
    }

    override fun getAllProgrammes(ctx: Context, successCb: (ProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if(!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        val tag = UUID.randomUUID().toString() //TODO see usage
        val req = HttpRequest(
                ALL_PROGRAMMES_URL,
                "emU6MTIzNA==",
                ProgrammeCollection::class.java,
                { programmes -> successCb(programmes)},
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
    }

    override fun getAllCourses(ctx: Context, successCb: (CourseCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if(!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        val tag = UUID.randomUUID().toString() //TODO see usage
        val req = HttpRequest(
                ALL_COURSES_URL,
                "emU6MTIzNA==",
                CourseCollection::class.java,
                { courses -> successCb(courses)},
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
    }

    override fun getCoursesOfSpeficiProgramme(programmeId: Int, ctx: Context, successCb: (CourseProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if(!isConnected(ctx)) {
            return errorCb(VolleyError())
        }

        val tag = UUID.randomUUID().toString() //TODO see usage
        val req = HttpRequest(
                ALL_PROGRAMMES_URL + "/" + programmeId + "/courses",
                "emU6MTIzNA==",
                CourseProgrammeCollection::class.java,
                { courses -> successCb(courses)},
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
    }

    override fun getAllClasses(ctx: Context, successCb: (ClassCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if(!isConnected(ctx)) {
            return errorCb(VolleyError())
        }

        val tag = UUID.randomUUID().toString() //TODO see usage
        val req = HttpRequest(
                ALL_CLASSES_URL,
                "emU6MTIzNA==",
                ClassCollection::class.java,
                { classes -> successCb(classes)},
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
    }

    override fun getOrganization(ctx: Context, successCb: (Organization) -> Unit, errorCb: (VolleyError) -> Unit) {
        if(!isConnected(ctx)) {
            return errorCb(VolleyError())
        }

        val tag = UUID.randomUUID().toString() //TODO see usage
        val req = HttpRequest(
                ORGANIZATIONS + "/1",
                "emU6MTIzNA==",
                Organization::class.java,
                { organization  -> successCb(organization)},
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}