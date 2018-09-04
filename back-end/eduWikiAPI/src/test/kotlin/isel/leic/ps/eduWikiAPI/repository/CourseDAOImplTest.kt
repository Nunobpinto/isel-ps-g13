package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.enums.CourseMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.CourseProgramme
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before

import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime


@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class CourseDAOImplTest {

    @Autowired
    lateinit var courseDAO: CourseDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllCourses() {
        val courses = courseDAO.getAllCourses()
        assertEquals(3, courses.size)
    }

    @Test
    fun getSpecificCourse() {
        val course = courseDAO.getSpecificCourse(1).get()
        assertEquals(1, course.courseId)
        assertEquals("PI", course.shortName)
        assertEquals("Programacao na Internet", course.fullName)
        assertEquals(2, course.version)
        assertEquals(0, course.votes)
        assertEquals("jg", course.createdBy)
    }

    @Test
    fun getTermsOfCourse() {
        val terms = courseDAO.getTermsOfCourse(1)
        assertEquals(2, terms.size)
    }

    @Test
    fun getSpecificTermOfCourse() {
        val term = courseDAO.getSpecificTermOfCourse(1, 1).get()
        assertEquals("1718v", term.shortName)
        assertEquals("SUMMER", term.type.name)
        assertEquals(1, term.termId)
        assertEquals(2018, term.year)
    }

    @Test
    fun getClassesOfSpecificCourseInTerm() {
        val classes = courseDAO.getClassesOfSpecificCourseInTerm(1, 1)
        assertEquals(1, classes.size)
    }

    @Test
    fun getSpecificClassOfSpecificCourseInTerm() {
        val klass = courseDAO.getSpecificClassOfSpecificCourseInTerm(1, 1, 1).get()
        assertEquals("LI51D", klass.className)
        assertEquals(1, klass.classId)
        assertEquals("ze", klass.createdBy)
        assertEquals(1, klass.logId)
        assertEquals(1, klass.programmeId)
        assertEquals(1, klass.termId)
        assertEquals(2, klass.version)
        assertEquals(0, klass.votes)
    }

    @Test
    fun updateCourse() {
        val oldCourse = courseDAO.getSpecificCourse(1).get()
        assertEquals(2, oldCourse.version)
        val updatedCourse = courseDAO.updateCourse(
                Course(
                        courseId = oldCourse.courseId,
                        version = oldCourse.version.inc(),
                        logId = oldCourse.logId,
                        votes = oldCourse.votes,
                        createdBy = oldCourse.createdBy,
                        fullName = "Matematica",
                        shortName = "M1",
                        timestamp = Timestamp.valueOf(LocalDateTime.now())
                )
        )
        assertEquals("Matematica", updatedCourse.fullName)
        assertEquals("M1", updatedCourse.shortName)
        assertEquals(0, updatedCourse.votes)
        assertEquals(3, updatedCourse.version)
        assertEquals(1, updatedCourse.logId)
        assertEquals(1, updatedCourse.courseId)
        assertEquals("jg", updatedCourse.createdBy)
    }

    @Test
    fun createCourse() {
        val course = courseDAO.createCourse(
                Course(
                        createdBy = "jg",
                        fullName = "Algebra",
                        shortName = "Alga"
                )
        )
        assertEquals("Algebra", course.fullName)
        assertEquals("Alga", course.shortName)
        assertEquals(0, course.votes)
        assertEquals(1, course.version)
        assertEquals(4, course.logId)
        assertEquals(4, course.courseId)
        assertEquals("jg", course.createdBy)
    }

    @Test
    fun deleteSpecificCourse() {
        val course = courseDAO.createCourse(
                Course(
                        createdBy = "jg",
                        fullName = "Algebra",
                        shortName = "Alga"
                )
        )
        val rowsAffected = courseDAO.deleteSpecificCourse(course.courseId)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun updateVotesOnCourse() {
        val oldCourse = courseDAO.getSpecificCourse(1).get()
        assertEquals(0, oldCourse.votes)
        val rowsAffected = courseDAO.updateVotesOnCourse(1, 5)
        assertEquals(1, rowsAffected)
        val updatedCourse = courseDAO.getSpecificCourse(1).get()
        assertEquals(5, updatedCourse.votes)
    }

    @Test
    fun createCourseTerm() {
        val courseTerm = courseDAO.createCourseTerm(3, 1, Timestamp.valueOf(LocalDateTime.now()))
        assertEquals(3, courseTerm.courseId)
        assertEquals(1, courseTerm.termId)
    }

    @Test
    fun getAllCourseStageEntries() {
        val stagedCourses = courseDAO.getAllCourseStageEntries()
        assertEquals(3, stagedCourses.size)
    }

    @Test
    fun getCourseSpecificStageEntry() {
        val stagedCourse = courseDAO.getCourseSpecificStageEntry(1).get()
        assertEquals("jg", stagedCourse.createdBy)
        assertEquals("Programação", stagedCourse.fullName)
        assertEquals(1, stagedCourse.logId)
        assertEquals("PG", stagedCourse.shortName)
        assertEquals(1, stagedCourse.stageId)
        assertEquals(0, stagedCourse.votes)
    }

    @Test
    fun createStagingCourse() {
        val stagingCourse = courseDAO.createStagingCourse(
                CourseStage(
                        fullName = "Sistemas de Informação 1",
                        shortName = "SI1",
                        createdBy = "jg"
                )
        )
        assertEquals("SI1", stagingCourse.shortName)
        assertEquals("Sistemas de Informação 1", stagingCourse.fullName)
        assertEquals(0, stagingCourse.votes)
        assertEquals(4, stagingCourse.stageId)
        assertEquals("jg", stagingCourse.createdBy)
        assertEquals(4, stagingCourse.logId)
    }

    @Test
    fun deleteStagedCourse() {
        val rowsAffected = courseDAO.deleteStagedCourse(3)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun updateVotesOnStagedCourse() {
        val oldStagedCourse = courseDAO.getCourseSpecificStageEntry(1).get()
        assertEquals(0, oldStagedCourse.votes)
        val rowsAffected = courseDAO.updateVotesOnStagedCourse(1, 10)
        assertEquals(1, rowsAffected)
        val updatedStagedCourse = courseDAO.getCourseSpecificStageEntry(1).get()
        assertEquals(10, updatedStagedCourse.votes)
    }

    @Test
    fun getAllVersionsOfSpecificCourse() {
        val courseVersions = courseDAO.getAllVersionsOfSpecificCourse(1)
        assertEquals(2, courseVersions.size)
    }

    @Test
    fun getVersionOfSpecificCourse() {
        val courseVersion = courseDAO.getVersionOfSpecificCourse(1, 2).get()
        assertEquals(1, courseVersion.courseId)
        assertEquals("jg", courseVersion.createdBy)
        assertEquals("Programacao na Internet", courseVersion.fullName)
        assertEquals(2, courseVersion.version)
        assertEquals("PI", courseVersion.shortName)
    }


    @Test
    fun getAllReportsOnCourse() {
        val reportsOnCourse = courseDAO.getAllReportsOnCourse(3)
        assertEquals(2, reportsOnCourse.size)
    }

    @Test
    fun getSpecificReportOfCourse() {
        val reportOnCourse = courseDAO.getSpecificReportOfCourse(3, 1).get()
        assertEquals(3, reportOnCourse.courseId)
        assertEquals("Modelacao Ambientes Virtuais", reportOnCourse.fullName)
        assertEquals(1, reportOnCourse.logId)
        assertEquals(1, reportOnCourse.reportId)
        assertEquals("Gedson", reportOnCourse.reportedBy)
        assertEquals("MAV", reportOnCourse.shortName)
        assertEquals(2, reportOnCourse.votes)
    }

    @Test
    fun createCourseVersion() {
        val courseVersion = courseDAO.createCourseVersion(
                CourseVersion(
                        courseId = 2,
                        version = 2,
                        fullName = "Redes Computacionais",
                        shortName = "RC",
                        createdBy = "jg"
                )
        )
        assertEquals(2, courseVersion.courseId)
        assertEquals("jg", courseVersion.createdBy)
        assertEquals("Redes Computacionais", courseVersion.fullName)
        assertEquals(2, courseVersion.version)
        assertEquals("RC", courseVersion.shortName)
    }

    @Test
    fun reportCourse() {
        val report = courseDAO.reportCourse(
                1,
                CourseReport(
                        courseId = 1,
                        fullName = "Programação na Web",
                        shortName = "PW",
                        reportedBy = "renato"
                )
        )
        assertEquals(1, report.courseId)
        assertEquals("Programação na Web", report.fullName)
        assertEquals(3, report.logId)
        assertEquals(3, report.reportId)
        assertEquals("renato", report.reportedBy)
        assertEquals("PW", report.shortName)
        assertEquals(0, report.votes)
    }

    @Test
    fun deleteReportOnCourse() {
        val rowsAffected = courseDAO.deleteReportOnCourse(2)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun updateVotesOnReportedCourse() {
        val oldReportedCourse = courseDAO.getSpecificReportOfCourse(3, 2).get()
        assertEquals(7, oldReportedCourse.votes)
        val rowsAffected = courseDAO.updateVotesOnReportedCourse(2, 17)
        assertEquals(1, rowsAffected)
        val updatedStagedCourse = courseDAO.getSpecificReportOfCourse(3, 2).get()
        assertEquals(17, updatedStagedCourse.votes)
    }

    @Test
    fun getAllCoursesOnSpecificProgramme() {
        val programmeCourses = courseDAO.getAllCoursesOnSpecificProgramme(1)
        assertEquals(2, programmeCourses.size)
    }

    @Test
    fun getSpecificCourseOfProgramme() {
        val courseProgramme = courseDAO.getSpecificCourseOfProgramme(1, 1).get()
        assertEquals(1, courseProgramme.courseId)
        assertEquals("ze", courseProgramme.createdBy)
        assertEquals(6, courseProgramme.credits)
        assertEquals("quinto", courseProgramme.lecturedTerm)
        assertEquals(1, courseProgramme.logId)
        assertEquals(false, courseProgramme.optional)
        assertEquals(1, courseProgramme.programmeId)
        assertEquals(2, courseProgramme.version)
        assertEquals(0, courseProgramme.votes)
    }

    @Test
    fun updateCourseProgramme() {
        val courseProgramme = courseDAO.getSpecificCourseOfProgramme(1, 1).get()
        assertEquals(2, courseProgramme.version)
        val updatedCourseProgramme = courseDAO.updateCourseProgramme(
                1,
                1,
                CourseProgramme(
                        courseId = courseProgramme.courseId,
                        programmeId = courseProgramme.programmeId,
                        version = courseProgramme.version.inc(),
                        lecturedTerm = courseProgramme.lecturedTerm,
                        optional = !courseProgramme.optional,
                        credits = courseProgramme.credits,
                        createdBy = "mile"
                )
        )
        assertEquals(1, updatedCourseProgramme.courseId)
        assertEquals("mile", updatedCourseProgramme.createdBy)
        assertEquals(6, updatedCourseProgramme.credits)
        assertEquals("quinto", updatedCourseProgramme.lecturedTerm)
        assertEquals(1, updatedCourseProgramme.logId)
        assertEquals(true, updatedCourseProgramme.optional)
        assertEquals(1, updatedCourseProgramme.programmeId)
        assertEquals(3, updatedCourseProgramme.version)
        assertEquals(0, updatedCourseProgramme.votes)
    }

    @Test
    fun addCourseToProgramme() {
        val courseProgramme = courseDAO.addCourseToProgramme(
                2,
                CourseProgramme(
                        courseId = 1,
                        programmeId = 2,
                        lecturedTerm = "4",
                        optional = true,
                        credits = 5,
                        createdBy = "andre"
                )
        )
        assertEquals(1, courseProgramme.courseId)
        assertEquals("andre", courseProgramme.createdBy)
        assertEquals(5, courseProgramme.credits)
        assertEquals("4", courseProgramme.lecturedTerm)
        assertEquals(5, courseProgramme.logId)
        assertEquals(true, courseProgramme.optional)
        assertEquals(2, courseProgramme.programmeId)
        assertEquals(1, courseProgramme.version)
        assertEquals(0, courseProgramme.votes)
    }

    @Test
    fun deleteSpecificCourseProgramme() {
        val rowsAffected = courseDAO.deleteSpecificCourseProgramme(1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun updateVotesOnCourseProgramme() {
        val oldCourseProgramme = courseDAO.getSpecificCourseOfProgramme(1, 1).get()
        assertEquals(0, oldCourseProgramme.votes)
        val rowsAffected = courseDAO.updateVotesOnCourseProgramme(1, 1, 23)
        assertEquals(1, rowsAffected)
        val updatedCourseProgramme = courseDAO.getSpecificCourseOfProgramme(1, 1).get()
        assertEquals(23, updatedCourseProgramme.votes)
    }

    @Test
    fun getAllCourseStageEntriesOfSpecificProgramme() {
        val stageEntries = courseDAO.getAllCourseStageEntriesOfSpecificProgramme(1)
        assertEquals(2, stageEntries.size)
    }

    @Test
    fun getSpecificStagedCourseProgramme() {
        val stageEntry = courseDAO.getSpecificStagedCourseProgramme(1, 2).get()
        assertEquals(3, stageEntry.courseId)
        assertEquals("rui", stageEntry.createdBy)
        assertEquals(6, stageEntry.credits)
        assertEquals("thirth", stageEntry.lecturedTerm)
        assertEquals(2, stageEntry.logId)
        assertEquals(true, stageEntry.optional)
        assertEquals(1, stageEntry.programmeId)
        assertEquals(2, stageEntry.stageId)
        assertEquals(2, stageEntry.votes)
    }

    @Test
    fun createStagingCourseOfProgramme() {
        val stagingCourseOfProgramme = courseDAO.createStagingCourseOfProgramme(
                CourseProgrammeStage(
                        courseId = 1,
                        programmeId = 2,
                        lecturedTerm = "second",
                        optional = false,
                        credits = 6,
                        createdBy = "francisco"
                )
        )
        assertEquals(1, stagingCourseOfProgramme.courseId)
        assertEquals("francisco", stagingCourseOfProgramme.createdBy)
        assertEquals(6, stagingCourseOfProgramme.credits)
        assertEquals("second", stagingCourseOfProgramme.lecturedTerm)
        assertEquals(4, stagingCourseOfProgramme.logId)
        assertEquals(false, stagingCourseOfProgramme.optional)
        assertEquals(2, stagingCourseOfProgramme.programmeId)
        assertEquals(4, stagingCourseOfProgramme.stageId)
        assertEquals(0, stagingCourseOfProgramme.votes)
    }

    @Test
    fun deleteSpecificStagedCourseProgramme() {
        val affectedRows = courseDAO.deleteSpecificStagedCourseProgramme(1, 2)
        assertEquals(1, affectedRows)
    }

    @Test
    fun updateVotesOnStagedCourseProgramme() {
        val oldStagedCourseProgramme = courseDAO.getSpecificStagedCourseProgramme(1, 2).get()
        assertEquals(2, oldStagedCourseProgramme.votes)
        val rowsAffected = courseDAO.updateVotesOnStagedCourseProgramme(1, 2, 42)
        assertEquals(1, rowsAffected)
        val updatedStagedCourseProgramme = courseDAO.getSpecificStagedCourseProgramme(1, 2).get()
        assertEquals(42, updatedStagedCourseProgramme.votes)
    }

    @Test
    fun getAllVersionsOfCourseOnProgramme() {
        val versions = courseDAO.getAllVersionsOfCourseOnProgramme(1, 1)
        assertEquals(2, versions.size)
    }

    @Test
    fun getSpecificVersionOfCourseOnProgramme() {
        val version = courseDAO.getSpecificVersionOfCourseOnProgramme(1, 1, 2).get()
        assertEquals(1, version.courseId)
        assertEquals("ze", version.createdBy)
        assertEquals(6, version.credits)
        assertEquals("quinto", version.lecturedTerm)
        assertEquals(false, version.optional)
        assertEquals(1, version.programmeId)
        assertEquals(2, version.version)
    }

    @Test
    fun createCourseProgrammeVersion() {
        val createVersion = courseDAO.createCourseProgrammeVersion(
                CourseProgrammeVersion(
                        courseId = 2,
                        programmeId = 1,
                        lecturedTerm = "first",
                        optional = true,
                        credits = 6,
                        createdBy = "jg",
                        version = 2
                )
        )
        assertEquals(2, createVersion.courseId)
        assertEquals("jg", createVersion.createdBy)
        assertEquals(6, createVersion.credits)
        assertEquals("first", createVersion.lecturedTerm)
        assertEquals(true, createVersion.optional)
        assertEquals(1, createVersion.programmeId)
        assertEquals(2, createVersion.version)
    }

    @Test
    fun getAllReportsOfCourseOnProgramme() {
        val reports = courseDAO.getAllReportsOfCourseOnProgramme(1, 2)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificReportOfCourseProgramme() {
        val report = courseDAO.getSpecificReportOfCourseProgramme(1, 2, 2).get()
        assertEquals(2, report.courseId)
        assertEquals(6, report.credits)
        assertEquals(false, report.deleteFlag)
        assertEquals("fourth", report.lecturedTerm)
        assertEquals(2, report.logId)
        assertEquals(false, report.optional)
        assertEquals(1, report.programmeId)
        assertEquals(2, report.reportId)
        assertEquals("cristiano", report.reportedBy)
        assertEquals(31, report.votes)
    }

    @Test
    fun reportSpecificCourseOnProgramme() {
        val report = courseDAO.reportSpecificCourseOnProgramme(
                programmeId = 1,
                courseId = 2,
                courseProgrammeReport = CourseProgrammeReport(
                        courseId = 2,
                        programmeId = 1,
                        lecturedTerm = "first",
                        optional = false,
                        credits = 6,
                        reportedBy = "tiago",
                        deleteFlag = false,
                        votes = 55
                )
        )
        assertEquals(2, report.courseId)
        assertEquals(6, report.credits)
        assertEquals(false, report.deleteFlag)
        assertEquals("first", report.lecturedTerm)
        assertEquals(3, report.logId)
        assertEquals(false, report.optional)
        assertEquals(1, report.programmeId)
        assertEquals(3, report.reportId)
        assertEquals("tiago", report.reportedBy)
        assertEquals(55, report.votes)
    }

    @Test
    fun deleteSpecificReportOfCourseProgramme() {
        val affectedRows = courseDAO.deleteSpecificReportOfCourseProgramme(1, 2, 1)
        assertEquals(1, affectedRows)
    }

    @Test
    fun updateVotesOnReportedCourseProgramme() {
        val oldReportedCourseProgramme = courseDAO.getSpecificReportOfCourseProgramme(1, 2, 1).get()
        assertEquals(24, oldReportedCourseProgramme.votes)
        val rowsAffected = courseDAO.updateVotesOnReportedCourseProgramme(1, 2, 1, 71)
        assertEquals(1, rowsAffected)
        val updatedReportedCourseProgramme = courseDAO.getSpecificReportOfCourseProgramme(1, 2, 1).get()
        assertEquals(71, updatedReportedCourseProgramme.votes)
    }

    @Test
    fun createCourseMiscUnit() {
        val courseMiscUnit = courseDAO.createCourseMiscUnit(1, 1,  CourseMiscUnitType.EXAM_TEST)
        assertEquals(1, courseMiscUnit.courseId)
        assertEquals(9, courseMiscUnit.courseMiscUnitId)
        assertEquals("EXAM_TEST", courseMiscUnit.miscType.name)
        assertEquals(1, courseMiscUnit.termId)
    }

    @Test
    fun deleteSpecificCourseMiscUnitEntry() {
        val affectedRows = courseDAO.deleteSpecificCourseMiscUnitEntry(1, 1, 1)
        assertEquals(1, affectedRows)
    }

    @Test
    fun deleteAllCourseMiscUnitsFromTypeOfCourseInTerm() {
        val affectedRows = courseDAO.deleteAllCourseMiscUnitsFromTypeOfCourseInTerm(1, 1, CourseMiscUnitType.EXAM_TEST)
        assertEquals(2, affectedRows)
    }

    @Test
    fun createStagingCourseMiscUnit() {
        val stagingCourseMiscUnit = courseDAO.createStagingCourseMiscUnit(1, 1, CourseMiscUnitType.WORK_ASSIGNMENT)
        assertEquals(1, stagingCourseMiscUnit.courseId)
        assertEquals(1, stagingCourseMiscUnit.termId)
        assertEquals("WORK_ASSIGNMENT", stagingCourseMiscUnit.miscType.name)
        assertEquals(5, stagingCourseMiscUnit.stageId)
    }

    @Test
    fun deleteSpecificStagedCourseMiscUnitEntry() {
        val affectedRows = courseDAO.deleteSpecificStagedCourseMiscUnitEntry(1, 1, 1)
        assertEquals(1, affectedRows)
    }

    @Test
    fun getCourseByLogId() {
        val course = courseDAO.getCourseByLogId(2).get()
        assertEquals(0, course.votes)
        assertEquals(1, course.version)
        assertEquals(2, course.courseId)
        assertEquals("ze", course.createdBy)
        assertEquals("Redes de Computadores", course.fullName)
        assertEquals(2, course.logId)
        assertEquals("RCP", course.shortName)
    }

    @Test
    fun getCourseReportByLogId() {
        val courseReport = courseDAO.getCourseReportByLogId(1).get()
        assertEquals("Gedson", courseReport.reportedBy)
        assertEquals(1, courseReport.reportId)
        assertEquals(3, courseReport.courseId)
        assertEquals(2, courseReport.votes)
        assertEquals("Modelacao Ambientes Virtuais", courseReport.fullName)
        assertEquals(1, courseReport.logId)
        assertEquals("MAV", courseReport.shortName)
    }

    @Test
    fun getCourseStageByLogId() {
        val courseStage = courseDAO.getCourseStageByLogId(1).get()
        assertEquals("jg", courseStage.createdBy)
        assertEquals(1, courseStage.logId)
        assertEquals("Programação", courseStage.fullName)
        assertEquals("PG", courseStage.shortName)
        assertEquals(1, courseStage.stageId)
        assertEquals(0, courseStage.votes)
    }

    @Test
    fun getCourseProgrammeByLogId() {
        val courseProgramme = courseDAO.getCourseProgrammeByLogId(1).get()
        assertEquals("ze", courseProgramme.createdBy)
        assertEquals(1, courseProgramme.logId)
        assertEquals(1, courseProgramme.courseId)
        assertEquals(6, courseProgramme.credits)
        assertEquals(2, courseProgramme.version)
        assertEquals("quinto", courseProgramme.lecturedTerm)
        assertEquals(1, courseProgramme.programmeId)
        assertEquals(0, courseProgramme.votes)
        assertEquals(1, courseProgramme.courseId)
        assertEquals(false, courseProgramme.optional)
    }

    @Test
    fun getCourseProgrammeReportByLogId() {
        val courseProgrammeReport = courseDAO.getCourseProgrammeReportByLogId(1).get()
        assertEquals(1, courseProgrammeReport.reportId)
        assertEquals(1, courseProgrammeReport.logId)
        assertEquals(2, courseProgrammeReport.courseId)
        assertEquals(6, courseProgrammeReport.credits)
        assertEquals("first", courseProgrammeReport.lecturedTerm)
        assertEquals(1, courseProgrammeReport.programmeId)
        assertEquals("miguel", courseProgrammeReport.reportedBy)
        assertEquals(24, courseProgrammeReport.votes)
        assertEquals(true, courseProgrammeReport.optional)
        assertEquals(false, courseProgrammeReport.deleteFlag)
    }

    @Test
    fun getCourseProgrammeStageByLogId() {
        val courseProgrammeStage = courseDAO.getCourseProgrammeStageByLogId(1).get()
        assertEquals(1, courseProgrammeStage.stageId)
        assertEquals(1, courseProgrammeStage.logId)
        assertEquals(1, courseProgrammeStage.courseId)
        assertEquals(7, courseProgrammeStage.credits)
        assertEquals("sixth", courseProgrammeStage.lecturedTerm)
        assertEquals(1, courseProgrammeStage.programmeId)
        assertEquals("ruben", courseProgrammeStage.createdBy)
        assertEquals(3, courseProgrammeStage.votes)
        assertEquals(false, courseProgrammeStage.optional)
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}