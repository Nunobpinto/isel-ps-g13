package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
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

    @Autowired
    lateinit var handle: Handle

    override fun deleteSpecificExamOfCourseInTerm(courseMiscUnitId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE}" +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
            )
                    .bind("examId", courseMiscUnitId)
                    .execute()

    override fun updateExam(examId: Int, exam: Exam) : Optional<Exam> =
            handle.createUpdate(
                    "update $EXAM_TABLE SET " +
                            "$EXAM_VERSION = :version, " +
                            "$EXAM_CREATED_BY = :createdBy, " +
                            "$EXAM_SHEET = :sheet, " +
                            "$EXAM_DUE_DATE = :dueDate, " +
                            "$EXAM_TYPE = :type, " +
                            "$EXAM_PHASE = :phase, " +
                            "$EXAM_LOCATION = :location, " +
                            "$EXAM_VOTES = :votes, " +
                            "$EXAM_TIMESTAMP = :timestamp " +
                            "where $EXAM_ID = :examId"
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
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Exam::class.java)
                    .findFirst()

    override fun createExam(courseId: Int, termId: Int, exam: Exam): Optional<Exam> {
        if (!handle.isInTransaction) handle.begin()
        val courseMiscUnit = handle.createUpdate(
                "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} (" +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TYPE}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} " +
                        ") " +
                        "values(:type, :courseId, :termId)"
        )
                .bind("type", exam.type)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()
        val exam = handle.createUpdate(
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
        if (!handle.isInTransaction) handle.commit()
        return exam
    }

    override fun voteOnExam(examId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $EXAM_VOTES from $EXAM_TABLE " +
                        "where $EXAM_ID = :examId"
        )
                .bind("examId", examId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate(
                "update $EXAM_TABLE set $EXAM_VOTES = :votes " +
                        "where $EXAM_ID = :examId")
                .bind("votes", votes)
                .bind("examId", examId)
                .execute()
    }

    override fun getAllExamStages() =
            handle.createQuery("select * from $EXAM_STAGE_TABLE")
                    .mapTo(ExamStage::class.java)
                    .list()

    override fun createStagingExam(courseId: Int, termId: Int, examStage: ExamStage): Optional<ExamStage> {
        handle.begin()
        val courseMiscUnitStage = handle.createUpdate(
                "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} (" +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TYPE} " +
                        ") " +
                        "values(:courseId, :termId, :miscType:)"
        )
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("miscType", examStage.type)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnitStage::class.java)
                .findOnly()

        val examStage = handle.createUpdate(
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
        handle.commit()
        return examStage
    }

    override fun voteOnStagedExam(stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $EXAM_VOTES from $EXAM_STAGE_TABLE " +
                        "where $EXAM_STAGE_ID = :stageId"
        )
                .bind("stageId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate(
                "update $EXAM_STAGE_TABLE set $EXAM_VOTES = :votes " +
                        "where $EXAM_STAGE_ID = :stageId")
                .bind("votes", votes)
                .bind("stageId", stageId)
                .execute()
    }

    override fun getVersionExam(examId: Int, version: Int) =
            handle.createQuery(
                    "select * from $EXAM_VERSION_TABLE " +
                            "where $EXAM_VERSION_ID = :examId " +
                            "and $EXAM_VERSION = :version"
            )
                    .bind("examId", examId)
                    .bind("version", version)
                    .mapTo(ExamVersion::class.java)
                    .findFirst()

    override fun getAllVersionExams() =
            handle.createQuery("select * from $EXAM_VERSION_TABLE")
                    .mapTo(ExamVersion::class.java)
                    .list()

    override fun deleteVersionOfExam(examId: Int, version: Int) =
            handle.createUpdate(
                    "delete from $EXAM_VERSION_TABLE " +
                            "where $EXAM_VERSION_ID = :examId " +
                            "and $EXAM_VERSION = :version"
            )
                    .bind("examId", examId)
                    .bind("version", version)
                    .execute()

    override fun createVersionExam(examVersion: ExamVersion) =
            handle.createUpdate(
                    "insert into $EXAM_VERSION_TABLE (" +
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
                            "values(:examId, :version, :sheet, :dueDate, :type, " +
                            ":phase, :location, :createdBy, :timestamp)"
            )
                    .bind("examId", examVersion.examId)
                    .bind("version", examVersion.version)
                    .bind("sheet", examVersion.sheet)
                    .bind("dueDate", examVersion.dueDate)
                    .bind("type", examVersion.type)
                    .bind("phase", examVersion.phase)
                    .bind("location", examVersion.location)
                    .bind("createdBy", examVersion.createdBy)
                    .bind("timestamp", examVersion.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ExamVersion::class.java)
                    .findFirst()

    override fun reportExam(examReport: ExamReport) =
            handle.createUpdate(
                    "insert into $EXAM_REPORT_TABLE (" +
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
                            "values(:examId, :sheet, :dueDate, :type, " +
                            ":phase, :location, :reportedBy, :votes, :timestamp)"
            )
                    .bind("examId", examReport.examId)
                    .bind("sheet", examReport.sheet)
                    .bind("dueDate", examReport.dueDate)
                    .bind("type", examReport.type)
                    .bind("phase", examReport.phase)
                    .bind("location", examReport.location)
                    .bind("reportedBy", examReport.reportedBy)
                    .bind("votes", examReport.votes)
                    .bind("timestamp", examReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ExamReport::class.java)
                    .findFirst()

    override fun deleteReportOnExam(examId: Int, reportId: Int) =
            handle.createUpdate(
                    "delete from $EXAM_REPORT_TABLE " +
                            "where $EXAM_ID = :examId " +
                            "and $EXAM_REPORT_ID = :reportId")
                    .bind("examId", examId)
                    .bind("reportId", reportId)
                    .execute()

    override fun deleteAllReportsOnExam(courseMiscUnitId: Int) =
            handle.createUpdate(
                    "delete from $EXAM_REPORT_TABLE " +
                            "where $EXAM_ID = :examId"
            )
                    .bind("examId", courseMiscUnitId)
                    .execute()

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select E.$EXAM_ID, " +
                            "E.$EXAM_VERSION, " +
                            "E.$EXAM_CREATED_BY, " +
                            "E.$EXAM_SHEET, " +
                            "E.$EXAM_DUE_DATE, " +
                            "E.$EXAM_TYPE, " +
                            "E.$EXAM_PHASE, " +
                            "E.$EXAM_LOCATION, " +
                            "E.$EXAM_VOTES, " +
                            "E.$EXAM_TIMESTAMP " +
                            "from $EXAM_TABLE as E " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                            "on E.$EXAM_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} " +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(Exam::class.java)
                    .list()

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int) =
            handle.createQuery(
                    "select E.$EXAM_ID, " +
                            "E:$EXAM_VERSION, " +
                            "E.$EXAM_CREATED_BY " +
                            "E.$EXAM_SHEET, " +
                            "E.$EXAM_DUE_DATE, " +
                            "E.$EXAM_TYPE, " +
                            "E.$EXAM_PHASE, " +
                            "E.$EXAM_LOCATION, " +
                            "E.$EXAM_VOTES," +
                            "E.$EXAM_TIMESTAMP, " +
                            "from $EXAM_TABLE as E " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                            "on E.$EXAM_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("examId", examId)
                    .mapTo(Exam::class.java)
                    .findFirst()

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select E.$EXAM_STAGE_ID, " +
                            "E.$EXAM_SHEET, " +
                            "E.$EXAM_DUE_DATE, " +
                            "E.$EXAM_TYPE, " +
                            "E.$EXAM_PHASE " +
                            "E.$EXAM_LOCATION, " +
                            "E.$EXAM_CREATED_BY " +
                            "E.$EXAM_VOTES, " +
                            "E.$EXAM_TIMESTAMP " +
                            "from $EXAM_STAGE_TABLE as E " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} as C " +
                            "on E.$EXAM_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} " +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(ExamStage::class.java)
                    .list()

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int) =
            handle.createQuery(
                    "select E.$EXAM_STAGE_ID, " +
                            "E.$EXAM_SHEET, " +
                            "E.$EXAM_DUE_DATE, " +
                            "E.$EXAM_TYPE, " +
                            "E.$EXAM_PHASE " +
                            "E.$EXAM_LOCATION, " +
                            "E.$EXAM_CREATED_BY, " +
                            "E.$EXAM_VOTES, " +
                            "E.$EXAM_TIMESTAMP " +
                            "from $EXAM_STAGE_TABLE as E " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} as C " +
                            "on E.$EXAM_STAGE_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_ID} " +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_ID} = :stageId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("stageId", stageId)
                    .mapTo(ExamStage::class.java)
                    .findFirst()

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int) =
            handle.createQuery(
                    "select * from $EXAM_REPORT_TABLE " +
                            "where $EXAM_ID = :examId"
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
        var votes = handle.createQuery(
                "select $EXAM_VOTES from $EXAM_REPORT_TABLE " +
                        "where $EXAM_REPORT_ID = :reportId"
        )
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate(
                "update $EXAM_REPORT_TABLE set $EXAM_VOTES = :votes " +
                        "where $EXAM_REPORT_ID = :reportId"
        )
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getSpecificReportOfExam(examId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $EXAM_REPORT_TABLE " +
                            "where $EXAM_ID = :examId and $EXAM_REPORT_ID = :reportId"
            )
                    .bind("examId", examId)
                    .bind("reportId", reportId)
                    .mapTo(ExamReport::class.java)
                    .findFirst()

    override fun getExamSpecificStageEntry(stageId: Int) =
            handle.createQuery(
                    "select * from $EXAM_STAGE_TABLE " +
                            "where $EXAM_STAGE_ID = :stageId"
            )
                    .bind("stageId", stageId)
                    .mapTo(ExamStage::class.java)
                    .findFirst()

    override fun deleteStagedExam(stageId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE}" +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_ID} = :stageId"
            )
                    .bind("stageId", stageId)
                    .execute()

    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} " +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and ${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId "
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .execute()

    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} " +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :courseId " +
                            "and ${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .execute()

    override fun deleteAllVersionOfExam(examId: Int) =
            handle.createUpdate(
                    "delete from $EXAM_VERSION_TABLE " +
                            "where $EXAM_VERSION_ID = :examId"
            )
                    .bind("examId", examId)
                    .execute()

    override fun getAllVersionsOfSpecificExam(examId: Int) =
            handle.createQuery(
                    "select * from $EXAM_VERSION_TABLE " +
                            "where $EXAM_VERSION_ID = :examId"
            )
                    .bind("examId", examId)
                    .mapTo(ExamVersion::class.java)
                    .list()

    override fun getVersionOfSpecificExam(examId: Int, version: Int) =
            handle.createQuery(
                    "select * from $EXAM_VERSION_TABLE " +
                            "where $EXAM_VERSION_ID = :examId " +
                            "and $EXAM_VERSION = :versionId"
            )
                    .bind("examId", examId)
                    .bind("versionId", version)
                    .mapTo(ExamVersion::class.java)
                    .findFirst()

}