package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

interface ExamDAOJdbi : ExamDAO {

    companion object {
        //TABLE NAMES
        const val EXAM_TABLE = "exam"
        const val EXAM_VERSION_TABLE = "exam_version"
        const val EXAM_REPORT_TABLE = "exam_report"
        const val EXAM_STAGE_TABLE = "exam_stage"
        // FIELDS
        const val EXAM_ID = "exam_id"
        const val EXAM_VERSION = "exam_version"
        const val EXAM_VERSION_ID = "exam_id"
        const val EXAM_SHEET = "sheet"
        const val EXAM_DUE_DATE = "due_date"
        const val EXAM_TYPE = "exam_type"
        const val EXAM_PHASE = "phase"
        const val EXAM_LOCATION = "location"
        const val EXAM_VOTES = "votes"
        const val EXAM_TIMESTAMP = "time_stamp"
        const val EXAM_REPORT_ID = "exam_report_id"
        const val EXAM_STAGE_ID = "exam_stage_id"
        const val EXAM_REPORTED_BY = "reported_by"
        const val EXAM_CREATED_BY = "created_by"
    }

    @CreateSqlObject
    fun createCourseDAO(): CourseDAOJdbi

    override fun deleteSpecificExamOfCourseInTerm(examId: Int): Int =
            createCourseDAO().deleteSpecificCourseMiscUnitEntry(examId)

    @SqlUpdate(
            "update $EXAM_TABLE SET " +
                    "$EXAM_VERSION = :exam.version, " +
                    "$EXAM_CREATED_BY = :exam.createdBy, " +
                    "$EXAM_SHEET = :exam.sheet, " +
                    "$EXAM_DUE_DATE = :exam.dueDate, " +
                    "$EXAM_TYPE = :exam.type, " +
                    "$EXAM_PHASE = :exam.phase, " +
                    "$EXAM_LOCATION = :exam.location, " +
                    "$EXAM_VOTES = :exam.votes, " +
                    "$EXAM_TIMESTAMP = :exam.timestamp " +
                    "WHERE $EXAM_ID = :examId"
    )
    @GetGeneratedKeys
    override fun updateExam(examId: Int, exam: Exam): Exam

    @SqlUpdate(

            "INSERT INTO $EXAM_TABLE ( " +
                    "$EXAM_ID, " +
                    "$EXAM_VERSION, " +
                    "$EXAM_CREATED_BY, " +
                    "$EXAM_SHEET, " +
                    "$EXAM_DUE_DATE, " +
                    "$EXAM_TYPE, " +
                    "$EXAM_PHASE, " +
                    "$EXAM_LOCATION, " +
                    "$EXAM_VOTES, " +
                    "$EXAM_TIMESTAMP " +
                    ") " +
                    "VALUES(:courseMiscUnitId, :exam.version, :exam.createdBy, " +
                    ":exam.sheet, :exam.dueDate, :exam.type, " +
                    ":exam.phase, :exam.location, :exam.votes, :exam.timestamp)"
    )
    @GetGeneratedKeys
    fun createExam(courseMiscUnitId: Int, exam: Exam): Exam

    @Transaction
    override fun createExamOnCourseInTerm(courseId: Int, termId: Int, exam: Exam): Exam {
        val courseDAO = createCourseDAO()
        if (!courseDAO.getSpecificTermOfCourse(courseId, termId).isPresent) {
            courseDAO.createCourseTerm(courseId, termId, Timestamp.valueOf(LocalDateTime.now()))
        }
        val courseMiscUnit = courseDAO.createCourseMiscUnit(courseId, termId, "Exam")
        return createExam(courseMiscUnit.courseMiscUnitId, exam)
    }

    @SqlQuery(
            "SELECT $EXAM_VOTES FROM $EXAM_TABLE " +
                    "WHERE $EXAM_ID = :examId"
    )
    override fun getVotesOnExam(examId: Int): Int

    @SqlUpdate(
            "UPDATE $EXAM_TABLE SET $EXAM_VOTES = :votes " +
                    "WHERE $EXAM_ID = :examId"
    )
    override fun updateVotesOnExam(examId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $EXAM_STAGE_TABLE"
    )
    override fun getAllStagedExams(): List<ExamStage>

    @SqlUpdate(
            "INSERT INTO $EXAM_STAGE_TABLE ( " +
                    "$EXAM_STAGE_ID, " +
                    "$EXAM_SHEET, " +
                    "$EXAM_DUE_DATE, " +
                    "$EXAM_TYPE, " +
                    "$EXAM_PHASE, " +
                    "$EXAM_LOCATION, " +
                    "$EXAM_CREATED_BY, " +
                    "$EXAM_VOTES, " +
                    "$EXAM_TIMESTAMP " +
                    ") " +
                    "values(:courseMiscUnitStageId, :examStage.sheet, :examStage.dueDate, " +
                    ":examStage.type, :examStage.phase, :examStage.location, " +
                    ":examStage.createdBy, :examStage.votes, :examStage.timestamp)"
    )
    @GetGeneratedKeys
    fun createStagingExam(courseMiscUnitStageId: Int, examStage: ExamStage): ExamStage

