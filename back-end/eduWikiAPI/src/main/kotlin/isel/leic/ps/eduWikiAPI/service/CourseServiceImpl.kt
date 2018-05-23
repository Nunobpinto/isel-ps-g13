package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.repository.ConnectionWrapperImpl
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl
import isel.leic.ps.eduWikiAPI.repository.interfaces.ConnectionWrapper
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var courseDAO: CourseDAO

    override fun getAllCourses() : List<Course> = courseDAO.getAllCourses()

    override fun getSpecificCourse(courseId: Int) = courseDAO.getCourse(courseId)

    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport> = courseDAO.getAllReportsOnCourse(courseId)

}