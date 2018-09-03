package isel.leic.ps.eduWikiAPI.domain.mappers

import com.sun.xml.internal.bind.v2.schemagen.episode.Klass
import isel.leic.ps.eduWikiAPI.domain.inputModel.HomeworkInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.HomeworkCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.HomeworkReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.HomeworkStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.HomeworkVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.HomeworkReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.HomeworkStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.HomeworkVersionOutputModel
import org.springframework.web.multipart.MultipartFile
import java.util.*

fun toHomework(homeworkInputModel: HomeworkInputModel, createdBy: String, sheet: MultipartFile?) = Homework(
        createdBy = createdBy,
        homeworkName = homeworkInputModel.homeworkName,
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries,
        sheetId = if (sheet == null) null else UUID.randomUUID()
)

fun toHomeworkStage(homeworkInputModel: HomeworkInputModel, sheet: MultipartFile?, createdBy: String) = HomeworkStage(
        createdBy = createdBy,
        homeworkName = homeworkInputModel.homeworkName,
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries,
        sheetId = if (sheet == null) null else UUID.randomUUID()
)

fun stagedToHomework(stagedHomework: HomeworkStage) = Homework(
        createdBy = stagedHomework.createdBy,
        homeworkName = stagedHomework.homeworkName,
        sheetId = stagedHomework.sheetId,
        dueDate = stagedHomework.dueDate,
        lateDelivery = stagedHomework.lateDelivery,
        multipleDeliveries = stagedHomework.multipleDeliveries
)

fun toHomeworkVersion(homework: Homework) = HomeworkVersion(
        version = homework.version,
        homeworkId = homework.homeworkId,
        homeworkName = homework.homeworkName,
        createdBy = homework.createdBy,
        sheetId = homework.sheetId,
        dueDate = homework.dueDate,
        lateDelivery = homework.lateDelivery,
        multipleDeliveries = homework.multipleDeliveries,
        timestamp = homework.timestamp
)

fun toHomeworkReport(homeworkReportInputModel: HomeworkReportInputModel, homeworkId: Int, reportedBy: String) = HomeworkReport(
        homeworkId = homeworkId,
        sheetId = homeworkReportInputModel.sheetId,
        homeworkName = homeworkReportInputModel.homeworkName,
        dueDate = homeworkReportInputModel.dueDate,
        lateDelivery = homeworkReportInputModel.lateDelivery,
        multipleDeliveries = homeworkReportInputModel.multipleDeliveries,
        reportedBy = reportedBy
)

fun toHomeworkOutputModel(homework: Homework, course: Course, klass: Class, term: Term) = HomeworkOutputModel(
        createdBy = homework.createdBy,
        sheetId = homework.sheetId,
        dueDate = homework.dueDate,
        homeworkName = homework.homeworkName,
        lateDelivery = homework.lateDelivery,
        multipleDeliveries = homework.multipleDeliveries,
        homeworkId = homework.homeworkId,
        votes = homework.votes,
        version = homework.version,
        courseShortName = course.shortName,
        className = klass.className,
        lecturedTerm = term.shortName
)

fun toHomeworkVersionOutputModel(homeworkVersion: HomeworkVersion, course: Course, klass: Class, term: Term) = HomeworkVersionOutputModel(
        createdBy = homeworkVersion.createdBy,
        sheetId = homeworkVersion.sheetId,
        homeworkName = homeworkVersion.homeworkName,
        dueDate = homeworkVersion.dueDate,
        lateDelivery = homeworkVersion.lateDelivery,
        multipleDeliveries = homeworkVersion.multipleDeliveries,
        homeworkId = homeworkVersion.homeworkId,
        version = homeworkVersion.version,
        courseShortName = course.shortName,
        className = klass.className,
        lecturedTerm = term.shortName
)

fun toHomeworkStagedOutputModel(homeworkStage: HomeworkStage) = HomeworkStageOutputModel(
        createdBy = homeworkStage.createdBy,
        sheetId = homeworkStage.sheetId,
        homeworkName = homeworkStage.homeworkName,
        dueDate = homeworkStage.dueDate,
        lateDelivery = homeworkStage.lateDelivery,
        multipleDeliveries = homeworkStage.multipleDeliveries,
        votes = homeworkStage.votes,
        stagedId = homeworkStage.stageId
)

fun toHomeworkReportOutputModel(homeworkReport: HomeworkReport, course: Course, klass: Class, term: Term) = HomeworkReportOutputModel(
        sheetId = homeworkReport.sheetId,
        dueDate = homeworkReport.dueDate,
        lateDelivery = homeworkReport.lateDelivery,
        homeworkName = homeworkReport.homeworkName,
        multipleDeliveries = homeworkReport.multipleDeliveries,
        homeworkId = homeworkReport.homeworkId,
        votes = homeworkReport.votes,
        reportId = homeworkReport.reportId,
        reportedBy = homeworkReport.reportedBy,
        courseShortName = course.shortName,
        className = klass.className,
        lecturedTerm = term.shortName
)

fun toHomeworkCollectionOutputModel(homeworkList: List<HomeworkOutputModel>) = HomeworkCollectionOutputModel(
        homeworkList = homeworkList
)

fun toHomeworkStageCollectionOutputModel(homeworkStageList: List<HomeworkStageOutputModel>) = HomeworkStageCollectionOutputModel(
        homeworkStageList = homeworkStageList
)

fun toHomeworkReportCollectionOutputModel(homeworkReportList: List<HomeworkReportOutputModel>) = HomeworkReportCollectionOutputModel(
        homeworkReportList = homeworkReportList
)

fun toHomeworkVersionCollectionOutputModel(homeworkVersionList: List<HomeworkVersionOutputModel>) = HomeworkVersionCollectionOutputModel(
        homeworkVersionList = homeworkVersionList
)

fun Homework.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId/courses/$courseId/homeworks/$homeworkId",
        timestamp = actionLog.timestamp
)

fun HomeworkReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId/courses/$courseId/homeworks/$homeworkId/reports/$reportId",
        timestamp = actionLog.timestamp
)

fun HomeworkStage.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId/courses/$courseId/homeworks/stage/$stageId",
        timestamp = actionLog.timestamp
)