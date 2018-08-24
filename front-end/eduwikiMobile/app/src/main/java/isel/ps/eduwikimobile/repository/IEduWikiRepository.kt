package isel.ps.eduwikimobile.repository

import android.content.Context
import com.android.volley.VolleyError
import isel.ps.eduwikimobile.domain.model.collection.*
import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.paramsContainer.ResourceParametersContainer

interface IEduWikiRepository {
    fun getAllProgrammes(ctx: Context, successCb: (ProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllCourses(ctx: Context, successCb: (CourseCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getCoursesOfSpeficiProgramme(programmeId: Int, ctx: Context, successCb: (CourseProgrammeCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllClasses(ctx: Context, successCb: (ClassCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getOrganization(ctx: Context, successCb: (Organization) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getWorkAssignmentsOfSpecificCourse(termId: Int, courseId: Int, ctx: Context, successCb: (WorkAssignmentCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getExamsOfSpecificCourse(termId: Int, courseId: Int, ctx: Context, successCb: (ExamCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getClassesOfSpecificCourse(termId: Int, courseId: Int, ctx: Context, successCb: (ClassCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getTermsOfCourse(courseId: Int, ctx: Context, successCb: (TermCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllCoursesOfSpecificClass(classId: Int, ctx: Context, successCb: (CourseClassCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllLecturesOfCourseClass(courseId: Int, classId: Int,  ctx: Context, successCb: (LectureCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getAllHomeworksOfCourseClass(courseId: Int, classId: Int,  ctx: Context, successCb: (HomeworkCollection) -> Unit, errorCb: (VolleyError) -> Unit)
    fun getSpecificResource(params: ResourceParametersContainer)
}