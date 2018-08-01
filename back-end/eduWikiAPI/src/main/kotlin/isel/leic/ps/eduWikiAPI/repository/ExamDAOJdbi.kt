package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
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

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_ID = :examId"
    )
    override fun deleteSpecificExamOfCourseInTerm(examId: Int): Int

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
    override fun updateExam(examId: Int, exam: Exam) : Exam

    @SqlUpdate()
    override fun createExam(courseId: Int, termId: Int, exam: Exam): Exam {
        if (!hANDle.isInTransaction) hANDle.begin()
        val courseMiscUnit = hANDle.createUpdate(
                "insert into $COURSE_MISC_UNIT_TABLE (" +
                        "${CourseDAOJdbi.COURSE_MISC_UNIT_TYPE}, " +
                        "$COURSE_MISC_UNIT_COURSE_ID, " +
                        "$COURSE_MISC_UNIT_TERM_ID " +
                        ") " +
                        "values(:type, :courseId, :termId)"
        )
                .bind("type", exam.type)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()
        val exam = hANDle.createUpdate(
                "insert into $EXAM_TABLE (" +
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
                        "values(:examId, :version, :createdBy, :sheet, :dueDate, :type, " +
                        ":phase, :location, :votes, :timestamp)"
        )
                .bind("examId", courseMiscUnit.courseMiscUnitId)
                .bind("examVersion", exam.version)
                .bind("createdBy", exam.createdBy)
                .bind("sheet", exam.sheet)
                .bind("dueDate", exam.dueDate)
                .bind("type", exam.type)
                .bind("phase", exam.phase)
                .bind("location", exam.location)
                .bind("votes", exam.votes)
                .bind("timestamp", exam.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(Exam::class.java)
                .findFirst()
        if (!hANDle.isInTransaction) hANDle.commit()
        return exam
    }

    override fun voteOnExam(examId: Int, vote: Vote): Int {
        var votes = hANDle.createQuery(
                "SELECT $EXAM_VOTES FROM $EXAM_TABLE " +
                        "WHERE $EXAM_ID = :examId"
        )
                .bind("examId", examId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return hANDle.createUpdate(
                "update $EXAM_TABLE set $EXAM_VOTES = :votes " +
                        "WHERE $EXAM_ID = :examId")
                .bind("votes", votes)
                .bind("examId", examId)
                .execute()
    }

    @SqlQuery(
            "SELECT * FROM $EXAM_STAGE_TABLE"
    )
    override fun getAllExamStages(): List<ExamStage>

    @SqlUpdate(

    )
    override fun createStagingExam(courseId: Int, termId: Int, examStage: ExamStage): ExamStage {
        hANDle.begin()
        val courseMiscUnitStage = hANDle.createUpdate(
                "insert into $COURSE_MISC_UNIT_STAGE_TABLE (" +
                        "$COURSE_MISC_UNIT_COURSE_ID, " +
                        "$COURSE_MISC_UNIT_TERM_ID, " +
                        "${CourseDAOJdbi.COURSE_MISC_UNIT_TYPE} " +
                        ") " +
                        "values(:courseId, :termId, :miscType:)"
        )
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("miscType", examStage.type)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnitStage::class.java)
                .findOnly()

        val examStage = hANDle.createUpdate(
                "insert into $EXAM_STAGE_TABLE(" +
                        "$EXAM_STAGE_ID, " +
                        "$EXAM_SHEET, " +
                        "$EXAM_DUE_DATE, " +
                        "$EXAM_TYPE, " +
                        "$EXAM_PHASE, " +
                        "$EXAM_LOCATION, " +
                        "$EXAM_CREATED_BY, " +
                        "$EXAM_VOTES, " +
                        "$EXAM_TIMESTAMP) " +
                        "values(:stageId, :sheet, :dueDate, :type, " +
                        ":phase, :location, :createdBy, :votes, :timestamp)"
        )
                .bind("stageId", courseMiscUnitStage.stageId)
                .bind("sheet", examStage.sheet)
                .bind("dueDate", examStage.dueDate)
                .bind("type", examStage.type)
                .bind("phase", examStage.phase)
                .bind("location", examStage.location)
                .bind("createdBy", examStage.createdBy)
                .bind("votes", examStage.votes)
                .bind("timestamp", examStage.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(ExamStage::class.java)
                .findFirst()
        hANDle.commit()
        return examStage
    }

    override fun voteOnStagedExam(stageId: Int, vote: Vote): Int {
        var votes = hANDle.createQuery(
                "SELECT $EXAM_VOTES FROM $EXAM_STAGE_TABLE " +
                        "WHERE $EXAM_STAGE_ID = :stageId"
        )
                .bind("stageId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return hANDle.createUpdate(
                "update $EXAM_STAGE_TABLE set $EXAM_VOTES = :votes " +
                        "WHERE $EXAM_STAGE_ID = :stageId")
                .bind("votes", votes)
                .bind("stageId", stageId)
                .execute()
    }

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
    override fun createVersionExam(examVersion: ExamVersion): ExamVersion

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
                    ":examReport.type, :examReport.phase, :examReport.location, :examReport.reportedBy, " +
                    ":examReport.votes, :examReport.timestamp)"
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

    override fun voteOnReportToExamOnCourseInTerm(reportId: Int, vote: Vote): Int {
        var votes = hANDle.createQuery(
                "SELECT $EXAM_VOTES FROM $EXAM_REPORT_TABLE " +
                        "WHERE $EXAM_REPORT_ID = :reportId"
        )
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return hANDle.createUpdate(
                "update $EXAM_REPORT_TABLE set $EXAM_VOTES = :votes " +
                        "WHERE $EXAM_REPORT_ID = :reportId"
        )
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

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

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_STAGE_TABLE" +
                    "WHERE $COURSE_MISC_UNIT_STAGE_ID = :stageId"
    )
    override fun deleteStagedExam(stageId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND $COURSE_MISC_UNIT_TERM_ID = :termId "
    )
    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_STAGE_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_ID = :courseId " +
                    "AND $COURSE_MISC_UNIT_TERM_ID = :termId"
    )
    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int

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