package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.HomeworkInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.HomeworkReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.HomeworkStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.HomeworkVersionOutputModel

fun toHomework(homeworkInputModel: HomeworkInputModel) = Homework(
        createdBy = homeworkInputModel.createdBy,
        sheet = homeworkInputModel.sheet,
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries
)

fun toHomeworkStage(homeworkInputModel: HomeworkInputModel) = HomeworkStage(
        createdBy = homeworkInputModel.createdBy,
        sheet = homeworkInputModel.sheet,
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries
)

fun stagedToHomework(stagedHomework: HomeworkStage) = Homework(
        createdBy = stagedHomework.createdBy,
        sheet = stagedHomework.sheet,
        dueDate = stagedHomework.dueDate,
        lateDelivery = stagedHomework.lateDelivery,
        multipleDeliveries = stagedHomework.multipleDeliveries
)

fun toHomeworkVersion(homework: Homework) = HomeworkVersion(
        version = homework.version,
        homeworkId = homework.homeworkId,
        createdBy = homework.createdBy,
        sheet = homework.sheet,
        dueDate = homework.dueDate,
        lateDelivery = homework.lateDelivery,
        multipleDeliveries = homework.multipleDeliveries,
        timestamp = homework.timestamp
)

fun toHomeworkReport(homeworkReportInputModel: HomeworkReportInputModel) =  HomeworkReport(
        homeworkId = homeworkReportInputModel.homeworkId,
        sheet = homeworkReportInputModel.sheet,
        dueDate = homeworkReportInputModel.dueDate,
        lateDelivery = homeworkReportInputModel.lateDelivery,
        multipleDeliveries = homeworkReportInputModel.multipleDeliveries,
        reportedBy = homeworkReportInputModel.reportedBy
)

fun toHomeworkOutputModel(homework: Homework) = HomeworkOutputModel(
        username = homework.createdBy,
        sheet = homework.sheet,
        dueDate = homework.dueDate,
        lateDelivery = homework.lateDelivery,
        multipleDeliveries = homework.multipleDeliveries,
        homeworkId = homework.homeworkId,
        votes = homework.votes,
        version = homework.version
)

fun toHomeworkVersionOutputModel(homeworkVersion: HomeworkVersion) = HomeworkVersionOutputModel(
        username = homeworkVersion.createdBy,
        sheet = homeworkVersion.sheet,
        dueDate = homeworkVersion.dueDate,
        lateDelivery = homeworkVersion.lateDelivery,
        multipleDeliveries = homeworkVersion.multipleDeliveries,
        homeworkId = homeworkVersion.homeworkId,
        version = homeworkVersion.version
)

fun toHomeworkStagedOutputModel(homeworkStage: HomeworkStage) = HomeworkStageOutputModel(
        username = homeworkStage.createdBy,
        sheet = homeworkStage.sheet,
        dueDate = homeworkStage.dueDate,
        lateDelivery = homeworkStage.lateDelivery,
        multipleDeliveries = homeworkStage.multipleDeliveries,
        votes = homeworkStage.votes,
        stagedId = homeworkStage.stageId
)

fun toHomeworkReportOutputModel(homeworkReport: HomeworkReport) = HomeworkReportOutputModel(
        sheet = homeworkReport.sheet,
        dueDate = homeworkReport.dueDate,
        lateDelivery = homeworkReport.lateDelivery,
        multipleDeliveries = homeworkReport.multipleDeliveries,
        homeworkId = homeworkReport.homeworkId,
        votes = homeworkReport.votes,
        reportId = homeworkReport.reportId,
        reportedBy = homeworkReport.reportedBy
)