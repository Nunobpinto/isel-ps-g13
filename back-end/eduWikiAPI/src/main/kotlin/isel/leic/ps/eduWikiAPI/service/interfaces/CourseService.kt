package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport

interface CourseService {

    fun getAllCourses() : List<Course>

    fun getSpecificCourse(courseId: Int) : Course

    fun getAllReportsOnCourse(courseId: Int) : List<CourseReport>

}