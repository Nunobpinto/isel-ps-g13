package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_TYPE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExamDAOImpl : ExamDAO {

    companion object {
        //TABLE NAMES
        const val EXAM_TABLE = "exam"
        const val EXAM_VERSION_TABLE = "exam_version"
        const val EXAM_REPORT_TABLE = "exam_report"
        const val EXAM_STAGE_TABLE = "exam_stage"
        // FIELDS
        const val EXAM_VERSION = "exam_version"
        const val EXAM_SHEET = "sheet"
        const val EXAM_DUE_DATE = "due_date"
        const val EXAM_TYPE = "exam_type"
        const val EXAM_PHASE = "phase"
        const val EXAM_LOCATION = "location"
        const val EXAM_VOTES = "votes"
        const val EXAM_TIMESTAMP = "time_stamp"
        const val EXAM_REPORT_ID = "report_id"
        const val EXAM_STAGE_ID = "id"
        const val EXAM_VERSION_ID = "id"
        const val EXAM_REPORTED_BY = "reported_by"
        const val EXAM_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var handle: Handle

    override fun deleteSpecificExamOfCourseInTerm(courseMiscUnitId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_MISC_UNIT_TABLE where $COURSE_MISC_UNIT_ID = :organization_id"
            )
                    .bind("organization_id", courseMiscUnitId)
                    .execute()

    override fun updateExam(examId: Int, exam: Exam) =
            handle.createUpdate(
                    "update $EXAM_TABLE SET " +
                            "$EXAM_VERSION = :version, $EXAM_CREATED_BY = :createdBy, " +
                            "$EXAM_SHEET = :sheet, $EXAM_DUE_DATE = :dueDate, " +
                            "$EXAM_TYPE = :type::exam_type, $EXAM_PHASE = :phase, " +
                            "$EXAM_LOCATION = :location, $EXAM_VOTES = :votes, $EXAM_TIMESTAMP = :timestamp " +
                            "where $COURSE_MISC_UNIT_ID = :examId"
            )
                    .bind("version", exam.version)
                    .bind("createdBy", exam.createdBy)
                    .bind("sheet", exam.sheet)
                    .bind("dueDate", exam.dueDate)
                    .bind("type", exam.type)
                    .bind("phase", exam.phase)
                    .bind("location", exam.location)
                    .bind("votes", exam.votes)
                    .bind("timestamp", exam.timestamp)
                    .bind("examId", examId)
                    .execute()

    override fun createExam(courseId: Int, termId: Int, exam: Exam): Optional<Exam> {
        if(!handle.isInTransaction) handle.begin()
        val courseMiscUnit = handle.createUpdate(
                "insert into $COURSE_MISC_UNIT_TABLE " +
                        "($COURSE_MISC_TYPE, $COURSE_ID, $TERM_ID) " +
                        "values(:miscType::course_misc_unit_type, :courseId, :termId)"
        )
                .bind("miscType", "Exam/Test")
                .bind("courseId", courseId)
                .bind("termId", termId)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()
        val res = handle.createUpdate(
                "insert into $EXAM_TABLE " +
                        "($COURSE_MISC_UNIT_ID, $EXAM_CREATED_BY, " +
                        "$EXAM_SHEET, $EXAM_DUE_DATE, $EXAM_TYPE, $EXAM_PHASE, $EXAM_LOCATION, " +
                        "$EXAM_VOTES, $EXAM_TIMESTAMP) " +
                        "values(:courseMiscUnitId, :createdBy, :sheet, :dueDate, :type::exam_type, " +
                        ":phase, :location, :votes, :timestamp)"
        )
                .bind("courseMiscUnitId", courseMiscUnit.id)
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
        if(!handle.isInTransaction) handle.commit()
        return res
    }

    override fun voteOnExam(courseMiscUnitId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $EXAM_VOTES from $EXAM_TABLE where $COURSE_MISC_UNIT_ID = :examId")
                .bind("examId", courseMiscUnitId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate("update $EXAM_TABLE set $EXAM_VOTES = :votes where $COURSE_MISC_UNIT_ID = :examId")
                .bind("votes", votes)
                .bind("examId", courseMiscUnitId)
                .execute()
    }

    override fun getAllExamStages() =
            handle.createQuery("select * from $EXAM_STAGE_TABLE")
                    .mapTo(ExamStage::class.java)
                    .list()

    override fun createStagingExam(courseId: Int, termId: Int, examStage: ExamStage): Optional<ExamStage> {
        handle.begin()
        val courseMiscUnitStage = handle.createUpdate(
                "insert into $COURSE_MISC_UNIT_STAGE_TABLE " +
                        "($COURSE_ID, $TERM_ID, $COURSE_MISC_TYPE) " +
                        "values(:courseId, :termId, :miscType::course_misc_unit_type)"
        )
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("miscType", "Exam/Test")
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnitStage::class.java)
                .findOnly()

        val res = handle.createUpdate(
                "insert into $EXAM_STAGE_TABLE " +
                        "($COURSE_MISC_UNIT_ID, $EXAM_SHEET, $EXAM_DUE_DATE, " +
                        "$EXAM_TYPE, $EXAM_PHASE, $EXAM_LOCATION, $EXAM_CREATED_BY, " +
                        "$EXAM_VOTES, $EXAM_TIMESTAMP) " +
                        "values(:courseMiscUnitId, :sheet, :dueDate, :type::exam_type, :phase, :location, " +
                        ":createdBy, :votes, :timestamp)"
        )
                .bind("courseMiscUnitId", courseMiscUnitStage.id)
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
        handle.commit()
        return res
    }

    override fun voteOnStagedExam(stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $EXAM_VOTES from $EXAM_STAGE_TABLE where $COURSE_MISC_UNIT_ID = :examId")
                .bind("examId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $EXAM_STAGE_TABLE set $EXAM_VOTES = :votes where $COURSE_MISC_UNIT_ID = :examId")
                .bind("votes", votes)
                .bind("examId", stageId)
                .execute()
    }

    override fun getVersionExam(versionExamId: Int, version: Int) =
            handle.createQuery("select * from $EXAM_VERSION_TABLE where $EXAM_VERSION_ID = :examId and $EXAM_VERSION = :version")
                    .bind("examId", versionExamId)
                    .bind("version", version)
                    .mapTo(ExamVersion::class.java)
                    .findFirst()

    override fun getAllVersionExams() =
            handle.createQuery("select * from $EXAM_VERSION_TABLE")
                    .mapTo(ExamVersion::class.java)
                    .list()

    override fun deleteVersionOfExam(versionExamId: Int, version: Int) =
            handle.createUpdate("delete from $EXAM_VERSION_TABLE where $COURSE_MISC_UNIT_ID = :versionExamId and $EXAM_VERSION = :version")
                    .bind("versionExamId", versionExamId)
                    .bind("version", version)
                    .execute()

    override fun createVersionExam(examVersion: ExamVersion) =
            handle.createUpdate(
                    "insert into $EXAM_VERSION_TABLE " +
                            "($EXAM_VERSION_ID, $EXAM_VERSION, " +
                            "$EXAM_SHEET, $EXAM_DUE_DATE, $EXAM_TYPE, $EXAM_PHASE, " +
                            "$EXAM_LOCATION, $EXAM_CREATED_BY, $EXAM_TIMESTAMP) " +
                            "values(:id, :version, :sheet, :dueDate, :type::exam_type, " +
                            ":phase, :location, :createdBy, :timestamp)"
            )
                    .bind("id", examVersion.courseMiscUnitId)
                    .bind("version", examVersion.version)
                    .bind("createdBy", examVersion.createdBy)
                    .bind("sheet", examVersion.sheet)
                    .bind("dueDate", examVersion.dueDate)
                    .bind("type", examVersion.type)
                    .bind("phase", examVersion.phase)
                    .bind("location", examVersion.location)
                    .bind("timestamp", examVersion.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ExamVersion::class.java)
                    .findFirst()

    override fun reportExam(examReport: ExamReport) =
            handle.createUpdate(
                    "insert into $EXAM_REPORT_TABLE " +
                            "($COURSE_MISC_UNIT_ID, $EXAM_REPORTED_BY, " +
                            "$EXAM_SHEET, $EXAM_DUE_DATE, $EXAM_TYPE, $EXAM_PHASE, " +
                            "$EXAM_LOCATION, $EXAM_TIMESTAMP) " +
                            "values(:id, :reportedBy, :sheet, :dueDate, :type::exam_type, " +
                            ":phase, :location, :timestamp)"
            )
                    .bind("id", examReport.courseMiscUnitId)
                    .bind("reportedBy", examReport.reportedBy)
                    .bind("sheet", examReport.sheet)
                    .bind("dueDate", examReport.dueDate)
                    .bind("type", examReport.type)
                    .bind("phase", examReport.phase)
                    .bind("location", examReport.location)
                    .bind("timestamp", examReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ExamReport::class.java)
                    .findFirst()

    override fun deleteReportOnExam(examId: Int, reportId: Int) =
            handle.createUpdate("delete from $EXAM_REPORT_TABLE where $COURSE_MISC_UNIT_ID = :examId and $EXAM_REPORT_ID = :reportId")
                    .bind("examId", examId)
                    .bind("reportId", reportId)
                    .execute()

    override fun deleteAllReportsOnExam(courseMiscUnitId: Int) =
            handle.createUpdate("delete from $EXAM_REPORT_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :organization_id")
                    .bind("organization_id", courseMiscUnitId)
                    .execute()

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select C.$COURSE_MISC_UNIT_ID, $EXAM_LOCATION, $EXAM_VERSION, $EXAM_DUE_DATE, $EXAM_PHASE, " +
                            "$EXAM_SHEET, $EXAM_TYPE, $EXAM_VOTES, C.$TERM_ID, $EXAM_CREATED_BY " +
                            "from $EXAM_TABLE as E " +
                            "inner join $COURSE_MISC_UNIT_TABLE as C " +
                            "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId and C.$TERM_ID = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(Exam::class.java)
                    .list()

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int) =
            handle.createQuery(
                    "select C.$TERM_ID, C.$COURSE_MISC_UNIT_ID, E.$EXAM_TIMESTAMP, E.$EXAM_LOCATION, E.$EXAM_VERSION, E.$EXAM_DUE_DATE, E.$EXAM_PHASE, " +
                            "E.$EXAM_SHEET, E.$EXAM_TYPE, E.$EXAM_VOTES, E.$EXAM_CREATED_BY " +
                            "from $EXAM_TABLE as E " +
                            "inner join $COURSE_MISC_UNIT_TABLE as C " +
                            "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId " +
                            "and C.$TERM_ID = :termId " +
                            "and C.$COURSE_MISC_UNIT_ID = :examId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("examId", examId)
                    .mapTo(Exam::class.java)
                    .findFirst()

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select E.$EXAM_STAGE_ID, $EXAM_LOCATION, $EXAM_DUE_DATE, $EXAM_PHASE " +
                            "$EXAM_SHEET, $EXAM_TYPE, $EXAM_VOTES, $EXAM_CREATED_BY " +
                            "from $EXAM_STAGE_TABLE as E " +
                            "inner join $COURSE_MISC_UNIT_STAGE_TABLE as C " +
                            "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId and C.$TERM_ID = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(ExamStage::class.java)
                    .list()

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int) =
            handle.createQuery(
                    "select E.$EXAM_STAGE_ID, $EXAM_LOCATION, $EXAM_DUE_DATE, $EXAM_PHASE " +
                            "$EXAM_SHEET, $EXAM_TYPE, $EXAM_VOTES " +
                            "from $EXAM_STAGE_TABLE as E " +
                            "inner join $COURSE_MISC_UNIT_STAGE_TABLE as C " +
                            "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId " +
                            "and C.$TERM_ID = :termId " +
                            "and C.$COURSE_MISC_UNIT_ID = :stageId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("stageId", stageId)
                    .mapTo(ExamStage::class.java)
                    .findFirst()

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int) =
            handle.createQuery(
                    "select * from $EXAM_REPORT_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :examId"
            )
                    .bind("examId", examId)
                    .mapTo(ExamReport::class.java)
                    .list()

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int) =
            handle.createQuery(
                    "select * from $EXAM_REPORT_TABLE " +
                            "where $EXAM_REPORT_ID = :reportId"
            )
                    .bind("reportId", reportId)
                    .mapTo(ExamReport::class.java)
                    .findFirst()

    override fun voteOnReportToExamOnCourseInTerm(reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $EXAM_VOTES from $EXAM_REPORT_TABLE where $EXAM_REPORT_ID = :reportId")
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $EXAM_REPORT_TABLE set $EXAM_VOTES = :votes where $EXAM_REPORT_ID = :reportId")
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getSpecificReportOfExam(examId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $EXAM_REPORT_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :examId and $EXAM_REPORT_ID = :reportId"
            )
                    .bind("examId", examId)
                    .bind("reportId", reportId)
                    .mapTo(ExamReport::class.java)
                    .findFirst()

    override fun getExamSpecificStageEntry(stageId: Int) =
            handle.createQuery(
                    "select * from $EXAM_STAGE_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :examId"
            )
                    .bind("examId", stageId)
                    .mapTo(ExamStage::class.java)
                    .findFirst()

    override fun deleteStagedExam(stageId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_MISC_UNIT_STAGE_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :examId"
            )
                    .bind("examId", stageId)
                    .execute()

    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int) =
        handle.createUpdate(
                "delete from $COURSE_MISC_UNIT_TABLE " +
                        "where $COURSE_ID = :courseId and $TERM_ID = :termId and $COURSE_MISC_TYPE = Exam/Test"
        )
                .bind("courseId", courseId)
                .bind("termId", termId)
                .execute()

    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int) =
        handle.createUpdate(
                "delete from $COURSE_MISC_UNIT_STAGE_TABLE " +
                        "where $COURSE_MISC_UNIT_ID = :courseId and $TERM_ID = :termId and $COURSE_MISC_TYPE = Exam/Test"
        )
                .bind("courseId", courseId)
                .bind("termId", termId)
                .execute()

    override fun deleteAllVersionOfExam(versionExamId: Int) =
        handle.createUpdate("delete from $EXAM_VERSION_TABLE where $COURSE_MISC_UNIT_ID = :versionExamId")
                .bind("versionExamId", versionExamId)
                .execute()

    override fun getAllVersionsOfSpecificExam(examId: Int) =
        handle.createQuery("select * from $EXAM_VERSION_TABLE where $COURSE_MISC_UNIT_ID = :examId")
                .bind("examId", examId)
                .mapTo(ExamVersion::class.java)
                .list()

    override fun getVersionOfSpecificExam(examId: Int, versionId: Int) =
        handle.createQuery("select * from $EXAM_VERSION_TABLE where $COURSE_MISC_UNIT_ID = :examId and $EXAM_VERSION = :versionId")
                .bind("examId", examId)
                .bind("versionId", versionId)
                .mapTo(ExamVersion::class.java)
                .findFirst()

}