package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class ExamDAOImpl : ExamDAO {

    //TODO Delete With Cascade All Course_misc_unit of type Exam

    companion object {
        //TABLE NAMES
        const val EXM_TABLE = "exam"
        const val EXM_VERSION_TABLE = "exam_version"
        const val EXM_REPORT_TABLE = "exam_report"
        const val EXM_STAGE_TABLE = "exam_stage"
        // FIELDS
        const val EXM_VERSION = "exam_version"
        const val EXM_SHEET = "sheet"
        const val EXM_DUE_DATE = "due_date"
        const val EXM_TYPE = "exam_type"
        const val EXM_PHASE = "phase"
        const val EXM_LOCATION = "location"
        const val EXM_VOTES = "votes"
        const val EXM_TIMESTAMP = "time_stamp"
        const val EXM_REPORT_ID = "report_id"
        const val EXM_REPORTED_BY = "reported_by"
        const val EXM_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var dbi: Jdbi

    override fun getSpecificExam(courseMiscUnitId: Int): Exam = dbi.withHandle<Exam, Exception> {
        val select = "select * from $EXM_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createQuery(select)
                .mapTo(Exam::class.java)
                .findOnly()
    }

    override fun getAllExams(): List<Exam> = dbi.withHandle<List<Exam>, Exception> {
        val select = "select * from $EXM_TABLE"
        it.createQuery(select)
                .mapTo(Exam::class.java)
                .list()
    }

    override fun deleteSpecificExamOfCourseInTerm(courseMiscUnitId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :organization_id"
        it.createUpdate(delete)
                .bind("organization_id", courseMiscUnitId)
                .execute()
    }

    override fun updateExam(examId: Int, exam: Exam): Int = dbi.withHandle<Int, Exception> {
        val update = "update $EXM_TABLE SET " +
                "$EXM_VERSION = :version, $EXM_CREATED_BY = :createdBy, " +
                "$EXM_SHEET = :sheet, $EXM_DUE_DATE = :dueDate, " +
                "$EXM_TYPE = :type, $EXM_PHASE = :phase, " +
                "$EXM_LOCATION = :location, $EXM_VOTES = :votes, $EXM_TIMESTAMP = :timestamp " +
                "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"

        it.createUpdate(update)
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
    }

    override fun createExam(courseId: Int, termId: Int, exam: Exam): Int = dbi.inTransaction<Int, Exception> {
        val insertInCourseMiscUnit = "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} " +
                "(${CourseDAOImpl.COURSE_MISC_TYPE}, ${CourseDAOImpl.COURSE_ID}, " +
                "${TermDAOImpl.TERM_ID}, $EXM_TIMESTAMP)" +
                "values(:miscType, :courseId, :termId, :timestamp)"

        val courseMiscUnit = it.createUpdate(insertInCourseMiscUnit)
                .bind("miscType", "Exam/Test")
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("timestamp", exam.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()

        val insertInExam = "insert into $EXM_TABLE" +
                "(${CourseDAOImpl.COURSE_MISC_UNIT_ID}, $EXM_VERSION, $EXM_CREATED_BY, " +
                "$EXM_SHEET, $EXM_DUE_DATE, $EXM_TYPE, $EXM_PHASE, $EXM_LOCATION," +
                "$EXM_VOTES, $EXM_TIMESTAMP)" +
                "values(:courseMiscUnitId, :version, :createdBy, :sheet, :dueDate, :type," +
                ":phase, :location, :votes, :timestamp)"
        it.createUpdate(insertInExam)
                .bind("courseMiscUnitId", courseMiscUnit.id)
                .bind("version", exam.version)
                .bind("createdBy", exam.createdBy)
                .bind("sheet", exam.sheet)
                .bind("dueDate", exam.dueDate)
                .bind("type", exam.type)
                .bind("phase", exam.phase)
                .bind("location", exam.location)
                .bind("votes", exam.votes)
                .bind("timestamp", exam.timestamp)
                .execute()
    }

    override fun voteOnExam(courseMiscUnitId: Int, vote: Vote): Int = dbi.withHandle<Int, Exception> {
        val voteQuery = "select $EXM_VOTES from $EXM_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        var votes = it.createQuery(voteQuery)
                .bind("examId", courseMiscUnitId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $EXM_TABLE set $EXM_VOTES = :votes where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("examId", courseMiscUnitId)
                .execute()
    }

    override fun getAllExamStages(): List<ExamStage> = dbi.withHandle<List<ExamStage>, Exception> {
        null
    }

    override fun createStagingExam(courseId: Int, termId: Int, examStage: ExamStage): Int = dbi.inTransaction<Int, Exception> {
        val insertInCourseMiscUnitStage = "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} " +
                "(${CourseDAOImpl.COURSE_ID}, ${TermDAOImpl.TERM_ID}, ${CourseDAOImpl.COURSE_MISC_TYPE}" +
                "values(:courseId, :termId, :miscType)"

        val courseMiscUnitStage = it.createUpdate(insertInCourseMiscUnitStage)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("miscType", "Exam/Test")
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnitStage::class.java)
                .findOnly()

        val insertInExamStage = "insert into $EXM_STAGE_TABLE" +
                "(${CourseDAOImpl.COURSE_MISC_UNIT_ID}, $EXM_SHEET, $EXM_DUE_DATE," +
                "$EXM_TYPE, $EXM_PHASE, $EXM_LOCATION, $EXM_VERSION, $EXM_CREATED_BY," +
                "$EXM_VOTES, $EXM_TIMESTAMP" +
                "values(:courseMiscUnitId, :sheet, :dueDate, :type, :phase, :location, :version, " +
                ":createdBy, :votes, :timestamp)"
        it.createUpdate(insertInExamStage)
                .bind("courseMiscUnitId", courseMiscUnitStage.id)
                .bind("sheet", examStage.sheet)
                .bind("dueDate", examStage.dueDate)
                .bind("type", examStage.type)
                .bind("phase", examStage.phase)
                .bind("location", examStage.location)
                .bind("createdBy", examStage.createdBy)
                .bind("votes", examStage.votes)
                .bind("timestamp", examStage.timestamp)
                .execute()
    }

    override fun voteOnStagedExam(stageId: Int, vote: Vote): Int = dbi.inTransaction<Int, Exception> {
        val voteQuery = "select $EXM_VOTES from $EXM_STAGE_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        var votes = it.createQuery(voteQuery)
                .bind("examId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $EXM_STAGE_TABLE set $EXM_VOTES = :votes where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("examId", stageId)
                .execute()
    }

    override fun getVersionExam(versionExamId: Int, version: Int): ExamVersion = dbi.withHandle<ExamVersion, Exception> {
        null
    }

    override fun getAllVersionExams(): List<ExamVersion> = dbi.withHandle<List<ExamVersion>, Exception> {
        null
    }

    override fun deleteVersionOfExam(versionExamId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $EXM_VERSION_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :versionExamId and $EXM_VERSION = :version"
        it.createUpdate(delete)
                .bind("versionExamId", versionExamId)
                .bind("version", version)
                .execute()

    }

    override fun createVersionExam(examVersion: ExamVersion) = dbi.useHandle<Exception> {
    }

    override fun reportExam(examReport: ExamReport) = dbi.useHandle<Exception> {
    }

    override fun deleteReportOnExam(examId: Int, reportId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $EXM_REPORT_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId and $EXM_REPORT_ID = :reportId"
        it.createUpdate(delete)
                .bind("examId", examId)
                .bind("reportId", reportId)
                .execute()

    }

    override fun deleteAllReportsOnExam(courseMiscUnitId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $EXM_REPORT_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :organization_id"
        it.createUpdate(delete)
                .bind("organization_id", courseMiscUnitId)
                .execute()
    }

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam> = dbi.withHandle<List<Exam>, Exception> {
        val select = "select $EXM_LOCATION, $EXM_VERSION, $EXM_DUE_DATE, $EXM_PHASE" +
                ", $EXM_SHEET, $EXM_TYPE, $EXM_VOTES from $EXM_TABLE as E" +
                "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                "on E.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                "where C.${CourseDAOImpl.COURSE_ID} = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(Exam::class.java)
                .list()
    }

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam = dbi.withHandle<Exam, Exception> {
        val select = "select $EXM_LOCATION, $EXM_VERSION, $EXM_DUE_DATE, $EXM_PHASE" +
                ", $EXM_SHEET, $EXM_TYPE, $EXM_VOTES from $EXM_TABLE as E " +
                "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                "on E.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} " +
                "where C.${CourseDAOImpl.COURSE_ID} = :courseId " +
                "and C.${TermDAOImpl.TERM_ID} = :termId " +
                "and C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("examId", examId)
                .mapTo(Exam::class.java)
                .findOnly()
    }

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage> = dbi.withHandle<List<ExamStage>, Exception> {
        val select = "select $EXM_LOCATION, $EXM_VERSION, $EXM_DUE_DATE, $EXM_PHASE" +
                ", $EXM_SHEET, $EXM_TYPE, $EXM_VOTES, $EXM_CREATED_BY from $EXM_STAGE_TABLE as E" +
                "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                "on E.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                "where C.${CourseDAOImpl.COURSE_ID} = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(ExamStage::class.java)
                .list()
    }

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage = dbi.withHandle<ExamStage, Exception> {
        val select = "select $EXM_LOCATION, $EXM_VERSION, $EXM_DUE_DATE, $EXM_PHASE" +
                ", $EXM_SHEET, $EXM_TYPE, $EXM_VOTES from $EXM_STAGE_TABLE as E" +
                "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                "on E.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                "where C.${CourseDAOImpl.COURSE_ID} = :courseId " +
                "and C.${TermDAOImpl.TERM_ID} = :termId " +
                "and ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :stageId"

        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("stageId", stageId)
                .mapTo(ExamStage::class.java)
                .findOnly()
    }

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport> = dbi.withHandle<List<ExamReport>, Exception> {
        val select = "select * from $EXM_REPORT_TABLE " +
                "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createQuery(select)
                .bind("examId", examId)
                .mapTo(ExamReport::class.java)
                .list()
    }

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport = dbi.withHandle<ExamReport, Exception> {
        val select = "select * from $EXM_REPORT_TABLE" +
                "where $EXM_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("reportId", reportId)
                .mapTo(ExamReport::class.java)
                .findOnly()
    }

    override fun createExamOnCourseInTerm(courseId: Int, termId: Int, exam: Exam): Int = dbi.inTransaction<Int, Exception> {
        val insertInCourseMiscUnit = "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE}" +
                "(${CourseDAOImpl.COURSE_MISC_TYPE}, ${CourseDAOImpl.COURSE_ID}" +
                "${TermDAOImpl.TERM_ID}, ${CourseDAOImpl.COURSE_TIMESTAMP}" +
                "values(:miscType, :courseId, :termId, :timestamp)"

        val courseMiscUnit = it.createUpdate(insertInCourseMiscUnit)
                .bind("miscType", "Exam/Test")
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("timestamp", exam.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()

        val insertInExam = "insert into $EXM_TABLE" +
                "(${CourseDAOImpl.COURSE_MISC_UNIT_ID}, $EXM_VERSION" +
                "$EXM_CREATED_BY, $EXM_SHEET, $EXM_DUE_DATE, $EXM_TYPE," +
                "$EXM_PHASE, $EXM_LOCATION, $EXM_VOTES, $EXM_TIMESTAMP" +
                "values(:courseMiscUnitId, :version, :createdBy, :sheet," +
                ":dueDate, :type, :phase, :location, :votes, :timestamp)"
        it.createUpdate(insertInExam)
                .bind("courseMiscUnitId", courseMiscUnit.id)
                .bind("version", exam.version)
                .bind("createdBy", exam.createdBy)
                .bind("sheet", exam.sheet)
                .bind("dueDate", exam.dueDate)
                .bind("type", exam.type)
                .bind("phase", exam.phase)
                .bind("location", exam.location)
                .bind("votes", exam.votes)
                .bind("timestamp", exam.timestamp)
                .execute()
    }

    override fun addReportToExamOnCourseInTerm(examId: Int, examReport: ExamReport): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $EXM_REPORT_TABLE" +
                "(${CourseDAOImpl.COURSE_MISC_UNIT_ID}, $EXM_SHEET, " +
                "$EXM_DUE_DATE, $EXM_TYPE, $EXM_PHASE, $EXM_LOCATION," +
                "$EXM_REPORTED_BY, $EXM_VOTES, $EXM_TIMESTAMP ) " +
                "values(:examId, :sheet, :dueDate, :type, :phase, :location, :reportedBy, :votes, :timestamp)"
        it.createUpdate(insert)
                .bind("examId", examId)
                .bind("sheet", examReport.sheet)
                .bind("dueDate", examReport.dueDate)
                .bind("type", examReport.type)
                .bind("phase", examReport.phase)
                .bind("location", examReport.location)
                .bind("reportedBy", examReport.reportedBy)
                .bind("votes", examReport.votes)
                .bind("timestamp", examReport.timestamp)
                .execute()
    }

    override fun voteOnReportToExamOnCourseInTerm(reportId: Int, vote: Vote): Int = dbi.inTransaction<Int, Exception> {
        val voteQuery = "select $EXM_VOTES from $EXM_REPORT_TABLE where $EXM_REPORT_ID = :reportId"
        var votes = it.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $EXM_REPORT_TABLE set $EXM_VOTES = :votes where $EXM_REPORT_ID = :reportId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getSpecificReportOfExam(examId: Int, reportId: Int): ExamReport = dbi.withHandle<ExamReport, Exception> {
        val select = "select * from $EXM_REPORT_TABLE " +
                "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId and $EXM_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("examId", examId)
                .bind("reportId", reportId)
                .mapTo(ExamReport::class.java)
                .findOnly()
    }

    override fun addToExamVersion(exam: Exam): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $EXM_VERSION_TABLE " +
                "(${CourseDAOImpl.COURSE_MISC_UNIT_ID}, $EXM_VERSION, $EXM_SHEET, $EXM_DUE_DATE, " +
                "$EXM_TYPE, $EXM_PHASE, $EXM_LOCATION, $EXM_CREATED_BY, $EXM_TIMESTAMP" +
                "values (:examId, :version, :sheet, :dueDate, :type, " +
                ":phase, :location,  createdBy, :timestamp)"
        it.createUpdate(insert)
                .bind("examId", exam.id)
                .bind("version", exam.version)
                .bind("sheet", exam.sheet)
                .bind("dueDate", exam.dueDate)
                .bind("type", exam.type)
                .bind("phase", exam.phase)
                .bind("location", exam.location)
                .bind("createdBy", exam.createdBy)
                .bind("timestamp", exam.timestamp)
                .execute()
    }

    override fun getExamSpecificStageEntry(stageId: Int): ExamStage = dbi.withHandle<ExamStage, Exception> {
        val select = "select * from $EXM_STAGE_TABLE" +
                "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createQuery(select)
                .bind("examId", stageId)
                .mapTo(ExamStage::class.java)
                .findOnly()
    }

    override fun deleteStagedExam(stageId: Int): Int = dbi.inTransaction<Int, Exception> {
        val deleteFromCourseMiscUnitStageTable = "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE}" +
                "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createUpdate(deleteFromCourseMiscUnitStageTable)
                .bind("examId", stageId)
                .execute()
    }

    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} where ${CourseDAOImpl.COURSE_ID} = :courseId and ${TermDAOImpl.TERM_ID} = :termId and ${CourseDAOImpl.COURSE_MISC_TYPE} = Exam/Test"
        it.createUpdate(delete)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .execute()
    }

    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :courseId and ${TermDAOImpl.TERM_ID} = :termId and ${CourseDAOImpl.COURSE_MISC_TYPE} = Exam/Test"
        it.createUpdate(delete)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .execute()
    }

    override fun deleteAllVersionOfExam(versionExamId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $EXM_VERSION_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :versionExamId"
        it.createUpdate(delete)
                .bind("versionExamId", versionExamId)
                .execute()
    }

    override fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion> = dbi.withHandle<List<ExamVersion>, Exception> {
        val select = "select * from $EXM_VERSION_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId"
        it.createQuery(select)
                .bind("examId", examId)
                .mapTo(ExamVersion::class.java)
                .list()
    }

    override fun getVersionOfSpecificExam(examId: Int, versionId: Int): ExamVersion = dbi.withHandle<ExamVersion, Exception> {
        val select = "select * from $EXM_VERSION_TABLE where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :examId and $EXM_VERSION = :versionId"
        it.createQuery(select)
                .bind("examId", examId)
                .bind("versionId", versionId)
                .mapTo(ExamVersion::class.java)
                .findOnly()
    }

}