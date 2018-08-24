package isel.ps.eduwikimobile.repository

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.util.Log
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.API_URL
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.comms.HttpRequest
import isel.ps.eduwikimobile.domain.model.collection.*
import isel.ps.eduwikimobile.domain.model.single.Organization
import java.util.*
import isel.ps.eduwikimobile.ui.activities.MainActivity
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.paramsContainer.ResourceParametersContainer


class EduWikiRepository : IEduWikiRepository {

    companion object {
        val ALL_PROGRAMMES_URL = API_URL + "/programmes"
        val ALL_COURSES_URL = API_URL + "/courses"
        val ALL_CLASSES_URL = API_URL + "/classes"
        val ORGANIZATION = API_URL + "/organization"
        val RESOURCES = API_URL + "/resources"
    }

    override fun getAllProgrammes(ctx: Context, successCb: (ProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_PROGRAMMES_URL, "emU6MTIzNA==", ProgrammeCollection::class.java, successCb, errorCb)
    }

    override fun getAllCourses(ctx: Context, successCb: (CourseCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_COURSES_URL, "emU6MTIzNA==", CourseCollection::class.java, successCb, errorCb)
    }

    override fun getCoursesOfSpeficiProgramme(programmeId: Int, ctx: Context, successCb: (CourseProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_PROGRAMMES_URL + "/" + programmeId + "/courses", "emU6MTIzNA==", CourseProgrammeCollection::class.java, successCb, errorCb)
    }

    override fun getAllClasses(ctx: Context, successCb: (ClassCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_CLASSES_URL, "emU6MTIzNA==", ClassCollection::class.java, successCb, errorCb)
    }

    override fun getOrganization(ctx: Context, successCb: (Organization) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ORGANIZATION, "emU6MTIzNA==", Organization::class.java, successCb, errorCb)
    }

    override fun getWorkAssignmentsOfSpecificCourse(termId: Int, courseId: Int, ctx: Context, successCb: (WorkAssignmentCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_COURSES_URL + "/" + courseId + "/terms/" + termId + "/work-assignments", "emU6MTIzNA==", WorkAssignmentCollection::class.java, successCb, errorCb)
    }

    override fun getExamsOfSpecificCourse(termId: Int, courseId: Int, ctx: Context, successCb: (ExamCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_COURSES_URL + "/" + courseId + "/terms/" + termId + "/exams", "emU6MTIzNA==", ExamCollection::class.java, successCb, errorCb)
    }

    override fun getClassesOfSpecificCourse(termId: Int, courseId: Int, ctx: Context, successCb: (ClassCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_COURSES_URL + "/" + courseId + "/terms/" + termId + "/classes", "emU6MTIzNA==", ClassCollection::class.java, successCb, errorCb)
    }

    override fun getTermsOfCourse(courseId: Int, ctx: Context, successCb: (TermCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_COURSES_URL + "/" + courseId + "/terms", "emU6MTIzNA==", TermCollection::class.java, successCb, errorCb)
    }

    override fun getAllCoursesOfSpecificClass(classId: Int, ctx: Context, successCb: (CourseClassCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_CLASSES_URL + "/" + classId + "/courses", "emU6MTIzNA==", CourseClassCollection::class.java, successCb, errorCb)
    }

    override fun getAllLecturesOfCourseClass(courseId: Int, classId: Int, ctx: Context, successCb: (LectureCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_CLASSES_URL + "/" + classId + "/courses/" + courseId + "/lectures", "emU6MTIzNA==", LectureCollection::class.java, successCb, errorCb)
    }

    override fun getAllHomeworksOfCourseClass(courseId: Int, classId: Int, ctx: Context, successCb: (HomeworkCollection) -> Unit, errorCb: (VolleyError) -> Unit) {
        if (!isConnected(ctx)) {
            return errorCb(VolleyError())
        }
        makeRequest(ctx, ALL_CLASSES_URL + "/" + classId + "/courses/" + courseId + "/homeworks", "emU6MTIzNA==", HomeworkCollection::class.java, successCb, errorCb)
    }

    override fun getSpecificResource(params: ResourceParametersContainer) {
        if (!isConnected(params.app)) {
            return params.errorCb(AppException("No connection detected"))
        }
        downloadFileRequest(params.activity, params.app, RESOURCES + "/" + params.resourceId, params.successCb, params.errorCb)
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun <T> makeRequest(
            ctx: Context,
            url: String,
            userToken: String,
            klass: Class<T>,
            successCb: (T) -> Unit,
            errorCb: (VolleyError) -> Unit
    ): String {
        val tag = UUID.randomUUID().toString() //TODO see usage
        val req = HttpRequest(
                url,
                userToken,
                klass,
                { result -> successCb(result) },
                errorCb
        )
        req.tag = tag
        (ctx as EduWikiApplication).requestQueue.add(req)
        return tag
    }

    private fun downloadFileRequest(
            activity: MainActivity,
            ctx: Context,
            url: String,
            successCb: () -> Unit,
            errorCb: (AppException) -> Unit
    ) {
        activity.url = url
        if (checkSelfPermission(ctx,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission error","You have permission")
        }
        else {
            Log.e("Permission error","You have asked for permission");
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);
        }

        DownloadManager.ACTION_DOWNLOAD_COMPLETE
    }
}