package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
import org.junit.Test

import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.support.beans
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class LectureDAOImplTest {

    @Autowired
    lateinit var lectureDAO: LectureDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllLecturesFromCourseInClass() {
        val lectures = lectureDAO.getAllLecturesFromCourseInClass(1)
        assertEquals(2, lectures.size)
    }

    @Test
    fun getSpecificLectureFromCourseInClass() {
        val lecture = lectureDAO.getSpecificLectureFromCourseInClass(1, 1).get()
        assertEquals(1, lecture.classId)
        assertEquals(1, lecture.lectureId)
        assertEquals(1, lecture.courseId)
        assertEquals(2, lecture.version)
        assertEquals(0, lecture.votes)
        assertEquals("bruno", lecture.createdBy)
        assertEquals(DayOfWeek.MONDAY, lecture.weekDay)
        assertEquals(LocalTime.parse("12:00:00"), lecture.begins)
        assertEquals(Duration.parse("PT2H"), lecture.duration)
        assertEquals("E.1.07", lecture.location)
        assertEquals(1, lecture.logId)
    }

    @Test
    fun createLectureOnCourseInClass() {
        val lecture = lectureDAO.createLectureOnCourseInClass(
                1,
                Lecture(
                        lectureId = 9,
                        classId = 1,
                        courseId = 1,
                        createdBy = "jg",
                        weekDay = DayOfWeek.THURSDAY,
                        begins = LocalTime.parse("09:00:00"),
                        duration = Duration.parse("PT1H30M"),
                        location = "LS1",
                        votes = 36
                )
        )
        assertEquals(8, lecture.lectureId)
        assertEquals("LS1", lecture.location)
        assertEquals(1, lecture.version)
        assertEquals(5, lecture.logId)
        assertEquals(36, lecture.votes)
        assertEquals("jg", lecture.createdBy)
        assertEquals("THURSDAY", lecture.weekDay.name)
        assertEquals(LocalTime.parse("09:00:00"), lecture.begins)
        assertEquals(Duration.parse("PT1H30M"), lecture.duration)
    }

    @Test
    fun updateLecture() {
        val oldLecture = lectureDAO.getSpecificLectureFromCourseInClass(1, 1).get()
        assertEquals(2, oldLecture.version)
        val lecture = lectureDAO.updateLecture(
                Lecture(
                        lectureId = 1,
                        classId = 1,
                        courseId = 1,
                        duration = Duration.parse("PT2H"),
                        version = oldLecture.version.inc(),
                        votes = 98,
                        createdBy = "ze",
                        location = "C.0.12",
                        begins = LocalTime.parse("18:30:00"),
                        weekDay = DayOfWeek.MONDAY
                )
        )
        assertEquals(1, lecture.lectureId)
        assertEquals(1, lecture.logId)
        assertEquals(98, lecture.votes)
        assertEquals("ze", lecture.createdBy)
        assertEquals(Duration.parse("PT2H"), lecture.duration)
        assertEquals(3, lecture.version)
        assertEquals("C.0.12", lecture.location)
        assertEquals(LocalTime.parse("18:30:00"), lecture.begins)
        assertEquals(DayOfWeek.MONDAY, lecture.weekDay)
    }

    @Test
    fun updateVotesOnLecture() {
        val oldLecture = lectureDAO.getSpecificLectureFromCourseInClass(1, 1).get()
        assertEquals(0, oldLecture.votes)
        val rowsAffected = lectureDAO.updateVotesOnLecture(1, 257)
        assertEquals(1, rowsAffected)
        val updatedLecture = lectureDAO.getSpecificLectureFromCourseInClass(1, 1).get()
        assertEquals(257, updatedLecture.votes)
    }

    @Test
    fun deleteSpecificLectureOfCourseInClass() {
        val rowsAffected = lectureDAO.deleteSpecificLectureOfCourseInClass(1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllStagedLecturesOfCourseInClass() {
        val stages = lectureDAO.getAllStagedLecturesOfCourseInClass(1)
        assertEquals(2, stages.size)
    }

    @Test
    fun getSpecificStagedLectureOfCourseInClass() {
        val stagedLecture = lectureDAO.getSpecificStagedLectureOfCourseInClass(1, 1).get()
        assertEquals(LocalTime.parse("11:30:00"), stagedLecture.begins)
        assertEquals(1, stagedLecture.stageId)
        assertEquals(1, stagedLecture.classId)
        assertEquals(1, stagedLecture.courseId)
        assertEquals(1, stagedLecture.logId)
        assertEquals(DayOfWeek.THURSDAY, stagedLecture.weekDay)
        assertEquals(Duration.parse("PT2H"), stagedLecture.duration)
        assertEquals("nuno", stagedLecture.createdBy)
        assertEquals(0, stagedLecture.votes)
        assertEquals("G.0.10", stagedLecture.location)
    }

    @Test
    fun createStagingLectureOnCourseInClass() {
        val stagingHomeworkLecture = lectureDAO.createStagingLectureOnCourseInClass(
                1,
                LectureStage(
                        stageId = 5,
                        classId = 1,
                        courseId = 1,
                        createdBy = "nuno",
                        votes = -47,
                        weekDay = DayOfWeek.FRIDAY,
                        begins = LocalTime.parse("17:00:00"),
                        duration = Duration.parse("PT3H"),
                        location = "E.2.21"
                )
        )
        assertEquals(5, stagingHomeworkLecture.stageId)
        assertEquals(3, stagingHomeworkLecture.logId)
        assertEquals(DayOfWeek.FRIDAY, stagingHomeworkLecture.weekDay)
        assertEquals(LocalTime.parse("17:00:00"), stagingHomeworkLecture.begins)
        assertEquals(Duration.parse("PT3H"), stagingHomeworkLecture.duration)
        assertEquals("nuno", stagingHomeworkLecture.createdBy)
        assertEquals(-47, stagingHomeworkLecture.votes)
        assertEquals("E.2.21", stagingHomeworkLecture.location)
    }

    @Test
    fun updateVotesOnStagedLecture() {
        val oldStagedLecture = lectureDAO.getSpecificStagedLectureOfCourseInClass(1, 1).get()
        assertEquals(0, oldStagedLecture.votes)
        val rowsAffected = lectureDAO.updateVotesOnStagedLecture(1, -80)
        assertEquals(1, rowsAffected)
        val updatedStagedLecture = lectureDAO.getSpecificStagedLectureOfCourseInClass(1, 1).get()
        assertEquals(-80, updatedStagedLecture.votes)
    }

    @Test
    fun deleteSpecificStagedLectureOfCourseInClass() {
        val rowsAffected = lectureDAO.deleteSpecificStagedLectureOfCourseInClass(1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllReportsOfLectureFromCourseInClass() {
        val reports = lectureDAO.getAllReportsOfLectureFromCourseInClass(1, 1)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificReportOfLectureFromCourseInClass() {
        val reportedLecture = lectureDAO.getSpecificReportOfLectureFromCourseInClass(1, 1, 1).get()
        assertEquals(1, reportedLecture.reportId)
        assertEquals(1, reportedLecture.lectureId)
        assertEquals(1, reportedLecture.classId)
        assertEquals(1, reportedLecture.courseId)
        assertEquals(1, reportedLecture.logId)
        assertEquals(DayOfWeek.FRIDAY, reportedLecture.weekDay)
        assertEquals(LocalTime.parse("10:00:00"), reportedLecture.begins)
        assertEquals(Duration.parse("PT2H"), reportedLecture.duration)
        assertEquals("nuno", reportedLecture.reportedBy)
        assertEquals(0, reportedLecture.votes)
        assertEquals("G.1.05", reportedLecture.location)
    }

    @Test
    fun createReportOnLecture() {
        val lecture = lectureDAO.createReportOnLecture(
                LectureReport(
                        lectureId = 1,
                        classId = 1,
                        courseId = 1,
                        location = "A.2.15",
                        begins = LocalTime.parse("15:00:00"),
                        duration = Duration.parse("PT1H30M"),
                        weekDay = DayOfWeek.WEDNESDAY,
                        reportedBy = "nuno",
                        votes = 10
                )
        )
        assertEquals(1, lecture.lectureId)
        assertEquals(3, lecture.reportId)
        assertEquals(3, lecture.logId)
        assertEquals("A.2.15", lecture.location)
        assertEquals(LocalTime.parse("15:00:00"), lecture.begins)
        assertEquals(Duration.parse("PT1H30M"), lecture.duration)
        assertEquals(DayOfWeek.WEDNESDAY, lecture.weekDay)
        assertEquals("nuno", lecture.reportedBy)
        assertEquals(10, lecture.votes)
    }

    @Test
    fun updateVotesOnReportedLecture() {
        val oldReportedLecture = lectureDAO.getSpecificReportOfLectureFromCourseInClass(1, 1, 1).get()
        assertEquals(0, oldReportedLecture.votes)
        val rowsAffected = lectureDAO.updateVotesOnReportedLecture(1, 1, -5)
        assertEquals(1, rowsAffected)
        val updatedReportedLEcture = lectureDAO.getSpecificReportOfLectureFromCourseInClass(1, 1, 1).get()
        assertEquals(-5, updatedReportedLEcture.votes)
    }

    @Test
    fun deleteSpecificReportOnLectureOfCourseInClass() {
        val rowsAffected = lectureDAO.deleteSpecificReportOnLectureOfCourseInClass(1, 1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllVersionsOfLectureOfCourseInclass() {
        val versions = lectureDAO.getAllVersionsOfLectureOfCourseInclass(1, 1)
        assertEquals(2, versions.size)
    }

    @Test
    fun getSpecificVersionOfLectureOfCourseInClass() {
        val homeworkVersion = lectureDAO.getSpecificVersionOfLectureOfCourseInClass(1, 1, 2).get()
        assertEquals(1, homeworkVersion.lectureId)
        assertEquals(2, homeworkVersion.version)
        assertEquals("bruno", homeworkVersion.createdBy)
        assertEquals(DayOfWeek.MONDAY, homeworkVersion.weekDay)
        assertEquals(LocalTime.parse("12:00:00"), homeworkVersion.begins)
        assertEquals(Duration.parse("PT2H"), homeworkVersion.duration)
        assertEquals("E.1.07", homeworkVersion.location)
    }

    @Test
    fun createLectureVersion() {
        val lectureVersion = lectureDAO.createLectureVersion(
                LectureVersion(
                        version = 3,
                        lectureId = 1,
                        location = "F.2.04",
                        begins = LocalTime.parse("20:00:00"),
                        createdBy = "bruno",
                        duration = Duration.parse("PT3H"),
                        weekDay = DayOfWeek.TUESDAY
                )
        )
        assertEquals(1, lectureVersion.lectureId)
        assertEquals(3, lectureVersion.version)
        assertEquals("F.2.04", lectureVersion.location)
        assertEquals(LocalTime.parse("20:00:00"), lectureVersion.begins)
        assertEquals("bruno", lectureVersion.createdBy)
        assertEquals(Duration.parse("PT3H"), lectureVersion.duration)
        assertEquals(DayOfWeek.TUESDAY, lectureVersion.weekDay)
    }

    @Test
    fun getLectureByLogId() {
        val lecture = lectureDAO.getLectureByLogId(1).get()
        assertEquals(1, lecture.classId)
        assertEquals(1, lecture.lectureId)
        assertEquals(1, lecture.courseId)
        assertEquals(2, lecture.version)
        assertEquals(0, lecture.votes)
        assertEquals("bruno", lecture.createdBy)
        assertEquals(DayOfWeek.MONDAY, lecture.weekDay)
        assertEquals(LocalTime.parse("12:00:00"), lecture.begins)
        assertEquals(Duration.parse("PT2H"), lecture.duration)
        assertEquals("E.1.07", lecture.location)
        assertEquals(1, lecture.logId)
    }

    @Test
    fun getLectureReportByLogId() {
        val report = lectureDAO.getLectureReportByLogId(1).get()
        assertEquals(1, report.reportId)
        assertEquals(1, report.lectureId)
        assertEquals(1, report.classId)
        assertEquals(1, report.courseId)
        assertEquals(1, report.logId)
        assertEquals(DayOfWeek.FRIDAY, report.weekDay)
        assertEquals(LocalTime.parse("10:00:00"), report.begins)
        assertEquals(Duration.parse("PT2H"), report.duration)
        assertEquals("nuno", report.reportedBy)
        assertEquals(0, report.votes)
        assertEquals("G.1.05", report.location)
    }

    @Test
    fun getLectureStageByLogId() {
        val stagedLecture = lectureDAO.getLectureStageByLogId(1).get()
        assertEquals(LocalTime.parse("11:30:00"), stagedLecture.begins)
        assertEquals(1, stagedLecture.stageId)
        assertEquals(1, stagedLecture.classId)
        assertEquals(1, stagedLecture.courseId)
        assertEquals(1, stagedLecture.logId)
        assertEquals(DayOfWeek.THURSDAY, stagedLecture.weekDay)
        assertEquals(Duration.parse("PT2H"), stagedLecture.duration)
        assertEquals("nuno", stagedLecture.createdBy)
        assertEquals(0, stagedLecture.votes)
        assertEquals("G.0.10", stagedLecture.location)
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}