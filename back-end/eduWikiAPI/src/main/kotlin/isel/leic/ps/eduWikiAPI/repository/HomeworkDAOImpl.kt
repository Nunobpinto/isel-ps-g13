package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.HomeworkDAO
import org.springframework.beans.factory.annotation.Autowired
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class HomeworkDAOImpl : HomeworkDAO {

    companion object {
        //TABLE NAMES
        const val HOMEWORK_TABLE = "homework"
        const val HOMEWORK_STAGE_TABLE = "homework_stage"
        const val HOMEWORK_VERSION_TABLE = "homework_version"
        const val HOMEWORK_REPORT_TABLE = "homework_report"
        // FIELDS
        const val HOMEWORK_VERSION = "homework_version"
        const val HOMEWORK_ID = "homework_id"
        const val HOMEWORK_STAGE_ID = "class_misc_unit_stage_id"
        const val HOMEWORK_REPORT_ID = "homework_report_id"
        const val HOMEWORK_SHEET = "sheet"
        const val HOMEWORK_DUE_DATE = "due_date"
        const val HOMEWORK_LATE_DELIVERY = "late_delivery"
        const val HOMEWORK_MULTIPLE_DELIVERIES = "multipleDeliveries"
        const val HOMEWORK_VOTES = "votes"
        const val HOMEWORK_TIMESTAMP = "time_stamp"
        const val HOMEWORK_REPORTED_BY = "reported_by"
        const val HOMEWORK_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var handle: Handle

    override fun getAllHomeworksFromCourseInClass(courseClassId: Int): List<Homework> =
            handle.createQuery(
                    "select H.$HOMEWORK_ID, " +
                            "H.$HOMEWORK_VERSION, " +
                            "H.$HOMEWORK_CREATED_BY, " +
                            "H.$HOMEWORK_SHEET, " +
                            "H.$HOMEWORK_DUE_DATE, " +
                            "H.$HOMEWORK_LATE_DELIVERY, " +
                            "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                            "H.$HOMEWORK_VOTES, " +
                            "H.$HOMEWORK_TIMESTAMP " +
                            "from $HOMEWORK_TABLE as H" +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on L.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .mapTo(Homework::class.java)
                    .list()

    override fun getSpecificHomeworkFromSpecificCourseInClass(courseClassId: Int, homeworkId: Int): Optional<Homework> =
            handle.createQuery(
                    "select H.$HOMEWORK_ID, " +
                            "H.$HOMEWORK_VERSION, " +
                            "H.$HOMEWORK_CREATED_BY, " +
                            "H.$HOMEWORK_SHEET, " +
                            "H.$HOMEWORK_DUE_DATE, " +
                            "H.$HOMEWORK_LATE_DELIVERY, " +
                            "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                            "H.$HOMEWORK_VOTES, " +
                            "H.$HOMEWORK_TIMESTAMP " +
                            "from $HOMEWORK_TABLE as H" +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on L.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and H.$HOMEWORK_ID = :homeworkId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .mapTo(Homework::class.java)
                    .findFirst()

    override fun createHomework(homework: Homework): Optional<Homework> =
            handle.createUpdate(
                    "insert into $HOMEWORK_TABLE (" +
                            "$HOMEWORK_ID, " +
                            "$HOMEWORK_VERSION, " +
                            "$HOMEWORK_CREATED_BY, " +
                            "$HOMEWORK_SHEET, " +
                            "$HOMEWORK_DUE_DATE, " +
                            "$HOMEWORK_LATE_DELIVERY, " +
                            "$HOMEWORK_MULTIPLE_DELIVERIES, " +
                            "$HOMEWORK_VOTES, " +
                            "$HOMEWORK_TIMESTAMP " +
                            ")" +
                            "values (:homeworkId, :version, :createdBy, :sheet" +
                            ":dueDate, :lateDelivery, :multipleDeliveries, :votes, :timestamp)"
            )
                    .bind("homeworkId", homework.homeworkId)
                    .bind("version", homework.version)
                    .bind("createdBy", homework.createdBy)
                    .bind("sheet", homework.sheet)
                    .bind("dueDate", homework.dueDate)
                    .bind("lateDelivery", homework.lateDelivery)
                    .bind("multipleDeliveries", homework.multipleDeliveries)
                    .bind("votes", homework.votes)
                    .bind("timestamp", homework.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Homework::class.java)
                    .findFirst()

    override fun voteOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select H.$HOMEWORK_VOTES" +
                        "from $HOMEWORK_TABLE as H" +
                        "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                        "on H.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                        "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                        "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                        "and L.$HOMEWORK_ID = :homeworkId"
        )
                .bind("courseClassId", courseClassId)
                .bind("miscType", "Homework")
                .bind("homeworkId", homeworkId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $HOMEWORK_TABLE as H set H.$HOMEWORK_VOTES = :votes " +
                        "from ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                        "where H.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID} " +
                        "and H.$HOMEWORK_ID = :homeworkId"
        )
                .bind("votes", votes)
                .bind("homeworkId", homeworkId)
                .execute()
    }

    override fun deleteAllHomeworksOfCourseInClass(courseClassId: Int): Int =
            handle.createUpdate(
                    "delete from ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .execute()

    override fun deleteSpecificHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int =
            handle.createUpdate(
                    "delete from ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_ID} = :homeworkId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .execute()

    override fun getAllStagedHomeworksOfCourseInClass(courseClassId: Int): List<HomeworkStage> =
            handle.createQuery(
                    "select H.$HOMEWORK_STAGE_ID," +
                            "H.$HOMEWORK_SHEET, " +
                            "H.$HOMEWORK_DUE_DATE, " +
                            "H.$HOMEWORK_LATE_DELIVERY, " +
                            "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                            "H.$HOMEWORK_TIMESTAMP, " +
                            "H.$HOMEWORK_VOTES," +
                            "H.$HOMEWORK_CREATED_BY " +
                            "from $HOMEWORK_STAGE_TABLE as H " +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C " +
                            "on H.$HOMEWORK_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .mapTo(HomeworkStage::class.java)
                    .list()

    override fun getSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Optional<HomeworkStage> =
            handle.createQuery(
                    "select H.$HOMEWORK_STAGE_ID," +
                            "H.$HOMEWORK_SHEET, " +
                            "H.$HOMEWORK_DUE_DATE, " +
                            "H.$HOMEWORK_LATE_DELIVERY, " +
                            "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                            "H.$HOMEWORK_TIMESTAMP, " +
                            "H.$HOMEWORK_VOTES," +
                            "H.$HOMEWORK_CREATED_BY " +
                            "from $HOMEWORK_STAGE_TABLE as H " +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C " +
                            "on H.$HOMEWORK_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and H.$HOMEWORK_STAGE_ID = :stageId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("stageId", stageId)
                    .mapTo(HomeworkStage::class.java)
                    .findFirst()

    override fun createStagingHomework(homeworkStage: HomeworkStage): Optional<HomeworkStage> =
            handle.createUpdate(
                    "insert into $HOMEWORK_STAGE_TABLE (" +
                            "$HOMEWORK_STAGE_ID, " +
                            "$HOMEWORK_SHEET, " +
                            "$HOMEWORK_DUE_DATE, " +
                            "$HOMEWORK_LATE_DELIVERY, " +
                            "$HOMEWORK_MULTIPLE_DELIVERIES, " +
                            "$HOMEWORK_TIMESTAMP, " +
                            "$HOMEWORK_VOTES," +
                            "$HOMEWORK_CREATED_BY " +
                            ")" +
                            "values(:stageId, :sheet, :dueDate, :lateDelivery," +
                            ":multipleDeliveries, :timestamp, :votes, :createdBy)"
            )
                    .bind("stageId", homeworkStage.stageId)
                    .bind("sheet", homeworkStage.sheet)
                    .bind("dueDate", homeworkStage.dueDate)
                    .bind("lateDelivery", homeworkStage.lateDelivery)
                    .bind("multipleDeliveries", homeworkStage.multipleDeliveries)
                    .bind("timestamp", homeworkStage.timestamp)
                    .bind("votes", homeworkStage.votes)
                    .bind("createdBy", homeworkStage.createdBy)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(HomeworkStage::class.java)
                    .findFirst()

    override fun deleteSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Int =
            handle.createUpdate(
                    "delete from ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE}" +
                            "where ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID} = :stageId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("stageId", stageId)
                    .execute()

    override fun createHomeworkVersion(homeworkVersion: HomeworkVersion): Optional<HomeworkVersion> =
            handle.createUpdate(
                    "insert into $HOMEWORK_VERSION_TABLE (" +
                            "$HOMEWORK_ID = :homeworkId, " +
                            "$HOMEWORK_VERSION = :version, " +
                            "$HOMEWORK_CREATED_BY = :createdBy, " +
                            "$HOMEWORK_SHEET = :sheet, " +
                            "$HOMEWORK_DUE_DATE = :dueDate, " +
                            "$HOMEWORK_LATE_DELIVERY = :lateDelivery," +
                            "$HOMEWORK_MULTIPLE_DELIVERIES = :multipleDeliveries, " +
                            "$HOMEWORK_TIMESTAMP = :timestamp " +
                            ")" +
                            "values (:homeworkId, :version, :createdBy, :sheet, " +
                            ":dueDate, :lateDelivery, , :multipleDeliveries, :timestamp)"
            )
                    .bind("homeworkId", homeworkVersion.homeworkId)
                    .bind("version", homeworkVersion.version)
                    .bind("createdBy", homeworkVersion.createdBy)
                    .bind("sheet", homeworkVersion.sheet)
                    .bind("dueDate", homeworkVersion.dueDate)
                    .bind("lateDelivery", homeworkVersion.lateDelivery)
                    .bind("multipleDeliveries", homeworkVersion.multipleDeliveries)
                    .bind("timestamp", homeworkVersion.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(HomeworkVersion::class.java)
                    .findFirst()

    override fun voteOnStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select L.$HOMEWORK_VOTES" +
                        "from $HOMEWORK_STAGE_TABLE as L" +
                        "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C" +
                        "on L.$HOMEWORK_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                        "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                        "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                        "and L.$HOMEWORK_STAGE_ID = :stageId"
        )
                .bind("courseClassId", courseClassId)
                .bind("miscType", "Homework")
                .bind("stageId", stageId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $HOMEWORK_STAGE_TABLE as L set L.$HOMEWORK_VOTES = :votes " +
                        "from ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C" +
                        "where L.$HOMEWORK_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID} " +
                        "and L.$HOMEWORK_STAGE_ID = :stageId"
        )
                .bind("votes", votes)
                .bind("stageId", stageId)
                .execute()
    }

    override fun getAllReportsOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int): List<HomeworkReport> =
            handle.createQuery(
                    "select H.$HOMEWORK_REPORT_ID," +
                            "H.$HOMEWORK_ID," +
                            "H.$HOMEWORK_SHEET," +
                            "H.$HOMEWORK_DUE_DATE," +
                            "H.$HOMEWORK_LATE_DELIVERY," +
                            "H.$HOMEWORK_MULTIPLE_DELIVERIES," +
                            "H.$HOMEWORK_REPORTED_BY," +
                            "H.$HOMEWORK_VOTES," +
                            "H.$HOMEWORK_TIMESTAMP" +
                            "from $HOMEWORK_TABLE as H" +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on H.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and H.$HOMEWORK_ID = :homeworkId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("homeworkId", homeworkId)
                    .mapTo(HomeworkReport::class.java)
                    .list()

    override fun getSpecificReportOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport> =
            handle.createQuery("select H.$HOMEWORK_REPORT_ID," +
                    "H.$HOMEWORK_ID," +
                    "H.$HOMEWORK_SHEET," +
                    "H.$HOMEWORK_DUE_DATE," +
                    "H.$HOMEWORK_LATE_DELIVERY," +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES," +
                    "H.$HOMEWORK_REPORTED_BY," +
                    "H.$HOMEWORK_VOTES," +
                    "H.$HOMEWORK_TIMESTAMP" +
                    "from $HOMEWORK_TABLE as H" +
                    "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                    "on H.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                    "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                    "and H.$HOMEWORK_ID = :homeworkId" +
                    "and H.$HOMEWORK_REPORT_ID = :reportId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("homeworkId", homeworkId)
                    .bind("reportId", reportId)
                    .mapTo(HomeworkReport::class.java)
                    .findFirst()

    override fun createReportOnHomework(homeworkReport: HomeworkReport): Optional<HomeworkReport> =
            handle.createUpdate(
                    "insert into $HOMEWORK_REPORT_TABLE (" +
                            "$HOMEWORK_ID, " +
                            "$HOMEWORK_SHEET, " +
                            "$HOMEWORK_DUE_DATE, " +
                            "$HOMEWORK_LATE_DELIVERY, " +
                            "$HOMEWORK_MULTIPLE_DELIVERIES, " +
                            "$HOMEWORK_REPORTED_BY, " +
                            "$HOMEWORK_VOTES, " +
                            "$HOMEWORK_TIMESTAMP " +
                            ") " +
                            "values (:homeworkId, :sheet, :dueDate, :lateDelivery, " +
                            ":multipleDeliveries, :reportedBy, :votes, :timestamp)"
            )
                    .bind("homeworkId", homeworkReport.homeworkId)
                    .bind("sheet", homeworkReport.sheet)
                    .bind("dueDate", homeworkReport.dueDate)
                    .bind("lateDelivery", homeworkReport.lateDelivery)
                    .bind("multipleDeliveries", homeworkReport.multipleDeliveries)
                    .bind("reportedBy", homeworkReport.reportedBy)
                    .bind("votes", homeworkReport.votes)
                    .bind("timestamp", homeworkReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(HomeworkReport::class.java)
                    .findFirst()

    override fun updateHomeWork(homework: Homework): Optional<Homework> =
            handle.createUpdate(
                    "update $HOMEWORK_TABLE set " +
                            "$HOMEWORK_VERSION = :version, " +
                            "$HOMEWORK_CREATED_BY = :createdBy," +
                            "$HOMEWORK_SHEET = :sheet," +
                            "$HOMEWORK_DUE_DATE = :dueDate," +
                            "$HOMEWORK_LATE_DELIVERY = :lateDelivery, " +
                            "$HOMEWORK_MULTIPLE_DELIVERIES = :multipleDeliveries, " +
                            "$HOMEWORK_VOTES = :votes" +
                            "$HOMEWORK_TIMESTAMP = :timestamp " +
                            "where $HOMEWORK_ID = :homeworkId"
            )
                    .bind("version", homework.version)
                    .bind("createdBy", homework.createdBy)
                    .bind("sheet", homework.sheet)
                    .bind("dueDate", homework.dueDate)
                    .bind("lateDelivery", homework.lateDelivery)
                    .bind("multipleDeliveries", homework.multipleDeliveries)
                    .bind("votes", homework.votes)
                    .bind("timestamp", homework.timestamp)
                    .bind("homeworkId", homework.homeworkId)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Homework::class.java)
                    .findFirst()

    override fun voteOnReportOfHomeworkOfCourseInClass(homeworkId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $HOMEWORK_VOTES from $HOMEWORK_REPORT_TABLE " +
                        "where $HOMEWORK_ID = :homeworkId" +
                        "and $HOMEWORK_REPORT_ID = :reportId"
        )
                .bind("homeworkId", homeworkId)
                .bind("reportId", reportId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $HOMEWORK_REPORT_TABLE set $HOMEWORK_VOTES = :votes " +
                        "where $HOMEWORK_ID = :homeworkId" +
                        "and $HOMEWORK_REPORT_ID = :reportId"
        )
                .bind("votes", votes)
                .bind("homeworkId", homeworkId)
                .bind("reportId", reportId)
                .execute()
    }

    override fun deleteAllReportsOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int =
            handle.createUpdate(
                    "delete from $HOMEWORK_REPORT_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $HOMEWORK_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $HOMEWORK_ID = :homeworkId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .execute()

    override fun deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Int =
            handle.createUpdate(
                    "delete from $HOMEWORK_REPORT_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $HOMEWORK_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $HOMEWORK_ID = :homeworkId" +
                            "and $HOMEWORK_REPORT_ID = :reportId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .bind("reportId", reportId)
                    .execute()

    override fun getAllVersionsOfHomeworkOfCourseInclass(courseClassId: Int, homeworkId: Int): List<HomeworkVersion> =
            handle.createQuery(
                    "select H.$HOMEWORK_ID," +
                            "H.$HOMEWORK_VERSION, " +
                            "H.$HOMEWORK_CREATED_BY, " +
                            "H.$HOMEWORK_SHEET, " +
                            "H.$HOMEWORK_DUE_DATE" +
                            "H.$HOMEWORK_LATE_DELIVERY, " +
                            "H.$HOMEWORK_TIMESTAMP, " +
                            "H.$HOMEWORK_MULTIPLE_DELIVERIES " +
                            "from $HOMEWORK_VERSION_TABLE as H " +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on H.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and H.$HOMEWORK_ID = :homeworkId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .mapTo(HomeworkVersion::class.java)
                    .list()

    override fun getSpecificVersionOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, version: Int): Optional<HomeworkVersion> =
            handle.createQuery(
                    "select H.$HOMEWORK_ID," +
                            "H.$HOMEWORK_VERSION, " +
                            "H.$HOMEWORK_CREATED_BY, " +
                            "H.$HOMEWORK_SHEET, " +
                            "H.$HOMEWORK_DUE_DATE" +
                            "H.$HOMEWORK_LATE_DELIVERY, " +
                            "H.$HOMEWORK_TIMESTAMP, " +
                            "H.$HOMEWORK_MULTIPLE_DELIVERIES " +
                            "from $HOMEWORK_VERSION_TABLE as H " +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on H.$HOMEWORK_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and H.$HOMEWORK_ID = :homeworkId" +
                            "and H.$HOMEWORK_VERSION = :version"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .bind("version", version)
                    .mapTo(HomeworkVersion::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int =
            handle.createUpdate(
                    "delete from $HOMEWORK_VERSION_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $HOMEWORK_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $HOMEWORK_ID = :homeworkId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .execute()

    override fun deleteSpecificVersionOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, version: Int): Int =
            handle.createUpdate(
                    "delete from $HOMEWORK_VERSION_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $HOMEWORK_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $HOMEWORK_ID = :homeworkId" +
                            "and $HOMEWORK_VERSION = :version"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Homework")
                    .bind("homeworkId", homeworkId)
                    .bind("version", version)
                    .execute()





}