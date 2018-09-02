package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.HomeworkDAO
import org.junit.Test

import junit.framework.TestCase.*
import org.apache.tomcat.jni.Local
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class HomeworkDAOImplTest {

    @Autowired
    lateinit var homeworkDAO: HomeworkDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllHomeworksFromCourseInClass() {
        val homeworks = homeworkDAO.getAllHomeworksFromCourseInClass(1)
        assertEquals(2, homeworks.size)
    }

    @Test
    fun getSpecificHomeworkFromSpecificCourseInClass() {
        val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(1, 5).get()
        assertEquals(5, homework.homeworkId)
        assertEquals(1, homework.classId)
        assertEquals(1, homework.courseId)
        assertEquals("TPC01", homework.homeworkName)
        assertEquals(2, homework.version)
        assertEquals(1, homework.logId)
        assertEquals(0, homework.votes)
        assertEquals("bruno", homework.createdBy)
        assertEquals(LocalDate.parse("2018-09-02"), homework.dueDate)
        assertEquals(true, homework.lateDelivery)
        assertEquals(true, homework.multipleDeliveries)
    }

    @Test
    fun createHomeworkOnCourseInClass() {
        val homework = homeworkDAO.createHomeworkOnCourseInClass(
                1,
                Homework(
                        homeworkId = 8,
                        classId = 1,
                        courseId = 1,
                        homeworkName = "TPC10",
                        votes = 74,
                        createdBy = "arminda",
                        dueDate = LocalDate.parse("2014-11-15"),
                        lateDelivery = false,
                        multipleDeliveries = false
                )
        )
        assertEquals(8, homework.homeworkId)
        assertEquals("TPC10", homework.homeworkName)
        assertEquals(1, homework.version)
        assertEquals(4, homework.logId)
        assertEquals(74, homework.votes)
        assertEquals("arminda", homework.createdBy)
        assertEquals(LocalDate.parse("2014-11-15"), homework.dueDate)
        assertEquals(false, homework.lateDelivery)
        assertEquals(false, homework.multipleDeliveries)
    }

    @Test
    fun updateHomework() {
        val oldHomework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(1, 5).get()
        assertEquals(2, oldHomework.version)
        val homework = homeworkDAO.updateHomework(
                Homework(
                        homeworkId = 5,
                        classId = 1,
                        courseId = 1,
                        homeworkName = "TPC04",
                        version = oldHomework.version.inc(),
                        votes = -14,
                        createdBy = "allison",
                        dueDate = LocalDate.parse("2016-05-14"),
                        lateDelivery = false,
                        multipleDeliveries = true
                )
        )
        assertEquals(5, homework.homeworkId)
        assertEquals("TPC04", homework.homeworkName)
        assertEquals(3, homework.version)
        assertEquals(1, homework.logId)
        assertEquals(-14, homework.votes)
        assertEquals("allison", homework.createdBy)
        assertEquals(LocalDate.parse("2016-05-14"), homework.dueDate)
        assertEquals(false, homework.lateDelivery)
        assertEquals(true, homework.multipleDeliveries)
    }

    @Test
    fun updateVotesOnHomework() {
        val oldHomework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(1, 5).get()
        assertEquals(0, oldHomework.votes)
        val rowsAffected = homeworkDAO.updateVotesOnHomework(5, 140)
        assertEquals(1, rowsAffected)
        val updatedHomework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(1, 5).get()
        assertEquals(140, updatedHomework.votes)
    }

    @Test
    fun deleteSpecificHomeworkOfCourseInClass() {
        val rowsAffected = homeworkDAO.deleteSpecificHomeworkOfCourseInClass(1, 5)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllReportsOfHomeworkFromCourseInClass() {
        val reports = homeworkDAO.getAllReportsOfHomeworkFromCourseInClass(1, 5)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificReportOfHomeworkFromCourseInClass() {
        val reportedHomework = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(1, 5, 1).get()
        assertEquals(1, reportedHomework.reportId)
        assertEquals(5, reportedHomework.homeworkId)
        assertEquals(1, reportedHomework.classId)
        assertEquals(1, reportedHomework.courseId)
        assertEquals(1, reportedHomework.logId)
        assertEquals("TPC06", reportedHomework.homeworkName)
        assertEquals(false, reportedHomework.lateDelivery)
        assertEquals(true, reportedHomework.multipleDeliveries)
        assertEquals("vitor", reportedHomework.reportedBy)
        assertEquals(0, reportedHomework.votes)
    }

    @Test
    fun createReportOnHomework() {
        val homework = homeworkDAO.createReportOnHomework(
                HomeworkReport(
                        homeworkId = 5,
                        classId = 1,
                        courseId = 1,
                        homeworkName = "TPC99",
                        dueDate = LocalDate.parse("2011-11-12"),
                        lateDelivery = false,
                        multipleDeliveries = false,
                        reportedBy = "guilherme",
                        votes = 44
                )
        )
        assertEquals(5, homework.homeworkId)
        assertEquals(3, homework.reportId)
        assertEquals(3, homework.logId)
        assertEquals("TPC99", homework.homeworkName)
        assertEquals(LocalDate.parse("2011-11-12"), homework.dueDate)
        assertEquals(false, homework.lateDelivery)
        assertEquals(false, homework.multipleDeliveries)
        assertEquals("guilherme", homework.reportedBy)
        assertEquals(44, homework.votes)
    }

    @Test
    fun updateVotesOnReportedHomework() {
        val oldReportedHomework = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(1, 5, 1).get()
        assertEquals(0, oldReportedHomework.votes)
        val rowsAffected = homeworkDAO.updateVotesOnReportedHomework(5, 1, -12)
        assertEquals(1, rowsAffected)
        val updatedReportedHomework = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(1, 5, 1).get()
        assertEquals(-12, updatedReportedHomework.votes)
    }

    @Test
    fun deleteSpecificReportOnHomeworkOfCourseInClass() {
        val rowsAffected = homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(1, 5, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllStagedHomeworksOfCourseInClass() {
        val stagedHomeworks = homeworkDAO.getAllStagedHomeworksOfCourseInClass(3)
        assertEquals(2, stagedHomeworks.size)
    }

    @Test
    fun getSpecificStagedHomeworkOfCourseInClass() {
        val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(3, 1).get()
        assertEquals("TPC14", stagedHomework.homeworkName)
        assertEquals(1, stagedHomework.stageId)
        assertEquals(2, stagedHomework.classId)
        assertEquals(3, stagedHomework.courseId)
        assertEquals(1, stagedHomework.logId)
        assertEquals(LocalDate.parse("2014-03-24"), stagedHomework.dueDate)
        assertEquals(true, stagedHomework.lateDelivery)
        assertEquals(true, stagedHomework.multipleDeliveries)
        assertEquals("francisco", stagedHomework.createdBy)
        assertEquals(0, stagedHomework.votes)
    }

    @Test
    fun createStagingHomeworkOnCourseInClass() {
        val stagingHomework = homeworkDAO.createStagingHomeworkOnCourseInClass(
                3,
                HomeworkStage(
                        stageId = 3,
                        classId = 2,
                        courseId = 3,
                        homeworkName = "TPC05",
                        dueDate = LocalDate.parse("2004-06-27"),
                        lateDelivery = true,
                        multipleDeliveries = true,
                        createdBy = "adelino",
                        votes = -18
                )
        )
        assertEquals(3, stagingHomework.stageId)
        assertEquals(3, stagingHomework.logId)
        assertEquals("TPC05", stagingHomework.homeworkName)
        assertEquals(LocalDate.parse("2004-06-27"), stagingHomework.dueDate)
        assertEquals(true, stagingHomework.lateDelivery)
        assertEquals(true, stagingHomework.multipleDeliveries)
        assertEquals("adelino", stagingHomework.createdBy)
        assertEquals(-18, stagingHomework.votes)
    }

    @Test
    fun updateVotesOnStagedHomework() {
        val oldStagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(3, 1).get()
        assertEquals(0, oldStagedHomework.votes)
        val rowsAffected = homeworkDAO.updateVotesOnStagedHomework(1, 157)
        assertEquals(1, rowsAffected)
        val updatedStagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(3, 1).get()
        assertEquals(157, updatedStagedHomework.votes)
    }

    @Test
    fun deleteSpecificStagedHomeworkOfCourseInClass() {
        val rowsAffected = homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(3, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllVersionsOfHomeworkOfCourseInclass() {
        val versions = homeworkDAO.getAllVersionsOfHomeworkOfCourseInclass(1, 5)
        assertEquals(2, versions.size)
    }

    @Test
    fun getSpecificVersionOfHomeworkOfCourseInClass() {
        val homeworkVersion = homeworkDAO.getSpecificVersionOfHomeworkOfCourseInClass(1, 5, 1).get()
        assertEquals(5, homeworkVersion.homeworkId)
        assertEquals(1, homeworkVersion.version)
        assertEquals("TPC03", homeworkVersion.homeworkName)
        assertEquals(LocalDate.parse("2018-09-02"), homeworkVersion.dueDate)
        assertEquals("joao", homeworkVersion.createdBy)
        assertEquals(false, homeworkVersion.lateDelivery)
        assertEquals(true, homeworkVersion.multipleDeliveries)
    }

    @Test
    fun createHomeworkVersion() {
        val homeworkVersion = homeworkDAO.createHomeworkVersion(
                HomeworkVersion(
                        version = 3,
                        homeworkId = 5,
                        homeworkName = "TPC97",
                        dueDate = LocalDate.parse("2008-03-21"),
                        createdBy = "gabriel",
                        lateDelivery = false,
                        multipleDeliveries = true
                )
        )
        assertEquals(5, homeworkVersion.homeworkId)
        assertEquals(3, homeworkVersion.version)
        assertEquals("TPC97", homeworkVersion.homeworkName)
        assertEquals(LocalDate.parse("2008-03-21"), homeworkVersion.dueDate)
        assertEquals("gabriel", homeworkVersion.createdBy)
        assertEquals(false, homeworkVersion.lateDelivery)
        assertEquals(true, homeworkVersion.multipleDeliveries)
    }

    @Test
    fun getHomeworkByLogId() {
        val homework = homeworkDAO.getHomeworkByLogId(1).get()
        assertEquals(5, homework.homeworkId)
        assertEquals(1, homework.classId)
        assertEquals(1, homework.courseId)
        assertEquals("TPC01", homework.homeworkName)
        assertEquals(2, homework.version)
        assertEquals(1, homework.logId)
        assertEquals(0, homework.votes)
        assertEquals("bruno", homework.createdBy)
        assertEquals(LocalDate.parse("2018-09-02"), homework.dueDate)
        assertEquals(true, homework.lateDelivery)
        assertEquals(true, homework.multipleDeliveries)
    }

    @Test
    fun getHomeworkReportByLogId() {
        val reportedHomework = homeworkDAO.getHomeworkReportByLogId(1).get()
        assertEquals(1, reportedHomework.reportId)
        assertEquals(5, reportedHomework.homeworkId)
        assertEquals(1, reportedHomework.classId)
        assertEquals(1, reportedHomework.courseId)
        assertEquals(1, reportedHomework.logId)
        assertEquals("TPC06", reportedHomework.homeworkName)
        assertEquals(false, reportedHomework.lateDelivery)
        assertEquals(true, reportedHomework.multipleDeliveries)
        assertEquals("vitor", reportedHomework.reportedBy)
        assertEquals(0, reportedHomework.votes)
    }

    @Test
    fun getHomeworkStageByLogId() {
        val stagedHomework = homeworkDAO.getHomeworkStageByLogId(1).get()
        assertEquals("TPC14", stagedHomework.homeworkName)
        assertEquals(1, stagedHomework.stageId)
        assertEquals(2, stagedHomework.classId)
        assertEquals(3, stagedHomework.courseId)
        assertEquals(1, stagedHomework.logId)
        assertEquals(LocalDate.parse("2014-03-24"), stagedHomework.dueDate)
        assertEquals(true, stagedHomework.lateDelivery)
        assertEquals(true, stagedHomework.multipleDeliveries)
        assertEquals("francisco", stagedHomework.createdBy)
        assertEquals(0, stagedHomework.votes)

    }
}