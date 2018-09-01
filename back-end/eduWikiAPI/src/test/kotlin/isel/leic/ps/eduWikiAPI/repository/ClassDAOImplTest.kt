package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.CourseClass
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import org.junit.Test

import junit.framework.TestCase.*
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class ClassDAOImplTest {

    @Autowired
    lateinit var classDAO: ClassDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllClasses() {
        val classes = classDAO.getAllClasses()
        assertEquals(3, classes.size)
    }

    @Test
    fun getSpecificClass() {
        val klass = classDAO.getSpecificClass(1).get()
        assertEquals(1, klass.classId)
        assertEquals(1, klass.version)
        assertEquals("ze", klass.createdBy)
        assertEquals("LI51D", klass.className)
        assertEquals(1, klass.termId)
        assertEquals(1, klass.programmeId)
        assertEquals(0, klass.votes)
        assertEquals(1, klass.logId)
    }

    @Test
    fun getTermIdFromSpecificClass() {
        val termId = classDAO.getTermIdFromSpecificClass(1)
        assertEquals(1, termId)
    }

    @Test
    fun createClass() {
        val klass = classDAO.createClass(
                Class(
                        createdBy = "ricardo",
                        className = "LI61N",
                        termId = 2,
                        programmeId = 2,
                        votes = 5
                )
        )
        assertEquals(4, klass.classId)
        assertEquals(1, klass.version)
        assertEquals("ricardo", klass.createdBy)
        assertEquals("LI61N", klass.className)
        assertEquals(2, klass.termId)
        assertEquals(2, klass.programmeId)
        assertEquals(5, klass.votes)
        assertEquals(4, klass.logId)
    }

    @Test
    fun createClassMiscUnit() {
        val classMiscUnit = classDAO.createClassMiscUnit(
                1,
                ClassMiscUnitType.HOMEWORK
        )
        assertEquals(7, classMiscUnit.classMiscUnitId)
        assertEquals("HOMEWORK", classMiscUnit.miscType.name)
        assertEquals(1, classMiscUnit.courseClassId)
    }

    @Test
    fun updateClass() {
        val classUpdated = classDAO.updateClass(
                Class(
                        classId = 1,
                        version = 2,
                        createdBy = "edu",
                        className = "LI51N",
                        termId = 1,
                        programmeId = 1,
                        votes = 26
                )
        )
        assertEquals(1, classUpdated.classId)
        assertEquals(2, classUpdated.version)
        assertEquals("edu", classUpdated.createdBy)
        assertEquals("LI51N", classUpdated.className)
        assertEquals(1, classUpdated.termId)
        assertEquals(1, classUpdated.programmeId)
        assertEquals(26, classUpdated.votes)
    }

    @Test
    fun updateClassVotes() {
        val oldClass = classDAO.getSpecificClass(1).get()
        assertEquals(0, oldClass.votes)
        val rowsAffected = classDAO.updateClassVotes(1, 77)
        assertEquals(1, rowsAffected)
        val updatedClass = classDAO.getSpecificClass(1).get()
        assertEquals(77, updatedClass.votes)
    }

    @Test
    fun deleteSpecificClass() {
        val rowsAffected = classDAO.deleteSpecificClass(1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun deleteAllClassMiscUnitsFromTypeOfCourseInClass() {
        val rowsAffected = classDAO.deleteAllClassMiscUnitsFromTypeOfCourseInClass(1, ClassMiscUnitType.LECTURE)
        assertEquals(2, rowsAffected)
    }

    @Test
    fun deleteSpecificClassMiscUnitFromTypeOnCourseInClass() {
        val rowsAffected = classDAO.deleteSpecificClassMiscUnitFromTypeOnCourseInClass(1, 1, ClassMiscUnitType.LECTURE)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllStagedClasses() {
        val stagedClasses = classDAO.getAllStagedClasses()
        assertEquals(2, stagedClasses.size)
    }

    @Test
    fun getSpecificStagedClass() {
        val stagedClass = classDAO.getSpecificStagedClass(1).get()
        assertEquals(1, stagedClass.stageId)
        assertEquals("LI41D", stagedClass.className)
        assertEquals(1, stagedClass.termId)
        assertEquals(1, stagedClass.programmeId)
        assertEquals("joana", stagedClass.createdBy)
        assertEquals(1, stagedClass.logId)
        assertEquals(0, stagedClass.votes)
    }

    @Test
    fun createStagedClass() {
        val stagedClass = classDAO.createStagedClass(
                ClassStage(
                        className = "LM63N",
                        termId = 1,
                        programmeId = 1,
                        createdBy = "alice",
                        votes = 34
                )
        )
        assertEquals(3, stagedClass.stageId)
        assertEquals("LM63N", stagedClass.className)
        assertEquals(1, stagedClass.termId)
        assertEquals(1, stagedClass.programmeId)
        assertEquals("alice", stagedClass.createdBy)
        assertEquals(34, stagedClass.votes)
        assertEquals(3, stagedClass.logId)
    }

    @Test
    fun createStagingClassMiscUnit() {
        val stagingClassMiscUnit = classDAO.createStagingClassMiscUnit(1, ClassMiscUnitType.LECTURE)
        assertEquals(1, stagingClassMiscUnit.courseClassId)
        assertEquals("LECTURE", stagingClassMiscUnit.miscType.name)
        assertEquals(3, stagingClassMiscUnit.stageId)
    }

    @Test
    fun updateStagedClassVotes() {
        val oldStagedClass = classDAO.getSpecificStagedClass(1).get()
        assertEquals(0, oldStagedClass.votes)
        val rowsAffected = classDAO.updateStagedClassVotes(1, 5)
        assertEquals(1, rowsAffected)
        val updatedCourse = classDAO.getSpecificStagedClass(1).get()
        assertEquals(5, updatedCourse.votes)
    }

    @Test
    fun deleteSpecificStagedClass() {
        val rowsAffected = classDAO.deleteSpecificStagedClass(1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun deleteAllStagedClassMiscUnitsFromTypeOfCourseInClass() {
    }

    @Test
    fun deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass() {
        val rowsAffected = classDAO.deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(3, 1, ClassMiscUnitType.HOMEWORK)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllReportsFromClass() {
        val reports = classDAO.getAllReportsFromClass(1)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificReportFromClass() {
        val reportedClass = classDAO.getSpecificReportFromClass(1, 1).get()
        assertEquals(1, reportedClass.classId)
        assertEquals(1, reportedClass.reportId)
        assertEquals("LI53N", reportedClass.className)
        assertEquals(1, reportedClass.programmeId)
        assertEquals("john", reportedClass.reportedBy)
        assertEquals(0, reportedClass.votes)
        assertEquals(1, reportedClass.logId)
    }

    @Test
    fun reportClass() {
        val reportClass = classDAO.reportClass(
                classId = 1,
                report = ClassReport(
                        classId = 1,
                        className = "LI49D",
                        programmeId = 1,
                        termId = 1,
                        reportedBy = "angel",
                        votes = -2
                )
        )
        assertEquals(1, reportClass.classId)
        assertEquals(3, reportClass.reportId)
        assertEquals("LI49D", reportClass.className)
        assertEquals(1, reportClass.programmeId)
        assertEquals(1, reportClass.termId)
        assertEquals("angel", reportClass.reportedBy)
        assertEquals(-2, reportClass.votes)
        assertEquals(3, reportClass.logId)
    }

    @Test
    fun updateReportedClassVotes() {
        val oldReportedClass = classDAO.getSpecificReportFromClass(1, 1).get()
        assertEquals(0, oldReportedClass.votes)
        val rowsAffected = classDAO.updateReportedClassVotes(1, 1, 47)
        assertEquals(1, rowsAffected)
        val updatedReportedClass = classDAO.getSpecificReportFromClass(1, 1).get()
        assertEquals(47, updatedReportedClass.votes)
    }

    @Test
    fun deleteSpecificReportInClass() {
        val rowsAffected = classDAO.deleteSpecificReportInClass(1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllVersionsOfSpecificClass() {
        val versions = classDAO.getAllVersionsOfSpecificClass(1)
        assertEquals(2, versions.size)
    }

    @Test
    fun getVersionOfSpecificClass() {
        val classVersion = classDAO.getVersionOfSpecificClass(1, 1).get()
        assertEquals(1, classVersion.classId)
        assertEquals(1, classVersion.version)
        assertEquals(2, classVersion.programmeId)
        assertEquals(1, classVersion.termId)
        assertEquals("LI51D", classVersion.className)
        assertEquals("cris", classVersion.createdBy)
    }

    @Test
    fun createClassVersion() {
        val classVersion = classDAO.createClassVersion(
                ClassVersion(
                        classId = 1,
                        className = "LI43N",
                        programmeId = 1,
                        termId = 1,
                        createdBy = "johnny",
                        version = 3
                )
        )
        assertEquals(1, classVersion.termId)
        assertEquals(3, classVersion.version)
        assertEquals(1, classVersion.programmeId)
        assertEquals(1, classVersion.classId)
        assertEquals("johnny", classVersion.createdBy)
    }

    @Test
    fun getAllCoursesOfClass() {
        val courses = classDAO.getAllCoursesOfClass(2)
        assertEquals(2, courses.size)
    }

    @Test
    fun getCourseClass() {
        val courseClass = classDAO.getCourseClass(1, 1).get()
        assertEquals(1, courseClass.courseClassId)
        assertEquals(1, courseClass.classId)
        assertEquals(1, courseClass.courseId)
        assertEquals(1, courseClass.termId)
        assertEquals("rui", courseClass.createdBy)
        assertEquals(0, courseClass.votes)
        assertEquals(1, courseClass.logId)
    }

    @Test
    fun getCourseClassFromId() {
        val courseClass = classDAO.getCourseClassFromId(1)
        assertEquals(1, courseClass.courseClassId)
        assertEquals(1, courseClass.classId)
        assertEquals(1, courseClass.courseId)
        assertEquals(1, courseClass.termId)
        assertEquals("rui", courseClass.createdBy)
        assertEquals(0, courseClass.votes)
        assertEquals(1, courseClass.logId)
    }

    @Test
    fun getSpecificReportOfCourseInClass() {
        val reportCourseClass = classDAO.getSpecificReportOfCourseInClass(1, 1, 1).get()
        assertEquals(1, reportCourseClass.classId)
        assertEquals(1, reportCourseClass.reportId)
        assertEquals(1, reportCourseClass.courseClassId)
        assertEquals(1, reportCourseClass.courseId)
        assertEquals(1, reportCourseClass.termId)
        assertEquals(false, reportCourseClass.deletePermanently)
        assertEquals(1, reportCourseClass.logId)
        assertEquals("alice", reportCourseClass.reportedBy)
        assertEquals(19, reportCourseClass.votes)
    }

    @Test
    fun getAllReportsOfCourseInClass() {
        val reports = classDAO.getAllReportsOfCourseInClass(1)
        assertEquals(2, reports.size)
    }

    @Test
    fun getStageEntriesOfCoursesInClass() {
        val reports = classDAO.getStageEntriesOfCoursesInClass(2)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificStagedCourseInClass() {
        val stagedCourseClass = classDAO.getSpecificStagedCourseInClass(2, 2).get()
        assertEquals(2, stagedCourseClass.stageId)
        assertEquals(2, stagedCourseClass.courseId)
        assertEquals(2, stagedCourseClass.classId)
        assertEquals(2, stagedCourseClass.termId)
        assertEquals("manuel", stagedCourseClass.createdBy)
        assertEquals(2, stagedCourseClass.logId)
        assertEquals(3, stagedCourseClass.votes)
    }

    @Test
    fun deleteSpecificCourseInClass() {
        val rowsAffected = classDAO.deleteSpecificCourseInClass(2, 2)
        assertEquals(1 , rowsAffected)
    }

    @Test
    fun deleteSpecificCourseReportInClass() {
        val rowsAffected = classDAO.deleteSpecificReportInClass(1, 1)
        assertEquals(1 , rowsAffected)
    }

    @Test
    fun addCourseToClass() {
        val courseClass = classDAO.addCourseToClass(
                CourseClass(
                        courseId = 3,
                        classId = 1,
                        termId = 1,
                        createdBy = "miguel",
                        votes = -14
                )
        )
        assertEquals(4, courseClass.courseClassId)
        assertEquals(3, courseClass.courseId)
        assertEquals(1, courseClass.classId)
        assertEquals(1, courseClass.termId)
        assertEquals("miguel", courseClass.createdBy)
        assertEquals(-14, courseClass.votes)
        assertEquals(4, courseClass.logId)
    }

    @Test
    fun deleteSpecificStagedCourseInClass() {
        val rowsAffected = classDAO.deleteSpecificStagedCourseInClass(2, 2)
        assertEquals(1 , rowsAffected)
    }

    @Test
    fun reportCourseInClass() {
        val reportCourseClass = classDAO.reportCourseInClass(
                CourseClassReport(
                        courseClassId = 1,
                        classId = 2,
                        courseId = 1,
                        termId = 1,
                        deletePermanently = true,
                        votes = 41,
                        reportedBy = "joao"
                )
        )
        assertEquals(2, reportCourseClass.classId)
        assertEquals("joao", reportCourseClass.reportedBy)
        assertEquals(3, reportCourseClass.reportId)
        assertEquals(1, reportCourseClass.courseClassId)
        assertEquals(1, reportCourseClass.courseId)
        assertEquals(1, reportCourseClass.termId)
        assertEquals(false, reportCourseClass.deletePermanently)
        assertEquals(3, reportCourseClass.logId)
        assertEquals(41, reportCourseClass.votes)
    }

    @Test
    fun deleteSpecificReportOnCourseClass() {
        val rowsAffected = classDAO.deleteSpecificReportOnCourseClass(1, 1)
        assertEquals(1 , rowsAffected)
    }

    @Test
    fun createStagingCourseInClass() {
        val stagingCourseInClass = classDAO.createStagingCourseInClass(
                CourseClassStage(
                        courseId = 2,
                        classId = 1,
                        termId = 1,
                        createdBy = "edgar",
                        votes = 10
                )
        )
        assertEquals(2, stagingCourseInClass.courseId)
        assertEquals(3, stagingCourseInClass.stageId)
        assertEquals(1, stagingCourseInClass.classId)
        assertEquals(1, stagingCourseInClass.termId)
        assertEquals("edgar", stagingCourseInClass.createdBy)
        assertEquals(10, stagingCourseInClass.votes)
        assertEquals(3, stagingCourseInClass.logId)
    }

    @Test
    fun updateCourseClassVotes() {
        val oldCourseClass = classDAO.getCourseClass(1,1).get()
        assertEquals(0, oldCourseClass.votes)
        val rowsAffected = classDAO.updateCourseClassVotes(1, 1, 27)
        assertEquals(1, rowsAffected)
        val updatedCourseClass = classDAO.getCourseClass(1,1 ).get()
        assertEquals(27, updatedCourseClass.votes)
    }

    @Test
    fun updateReportedCourseClassVotes() {
        val oldReportedCourseClass = classDAO.getSpecificReportOfCourseInClass(1, 1,1).get()
        assertEquals(19, oldReportedCourseClass.votes)
        val rowsAffected = classDAO.updateReportedCourseClassVotes(1, 1, 1, 17)
        assertEquals(1, rowsAffected)
        val updatedReportedCourseClass = classDAO.getSpecificReportOfCourseInClass(1, 1,1 ).get()
        assertEquals(17, updatedReportedCourseClass.votes)
    }

    @Test
    fun updateStagedCourseClassVotes() {
        val oldStagedCourseClass = classDAO.getSpecificStagedCourseInClass( 2,2).get()
        assertEquals(3, oldStagedCourseClass.votes)
        val rowsAffected = classDAO.updateStagedCourseClassVotes(2, 2, 12)
        assertEquals(1, rowsAffected)
        val updatedReportedCourseClass = classDAO.getSpecificStagedCourseInClass( 2,2).get()
        assertEquals(12, updatedReportedCourseClass.votes)
    }

    @Test
    fun getClassByLogId() {
        val klass = classDAO.getClassByLogId(1).get()
        assertEquals(1, klass.classId)
        assertEquals(1, klass.version)
        assertEquals("ze", klass.createdBy)
        assertEquals("LI51D", klass.className)
        assertEquals(1, klass.termId)
        assertEquals(1, klass.programmeId)
        assertEquals(0, klass.votes)
        assertEquals(1, klass.logId)
    }

    @Test
    fun getClassReportByLogId() {
        val classReport = classDAO.getClassReportByLogId(1).get()
        assertEquals(1, classReport.classId)
        assertEquals(1, classReport.reportId)
        assertEquals("LI53N", classReport.className)
        assertEquals(1, classReport.programmeId)
        assertEquals("john", classReport.reportedBy)
        assertEquals(0, classReport.votes)
        assertEquals(1, classReport.logId)
    }

    @Test
    fun getClassStageByLogId() {
        val classStage = classDAO.getClassStageByLogId(1).get()
        assertEquals(1, classStage.stageId)
        assertEquals("LI41D", classStage.className)
        assertEquals(1, classStage.termId)
        assertEquals(1, classStage.programmeId)
        assertEquals("joana", classStage.createdBy)
        assertEquals(1, classStage.logId)
        assertEquals(0, classStage.votes)
    }

    @Test
    fun getCourseClassByLogId() {
        val courseClass = classDAO.getCourseClassByLogId(1).get()
        assertEquals(1, courseClass.courseClassId)
        assertEquals(1, courseClass.classId)
        assertEquals(1, courseClass.courseId)
        assertEquals(1, courseClass.termId)
        assertEquals("rui", courseClass.createdBy)
        assertEquals(0, courseClass.votes)
        assertEquals(1, courseClass.logId)
    }

    @Test
    fun getCourseClassReportByLogId() {
        val courseClassReport = classDAO.getCourseClassReportByLogId(1).get()
        assertEquals(1, courseClassReport.classId)
        assertEquals(1, courseClassReport.reportId)
        assertEquals("alice", courseClassReport.reportedBy)
        assertEquals(19, courseClassReport.votes)
        assertEquals(1, courseClassReport.logId)
        assertEquals(1, courseClassReport.courseClassId)
        assertEquals(1, courseClassReport.courseId)
        assertEquals(1, courseClassReport.termId)
        assertEquals(false, courseClassReport.deletePermanently)
        assertEquals(1, courseClassReport.logId)
    }

    @Test
    fun getCourseClassStageByLogId() {
        val courseClassStage = classDAO.getCourseClassStageByLogId(1).get()
        assertEquals(1, courseClassStage.stageId)
        assertEquals(2, courseClassStage.termId)
        assertEquals("xico", courseClassStage.createdBy)
        assertEquals(1, courseClassStage.logId)
        assertEquals(88, courseClassStage.votes)
        assertEquals(2, courseClassStage.courseId)
        assertEquals(2, courseClassStage.classId)
    }

}