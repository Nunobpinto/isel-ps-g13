package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.CourseMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Repository
class ExamDAOImpl : ExamDAO {

    companion object {
        //TABLE NAMES
        const val EXAM_TABLE = "exam"
        const val EXAM_VERSION_TABLE = "exam_version"
        const val EXAM_REPORT_TABLE = "exam_report"
        const val EXAM_STAGE_TABLE = "exam_stage"

        // EXAM FIELDS
        const val EXAM_ID = "exam_id"
        const val EXAM_VERSION = "exam_version"
        const val EXAM_SHEET_ID = "sheet_id"
        const val EXAM_DUE_DATE = "due_date"
        const val EXAM_TYPE = "exam_type"
        const val EXAM_PHASE = "phase"
        const val EXAM_LOCATION = "location"
        const val EXAM_VOTES = "votes"
        const val EXAM_TIMESTAMP = "time_stamp"
        const val EXAM_CREATED_BY = "created_by"
        const val EXAM_LOG_ID = "log_id"

        // EXAM STAGE FIELDS
        const val EXAM_STAGE_ID = "exam_stage_id"
        const val EXAM_STAGE_CREATED_BY = "created_by"
        const val EXAM_STAGE_SHEET_ID = "sheet_id"
        const val EXAM_STAGE_DUE_DATE = "due_date"
        const val EXAM_STAGE_TYPE = "exam_type"
        const val EXAM_STAGE_PHASE = "phase"
        const val EXAM_STAGE_LOCATION = "location"
        const val EXAM_STAGE_VOTES = "votes"
        const val EXAM_STAGE_TIMESTAMP = "time_stamp"
        const val EXAM_STAGE_LOG_ID = "log_id"

        // EXAM REPORT FIELDS
        const val EXAM_REPORT_ID = "exam_report_id"
        const val EXAM_REPORT_EXAM_ID = "exam_id"
        const val EXAM_REPORTED_BY = "reported_by"
        const val EXAM_REPORT_SHEET_ID = "sheet_id"
        const val EXAM_REPORT_DUE_DATE = "due_date"
        const val EXAM_REPORT_TYPE = "exam_type"
        const val EXAM_REPORT_PHASE = "phase"
        const val EXAM_REPORT_LOCATION = "location"
        const val EXAM_REPORT_VOTES = "votes"
        const val EXAM_REPORT_TIMESTAMP = "time_stamp"
        const val EXAM_REPORT_LOG_ID = "log_id"

        // EXAM VERSION FIELDS
        const val EXAM_VERSION_EXAM_ID = "exam_id"
        const val EXAM_VERSION_ID = "exam_version"
        const val EXAM_VERSION_SHEET_ID = "sheet_id"
        const val EXAM_VERSION_DUE_DATE = "due_date"
        const val EXAM_VERSION_TYPE = "exam_type"
        const val EXAM_VERSION_PHASE = "phase"
        const val EXAM_VERSION_LOCATION = "location"
        const val EXAM_VERSION_TIMESTAMP = "time_stamp"
        const val EXAM_VERSION_CREATED_BY = "created_by"

    }

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getAllExamsFromSpecificTermOfCourse(courseId, termId)

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)

    override fun createExamOnCourseInTerm(courseId: Int, termId: Int, exam: Exam): Exam =
            jdbi.open().attach(ExamDAOJdbi::class.java).createExamOnCourseInTerm(courseId, termId, exam)

    override fun updateExam(examId: Int, exam: Exam): Exam =
            jdbi.open().attach(ExamDAOJdbi::class.java).updateExam(examId, exam)

    override fun updateVotesOnExam(examId: Int, votes: Int): Int =
            jdbi.open().attach(ExamDAOJdbi::class.java).updateVotesOnExam(examId, votes)

    override fun deleteSpecificExamOfCourseInTerm(termId: Int, courseId: Int, examId: Int): Int =
            jdbi.open().attach(ExamDAOJdbi::class.java).deleteSpecificExamOfCourseInTerm(termId, courseId, examId)

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun createStagingExamOnCourseInTerm(courseId: Int, termId: Int, examStage: ExamStage): ExamStage =
            jdbi.open().attach(ExamDAOJdbi::class.java).createStagingExamOnCourseInTerm(courseId, termId, examStage)

    override fun updateVotesOnStagedExam(stageId: Int, votes: Int): Int =
            jdbi.open().attach(ExamDAOJdbi::class.java).updateVotesOnStagedExam(stageId, votes)

    override fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int =
            jdbi.open().attach(ExamDAOJdbi::class.java).deleteSpecificStagedExamOfCourseInTerm(courseId, termId, stageId)

    override fun getAllVersionsOfSpecificExam(termId: Int, courseId: Int, examId: Int): List<ExamVersion> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getAllVersionsOfSpecificExam(termId, courseId, examId)

    override fun getVersionOfSpecificExam(termId: Int, courseId: Int, examId: Int, version: Int): Optional<ExamVersion> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getVersionOfSpecificExam(termId, courseId, examId, version)

    override fun createExamVersion(examVersion: ExamVersion): ExamVersion =
            jdbi.open().attach(ExamDAOJdbi::class.java).createExamVersion(examVersion)

    override fun getAllReportsOnExamOnSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): List<ExamReport> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getAllReportsOnExamOnSpecificTermOfCourse(courseId, termId, examId)

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int, reportId: Int): Optional<ExamReport> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)

    override fun reportExam(examReport: ExamReport): ExamReport =
            jdbi.open().attach(ExamDAOJdbi::class.java).reportExam(examReport)

    override fun updateVotesOnReportedExam(reportId: Int, votes: Int): Int =
            jdbi.open().attach(ExamDAOJdbi::class.java).updateVotesOnReportedExam(reportId, votes)

    override fun deleteReportOnExam(courseId: Int, termId: Int, examId: Int, reportId: Int): Int =
            jdbi.open().attach(ExamDAOJdbi::class.java).deleteReportOnExam(courseId, termId, examId, reportId)

    override fun getExamByLogId(logId: Int): Optional<Exam> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getExamByLogId(logId)

    override fun getExamStageByLogId(logId: Int): Optional<ExamStage> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getExamStageByLogId(logId)

    override fun getExamReportByLogId(logId: Int): Optional<ExamReport> =
            jdbi.open().attach(ExamDAOJdbi::class.java).getExamReportByLogId(logId)

    interface ExamDAOJdbi : ExamDAO {

        @CreateSqlObject
        fun createCourseDAO(): CourseDAOImpl.CourseDAOJdbi

        override fun deleteSpecificExamOfCourseInTerm(termId: Int, courseId: Int, examId: Int): Int =
                createCourseDAO().deleteSpecificCourseMiscUnitEntry(courseId, termId, examId)

        @SqlUpdate(
                "update $EXAM_TABLE SET " +
                        "$EXAM_VERSION = :exam.version, " +
                        "$EXAM_CREATED_BY = :exam.createdBy, " +
                        "$EXAM_SHEET_ID = :exam.sheetId, " +
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
                "INSERT INTO :schema.$EXAM_TABLE ( " +
                        "$EXAM_ID, " +
                        "$EXAM_VERSION, " +
                        "$EXAM_CREATED_BY, " +
                        "$EXAM_SHEET_ID, " +
                        "$EXAM_DUE_DATE, " +
                        "$EXAM_TYPE, " +
                        "$EXAM_PHASE, " +
                        "$EXAM_LOCATION, " +
                        "$EXAM_VOTES, " +
                        "$EXAM_TIMESTAMP " +
                        ") " +
                        "VALUES(:courseMiscUnitId, :exam.version, :exam.createdBy, " +
                        ":exam.sheetId, :exam.dueDate, :exam.type, " +
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
            val courseMiscUnit = courseDAO.createCourseMiscUnit(courseId, termId, CourseMiscUnitType.EXAM_TEST)
            return createExam(courseMiscUnit.courseMiscUnitId, exam)
        }

        @SqlUpdate(
                "UPDATE :schema.$EXAM_TABLE SET $EXAM_VOTES = :votes " +
                        "WHERE $EXAM_ID = :examId"
        )
        override fun updateVotesOnExam(examId: Int, votes: Int): Int

        @SqlUpdate(
                "INSERT INTO :schema.$EXAM_STAGE_TABLE ( " +
                        "$EXAM_STAGE_ID, " +
                        "$EXAM_STAGE_SHEET_ID, " +
                        "$EXAM_STAGE_DUE_DATE, " +
                        "$EXAM_STAGE_TYPE, " +
                        "$EXAM_STAGE_PHASE, " +
                        "$EXAM_STAGE_LOCATION, " +
                        "$EXAM_STAGE_CREATED_BY, " +
                        "$EXAM_STAGE_VOTES, " +
                        "$EXAM_STAGE_TIMESTAMP " +
                        ") " +
                        "values(:courseMiscUnitStageId, :examStage.sheetId, :examStage.dueDate, " +
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
            val courseMiscUnitStage = courseDAO.createStagingCourseMiscUnit(courseId, termId, CourseMiscUnitType.EXAM_TEST)
            return createStagingExam(courseMiscUnitStage.stageId, examStage)
        }

        @SqlUpdate(
                "UPDATE :schema.$EXAM_STAGE_TABLE SET $EXAM_STAGE_VOTES = :votes " +
                        "WHERE $EXAM_STAGE_ID = :stageId"
        )
        override fun updateVotesOnStagedExam(stageId: Int, votes: Int): Int

        @SqlUpdate(
                "INSERT INTO :schema.$EXAM_VERSION_TABLE ( " +
                        "$EXAM_VERSION_EXAM_ID, " +
                        "$EXAM_VERSION_ID, " +
                        "$EXAM_VERSION_SHEET_ID, " +
                        "$EXAM_VERSION_DUE_DATE, " +
                        "$EXAM_VERSION_TYPE, " +
                        "$EXAM_VERSION_PHASE, " +
                        "$EXAM_VERSION_LOCATION, " +
                        "$EXAM_VERSION_CREATED_BY, " +
                        "$EXAM_VERSION_TIMESTAMP " +
                        ") " +
                        "VALUES(:examVersion.examId, :examVersion.version, :examVersion.sheetId, " +
                        ":examVersion.dueDate, :examVersion.type, :examVersion.phase, " +
                        ":examVersion.location, :examVersion.createdBy, :examVersion.timestamp)"
        )
        @GetGeneratedKeys
        override fun createExamVersion(examVersion: ExamVersion): ExamVersion

        @SqlUpdate(
                "INSERT INTO :schema.$EXAM_REPORT_TABLE ( " +
                        "$EXAM_REPORT_EXAM_ID, " +
                        "$EXAM_REPORT_SHEET_ID, " +
                        "$EXAM_REPORT_DUE_DATE, " +
                        "$EXAM_REPORT_TYPE, " +
                        "$EXAM_REPORT_PHASE, " +
                        "$EXAM_REPORT_LOCATION, " +
                        "$EXAM_REPORTED_BY, " +
                        "$EXAM_REPORT_VOTES, " +
                        "$EXAM_REPORT_TIMESTAMP " +
                        ") " +
                        "VALUES(:examReport.examId, :examReport.sheetId, :examReport.dueDate, " +
                        ":examReport.type, :examReport.phase, :examReport.location, " +
                        ":examReport.reportedBy, :examReport.votes, :examReport.timestamp)"
        )
        @GetGeneratedKeys
        override fun reportExam(examReport: ExamReport): ExamReport

        @SqlUpdate(
                "DELETE FROM :schema.$EXAM_REPORT_TABLE AS E " +
                        "USING $COURSE_MISC_UNIT_TABLE AS C " +
                        "WHERE E.$EXAM_REPORT_EXAM_ID = C.$COURSE_MISC_UNIT_ID AND " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND " +
                        "C.$COURSE_MISC_UNIT_TERM_ID = :termId AND " +
                        "C.$COURSE_MISC_UNIT_ID = :examId AND " +
                        "E.$EXAM_REPORT_ID = :reportId"
        )
        override fun deleteReportOnExam(courseId: Int, termId: Int, examId: Int, reportId: Int): Int

        @SqlQuery(
                "SELECT E.$EXAM_ID, " +
                        "E.$EXAM_VERSION, " +
                        "E.$EXAM_CREATED_BY, " +
                        "E.$EXAM_SHEET_ID, " +
                        "E.$EXAM_DUE_DATE, " +
                        "E.$EXAM_TYPE, " +
                        "E.$EXAM_PHASE, " +
                        "E.$EXAM_LOCATION, " +
                        "E.$EXAM_VOTES, " +
                        "E.$EXAM_TIMESTAMP, " +
                        "E.$EXAM_LOG_ID, " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_TERM_ID " +
                        "FROM :schema.$EXAM_TABLE AS E " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_TABLE AS C " +
                        "ON E.$EXAM_ID = C.$COURSE_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                        "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId"
        )
        override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

        @SqlQuery(
                "SELECT E.$EXAM_ID, " +
                        "E:$EXAM_VERSION, " +
                        "E.$EXAM_CREATED_BY " +
                        "E.$EXAM_SHEET_ID, " +
                        "E.$EXAM_DUE_DATE, " +
                        "E.$EXAM_TYPE, " +
                        "E.$EXAM_PHASE, " +
                        "E.$EXAM_LOCATION, " +
                        "E.$EXAM_VOTES," +
                        "E.$EXAM_TIMESTAMP, " +
                        "E.$EXAM_LOG_ID, " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_TERM_ID " +
                        "FROM :schema.$EXAM_TABLE AS E " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_TABLE AS C " +
                        "ON E.$EXAM_ID = C.$COURSE_MISC_UNIT_ID" +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                        "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                        "AND C.$COURSE_MISC_UNIT_ID = :examId"
        )
        override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam>

        @SqlQuery(
                "SELECT E.$EXAM_STAGE_ID, " +
                        "E.$EXAM_STAGE_SHEET_ID, " +
                        "E.$EXAM_STAGE_DUE_DATE, " +
                        "E.$EXAM_STAGE_TYPE, " +
                        "E.$EXAM_STAGE_PHASE " +
                        "E.$EXAM_STAGE_LOCATION, " +
                        "E.$EXAM_STAGE_CREATED_BY " +
                        "E.$EXAM_STAGE_VOTES, " +
                        "E.$EXAM_STAGE_TIMESTAMP, " +
                        "E.$EXAM_STAGE_LOG_ID, " +
                        "C.$COURSE_MISC_UNIT_STAGE_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_STAGE_TERM_ID " +
                        "FROM :schema.$EXAM_STAGE_TABLE AS E " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON E.$EXAM_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_STAGE_COURSE_ID = :courseId " +
                        "AND C.$COURSE_MISC_UNIT_STAGE_TERM_ID = :termId"
        )
        override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

        @SqlQuery(
                "SELECT E.$EXAM_STAGE_ID, " +
                        "E.$EXAM_STAGE_SHEET_ID, " +
                        "E.$EXAM_STAGE_DUE_DATE, " +
                        "E.$EXAM_STAGE_TYPE, " +
                        "E.$EXAM_STAGE_PHASE " +
                        "E.$EXAM_STAGE_LOCATION, " +
                        "E.$EXAM_STAGE_CREATED_BY " +
                        "E.$EXAM_STAGE_VOTES, " +
                        "E.$EXAM_STAGE_TIMESTAMP, " +
                        "E.$EXAM_STAGE_LOG_ID, " +
                        "C.$COURSE_MISC_UNIT_STAGE_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_STAGE_TERM_ID " +
                        "FROM :schema.$EXAM_STAGE_TABLE AS E " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON E.$EXAM_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_STAGE_COURSE_ID = :courseId " +
                        "AND C.$COURSE_MISC_UNIT_STAGE_ID = :stageId"
        )
        override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage>

        @SqlQuery(
                "SELECT " +
                        "E.$EXAM_REPORT_ID, " +
                        "E.$EXAM_REPORT_EXAM_ID, " +
                        "E.$EXAM_REPORT_SHEET_ID, " +
                        "E.$EXAM_REPORT_DUE_DATE, " +
                        "E.$EXAM_REPORT_TYPE, " +
                        "E.$EXAM_REPORT_PHASE, " +
                        "E.$EXAM_REPORT_LOCATION, " +
                        "E.$EXAM_REPORTED_BY, " +
                        "E.$EXAM_REPORT_VOTES, " +
                        "E.$EXAM_REPORT_TIMESTAMP, " +
                        "E.$EXAM_REPORT_LOG_ID, " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_TERM_ID " +
                        "FROM :schema.$COURSE_MISC_UNIT_TABLE as C " +
                        "INNER JOIN :schema.$EXAM_REPORT_TABLE as E ON c.$COURSE_MISC_UNIT_ID = E.$EXAM_REPORT_EXAM_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND C.$COURSE_MISC_UNIT_TERM_ID = :termId AND " +
                        "C.$COURSE_MISC_UNIT_ID = :examId"
        )
        override fun getAllReportsOnExamOnSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): List<ExamReport>

        @SqlQuery(
                "SELECT " +
                        "E.$EXAM_REPORT_ID, " +
                        "E.$EXAM_REPORT_EXAM_ID, " +
                        "E.$EXAM_REPORT_SHEET_ID, " +
                        "E.$EXAM_REPORT_DUE_DATE, " +
                        "E.$EXAM_REPORT_TYPE, " +
                        "E.$EXAM_REPORT_PHASE, " +
                        "E.$EXAM_REPORT_LOCATION, " +
                        "E.$EXAM_REPORTED_BY, " +
                        "E.$EXAM_REPORT_VOTES, " +
                        "E.$EXAM_REPORT_TIMESTAMP, " +
                        "E.$EXAM_REPORT_LOG_ID " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_TERM_ID " +
                        "FROM :schema.$COURSE_MISC_UNIT_TABLE as C " +
                        "INNER JOIN :schema.$EXAM_REPORT_TABLE as E ON C.$COURSE_MISC_UNIT_ID = E.$EXAM_REPORT_EXAM_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND C.$COURSE_MISC_UNIT_TERM_ID = :termId AND " +
                        "C.$COURSE_MISC_UNIT_ID = :examId AND E.$EXAM_REPORT_ID = :reportId"
        )
        override fun getSpecificReportOnExamOnSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int, reportId: Int): Optional<ExamReport>

        @SqlUpdate(
                "UPDATE :schema.$EXAM_REPORT_TABLE SET $EXAM_REPORT_VOTES = :votes " +
                        "WHERE $EXAM_REPORT_ID = :reportId"
        )
        override fun updateVotesOnReportedExam(reportId: Int, votes: Int): Int

        override fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int =
                createCourseDAO().deleteSpecificStagedCourseMiscUnitEntry(courseId, termId, stageId)

        @SqlQuery(
                "SELECT " +
                        "V.$EXAM_VERSION_ID, " +
                        "V.$EXAM_VERSION_EXAM_ID, " +
                        "V.$EXAM_VERSION_SHEET_ID, " +
                        "V.$EXAM_VERSION_DUE_DATE, " +
                        "V.$EXAM_VERSION_TYPE, " +
                        "V.$EXAM_VERSION_PHASE, " +
                        "V.$EXAM_VERSION_LOCATION, " +
                        "V.$EXAM_VERSION_CREATED_BY, " +
                        "V.$EXAM_VERSION_TIMESTAMP " +
                        "FROM :schema.$EXAM_VERSION_TABLE as V " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_TABLE as C ON V.$EXAM_VERSION_EXAM_ID = C.$COURSE_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND C.$COURSE_MISC_UNIT_TERM_ID = :termId AND C.$COURSE_MISC_UNIT_ID = :examId"
        )
        override fun getAllVersionsOfSpecificExam(termId: Int, courseId: Int, examId: Int): List<ExamVersion>

        @SqlQuery(
                "SELECT " +
                        "V.$EXAM_VERSION_ID, " +
                        "V.$EXAM_VERSION_EXAM_ID, " +
                        "V.$EXAM_VERSION_SHEET_ID, " +
                        "V.$EXAM_VERSION_DUE_DATE, " +
                        "V.$EXAM_VERSION_TYPE, " +
                        "V.$EXAM_VERSION_PHASE, " +
                        "V.$EXAM_VERSION_LOCATION, " +
                        "V.$EXAM_VERSION_CREATED_BY, " +
                        "V.$EXAM_VERSION_TIMESTAMP " +
                        "FROM :schema.$EXAM_VERSION_TABLE as V " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_TABLE as C ON V.$EXAM_VERSION_EXAM_ID = C.$COURSE_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND C.$COURSE_MISC_UNIT_TERM_ID = :termId AND " +
                        "C.$COURSE_MISC_UNIT_ID = :examId AND V.$EXAM_VERSION_ID = :version"
        )
        override fun getVersionOfSpecificExam(termId: Int, courseId: Int, examId: Int, version: Int): Optional<ExamVersion>

        @SqlQuery(
                "SELECT E.$EXAM_ID, " +
                        "E:$EXAM_VERSION, " +
                        "E.$EXAM_CREATED_BY " +
                        "E.$EXAM_SHEET_ID, " +
                        "E.$EXAM_DUE_DATE, " +
                        "E.$EXAM_TYPE, " +
                        "E.$EXAM_PHASE, " +
                        "E.$EXAM_LOCATION, " +
                        "E.$EXAM_VOTES," +
                        "E.$EXAM_TIMESTAMP, " +
                        "E.$EXAM_LOG_ID, " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_TERM_ID " +
                        "FROM :schema.$EXAM_TABLE AS E " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_TABLE AS C " +
                        "ON E.$EXAM_ID = C.$COURSE_MISC_UNIT_ID" +
                        "WHERE E.$EXAM_LOG_ID = :logId"
        )
        override fun getExamByLogId(logId: Int): Optional<Exam>

        @SqlQuery(
                "SELECT " +
                        "E.$EXAM_REPORT_ID, " +
                        "E.$EXAM_REPORT_EXAM_ID, " +
                        "E.$EXAM_REPORT_SHEET_ID, " +
                        "E.$EXAM_REPORT_DUE_DATE, " +
                        "E.$EXAM_REPORT_TYPE, " +
                        "E.$EXAM_REPORT_PHASE, " +
                        "E.$EXAM_REPORT_LOCATION, " +
                        "E.$EXAM_REPORTED_BY, " +
                        "E.$EXAM_REPORT_VOTES, " +
                        "E.$EXAM_REPORT_TIMESTAMP, " +
                        "E.$EXAM_REPORT_LOG_ID " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_TERM_ID " +
                        "FROM :schema.$COURSE_MISC_UNIT_TABLE as C " +
                        "INNER JOIN :schema.$EXAM_REPORT_TABLE as E " +
                        "ON C.$COURSE_MISC_UNIT_ID = E.$EXAM_REPORT_EXAM_ID " +
                        "WHERE E.$EXAM_REPORT_LOG_ID = :logId"
        )
        override fun getExamReportByLogId(logId: Int): Optional<ExamReport>

        @SqlQuery(
                "SELECT E.$EXAM_STAGE_ID, " +
                        "E.$EXAM_STAGE_SHEET_ID, " +
                        "E.$EXAM_STAGE_DUE_DATE, " +
                        "E.$EXAM_STAGE_TYPE, " +
                        "E.$EXAM_STAGE_PHASE " +
                        "E.$EXAM_STAGE_LOCATION, " +
                        "E.$EXAM_STAGE_CREATED_BY " +
                        "E.$EXAM_STAGE_VOTES, " +
                        "E.$EXAM_STAGE_TIMESTAMP, " +
                        "E.$EXAM_STAGE_LOG_ID, " +
                        "C.$COURSE_MISC_UNIT_STAGE_COURSE_ID, " +
                        "C.$COURSE_MISC_UNIT_STAGE_TERM_ID " +
                        "FROM :schema.$EXAM_STAGE_TABLE AS E " +
                        "INNER JOIN :schema.$COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON E.$EXAM_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                        "WHERE E.$EXAM_STAGE_LOG_ID = :logId"
        )
        override fun getExamStageByLogId(logId: Int): Optional<ExamStage>

    }

}