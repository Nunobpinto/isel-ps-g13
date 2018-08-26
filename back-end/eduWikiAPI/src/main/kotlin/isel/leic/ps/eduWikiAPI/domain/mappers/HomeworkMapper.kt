package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.HomeworkInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.Homework
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
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries,
        sheetId = if(sheet == null) null else UUID.randomUUID()
)

fun toHomeworkStage(homeworkInputModel: HomeworkInputModel, sheet: MultipartFile?, createdBy: String) = HomeworkStage(
        createdBy = createdBy,
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries,
        sheetId = if(sheet == null) null else UUID.randomUUID()
)

fun stagedToHomework(stagedHomework: HomeworkStage) = Homework(
        createdBy = stagedHomework.createdBy,
        sheetId = stagedHomework.sheetId,
        dueDate = stagedHomework.dueDate,
        lateDelivery = stagedHomework.lateDelivery,
        multipleDeliveries = stagedHomework.multipleDeliveries
)

fun toHomeworkVersion(homework: Homework) = HomeworkVersion(
        version = homework.version,
        homeworkId = homework.homeworkId,
        createdBy = homework.createdBy,
        sheetId = homework.sheetId,
        dueDate = homework.dueDate,
        lateDelivery = homework.lateDelivery,
        multipleDeliveries = homework.multipleDeliveries,
        timestamp = homework.timestamp
)

fun toHomeworkReport(homeworkReportInputModel: HomeworkReportInputModel, homeworkId: Int, reportedBy: String) =  HomeworkReport(
        homeworkId = homeworkId,
        sheetId = homeworkReportInputModel.sheetId,
        dueDate = homeworkReportInputModel.dueDate,
        lateDelivery = homeworkReportInputModel.lateDelivery,
        multipleDeliveries = homeworkReportInputModel.multipleDeliveries,
        reportedBy = reportedBy
)

fun toHomeworkOutputModel(homework: Homework) = HomeworkOutputModel(
        createdBy = homework.createdBy,
        sheetId = homework.sheetId,
        dueDate = homework.dueDate,
        lateDelivery = homework.lateDelivery,
        multipleDeliveries = homework.multipleDeliveries,
        homeworkId = homework.homeworkId,
        votes = homework.votes,
        version = homework.version
)

fun toHomeworkVersionOutputModel(homeworkVersion: HomeworkVersion) = HomeworkVersionOutputModel(
        createdBy = homeworkVersion.createdBy,
        sheetId = homeworkVersion.sheetId,
        dueDate = homeworkVersion.dueDate,
        lateDelivery = homeworkVersion.lateDelivery,
        multipleDeliveries = homeworkVersion.multipleDeliveries,
        homeworkId = homeworkVersion.homeworkId,
        version = homeworkVersion.version
)

fun toHomeworkStagedOutputModel(homeworkStage: HomeworkStage) = HomeworkStageOutputModel(
        createdBy = homeworkStage.createdBy,
        sheetId = homeworkStage.sheetId,
        dueDate = homeworkStage.dueDate,
        lateDelivery = homeworkStage.lateDelivery,
        multipleDeliveries = homeworkStage.multipleDeliveries,
        votes = homeworkStage.votes,
        stagedId = homeworkStage.stageId
)

fun toHomeworkReportOutputModel(homeworkReport: HomeworkReport) = HomeworkReportOutputModel(
        sheetId = homeworkReport.sheetId,
        dueDate = homeworkReport.dueDate,
        lateDelivery = homeworkReport.lateDelivery,
        multipleDeliveries = homeworkReport.multipleDeliveries,
        homeworkId = homeworkReport.homeworkId,
        votes = homeworkReport.votes,
        reportId = homeworkReport.reportId,
        reportedBy = homeworkReport.reportedBy
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