    @Transaction
    override fun createStagingExamOnCourseInTerm(courseId: Int, termId: Int, examStage: ExamStage): ExamStage {
        val courseDAO = createCourseDAO()
        if (!courseDAO.getSpecificTermOfCourse(courseId, termId).isPresent) {
            courseDAO.createCourseTerm(courseId, termId, Timestamp.valueOf(LocalDateTime.now()))
        }
        val courseMiscUnitStage = courseDAO.createStagingCourseMiscUnit(courseId, termId, "Exam")
        return createStagingExam(courseMiscUnitStage.stageId, examStage)
    }

    @SqlQuery(
            "SELECT $EXAM_VOTES FROM $EXAM_STAGE_TABLE " +
                    "WHERE $EXAM_STAGE_ID = :stageId"
    )
    override fun getVotesOnStagedExam(stageId: Int): Int

    @SqlUpdate(
            "UPDATE $EXAM_STAGE_TABLE SET $EXAM_VOTES = :votes " +
                    "WHERE $EXAM_STAGE_ID = :stageId"
    )
    override fun updateVotesOnStagedExam(stageId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $EXAM_VERSION_TABLE " +
                    "WHERE $EXAM_VERSION_ID = :examId " +
                    "AND $EXAM_VERSION = :version"
    )
    override fun getVersionExam(examId: Int, version: Int): Optional<ExamVersion>

    @SqlQuery(
            "SELECT * FROM $EXAM_VERSION_TABLE"
    )
    override fun getAllVersionExams(): List<ExamVersion>

    @SqlUpdate(
            "DELETE FROM $EXAM_VERSION_TABLE " +
                    "WHERE $EXAM_VERSION_ID = :examId " +
                    "AND $EXAM_VERSION = :version"
    )
    override fun deleteVersionOfExam(examId: Int, version: Int): Int

    @SqlUpdate(
            "INSERT INTO $EXAM_VERSION_TABLE ( " +
                    "$EXAM_VERSION_ID, " +
                    "$EXAM_VERSION, " +
                    "$EXAM_SHEET, " +
                    "$EXAM_DUE_DATE, " +
                    "$EXAM_TYPE, " +
                    "$EXAM_PHASE, " +
                    "$EXAM_LOCATION, " +
                    "$EXAM_CREATED_BY, " +
                    "$EXAM_TIMESTAMP " +
                    ") " +
                    "VALUES(:examVersion.examId, :examVersion.version, :examVersion.sheet, " +
                    ":examVersion.dueDate, :examVersion.type, :examVersion.phase, " +
                    ":examVersion.location, :examVersion.createdBy, :examVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createExamVersion(examVersion: ExamVersion): ExamVersion

    @SqlUpdate(
            "INSERT INTO $EXAM_REPORT_TABLE ( " +
                    "$EXAM_ID, " +
                    "$EXAM_SHEET, " +
                    "$EXAM_DUE_DATE, " +
                    "$EXAM_TYPE, " +
                    "$EXAM_PHASE, " +
                    "$EXAM_LOCATION, " +
                    "$EXAM_REPORTED_BY, " +
                    "$EXAM_VOTES, " +
                    "$EXAM_TIMESTAMP " +
                    ") " +
                    "VALUES(:examReport.examId, :examReport.sheet, :examReport.dueDate, " +
                    ":examReport.type, :examReport.phase, :examReport.location, " +
                    ":examReport.reportedBy, :examReport.votes, :examReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun reportExam(examReport: ExamReport): ExamReport

    @SqlUpdate(
            "DELETE FROM $EXAM_REPORT_TABLE " +
                    "WHERE $EXAM_ID = :examId " +
                    "AND $EXAM_REPORT_ID = :reportId"
    )
    override fun deleteReportOnExam(examId: Int, reportId: Int): Int

    @SqlUpdate(
            "DELETE FROM $EXAM_REPORT_TABLE " +
                    "WHERE $EXAM_ID = :examId"
    )
    override fun deleteAllReportsOnExam(examId: Int): Int

    @SqlQuery(
            "SELECT E.$EXAM_ID, " +
                    "E.$EXAM_VERSION, " +
                    "E.$EXAM_CREATED_BY, " +
                    "E.$EXAM_SHEET, " +
                    "E.$EXAM_DUE_DATE, " +
                    "E.$EXAM_TYPE, " +
                    "E.$EXAM_PHASE, " +
                    "E.$EXAM_LOCATION, " +
                    "E.$EXAM_VOTES, " +
                    "E.$EXAM_TIMESTAMP " +
                    "FROM $EXAM_TABLE AS E " +
                    "INNER JOIN $COURSE_MISC_UNIT_TABLE AS C " +
                    "ON E.$EXAM_ID = C.$COURSE_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId"
    )
    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    @SqlQuery(
            "SELECT E.$EXAM_ID, " +
                    "E:$EXAM_VERSION, " +
                    "E.$EXAM_CREATED_BY " +
                    "E.$EXAM_SHEET, " +
                    "E.$EXAM_DUE_DATE, " +
                    "E.$EXAM_TYPE, " +
                    "E.$EXAM_PHASE, " +
                    "E.$EXAM_LOCATION, " +
                    "E.$EXAM_VOTES," +
                    "E.$EXAM_TIMESTAMP, " +
                    "FROM $EXAM_TABLE AS E " +
                    "INNER JOIN $COURSE_MISC_UNIT_TABLE AS C " +
                    "ON E.$EXAM_ID = C.$COURSE_MISC_UNIT_ID" +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND C.$COURSE_MISC_UNIT_ID = :examId"
    )
    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam>

    @SqlQuery(
            "SELECT E.$EXAM_STAGE_ID, " +
                    "E.$EXAM_SHEET, " +
                    "E.$EXAM_DUE_DATE, " +
                    "E.$EXAM_TYPE, " +
                    "E.$EXAM_PHASE " +
                    "E.$EXAM_LOCATION, " +
                    "E.$EXAM_CREATED_BY " +
                    "E.$EXAM_VOTES, " +
                    "E.$EXAM_TIMESTAMP " +
                    "FROM $EXAM_STAGE_TABLE AS E " +
                    "INNER JOIN $COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON E.$EXAM_ID = C.$COURSE_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId"
    )
    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    @SqlQuery(
            "SELECT E.$EXAM_STAGE_ID, " +
                    "E.$EXAM_SHEET, " +
                    "E.$EXAM_DUE_DATE, " +
                    "E.$EXAM_TYPE, " +
                    "E.$EXAM_PHASE, " +
                    "E.$EXAM_LOCATION, " +
                    "E.$EXAM_CREATED_BY, " +
                    "E.$EXAM_VOTES, " +
                    "E.$EXAM_TIMESTAMP " +
                    "FROM $EXAM_STAGE_TABLE AS E " +
                    "INNER JOIN $COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON E.$EXAM_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND C.$COURSE_MISC_UNIT_STAGE_ID = :stageId"
    )
    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage>

    @SqlQuery(
            "SELECT * FROM $EXAM_REPORT_TABLE " +
                    "WHERE $EXAM_ID = :examId"
    )
    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport>

    @SqlQuery(
            "SELECT * FROM $EXAM_REPORT_TABLE " +
                    "WHERE $EXAM_REPORT_ID = :reportId"
    )
    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): Optional<ExamReport>

    @SqlQuery(
            "SELECT $EXAM_VOTES FROM $EXAM_REPORT_TABLE " +
                    "WHERE $EXAM_REPORT_ID = :reportId"
    )
    override fun getVotesOnReportedExam(reportId: Int): Int

    @SqlUpdate(
            "UPDATE $EXAM_REPORT_TABLE SET $EXAM_VOTES = :votes " +
                    "WHERE $EXAM_REPORT_ID = :reportId"
    )
    override fun updateVotesOnReportedExam(reportId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $EXAM_REPORT_TABLE " +
                    "WHERE $EXAM_ID = :examId " +
                    "AND $EXAM_REPORT_ID = :reportId"
    )
    override fun getSpecificReportOfExam(examId: Int, reportId: Int): Optional<ExamReport>

    @SqlQuery(
            "SELECT * FROM $EXAM_STAGE_TABLE " +
                    "WHERE $EXAM_STAGE_ID = :stageId"
    )
    override fun getExamSpecificStageEntry(stageId: Int): Optional<ExamStage>

    override fun deleteStagedExam(stageId: Int): Int =
            createCourseDAO().deleteSpecificStagedCourseMiscUnitEntry(stageId)

    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            createCourseDAO().deleteAllCourseMiscUnitsFromTypeOfCourseInTerm(courseId, termId, "Exam")

    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            createCourseDAO().deleteAllStagedCourseMiscUnitsFromTypeOfCourseInTerm(courseId, termId, "Exam")

    @SqlUpdate(
            "DELETE FROM $EXAM_VERSION_TABLE " +
                    "WHERE $EXAM_VERSION_ID = :examId"
    )
    override fun deleteAllVersionOfExam(examId: Int): Int

    @SqlQuery(
            "SELECT * FROM $EXAM_VERSION_TABLE " +
                    "WHERE $EXAM_VERSION_ID = :examId"
    )
    override fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion>

    @SqlQuery(
            "SELECT * FROM $EXAM_VERSION_TABLE " +
                    "WHERE $EXAM_VERSION_ID = :examId " +
                    "AND $EXAM_VERSION = :versionId"
    )
    override fun getVersionOfSpecificExam(examId: Int, version: Int): Optional<ExamVersion>

}