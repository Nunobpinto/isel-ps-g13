package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.HomeworkInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ClassMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion

fun toHomework(classMiscUnit: ClassMiscUnit, homeworkInputModel: HomeworkInputModel) = Homework(
        homeworkId = classMiscUnit.classMiscUnitId,
        createdBy = homeworkInputModel.createdBy,
        sheet = homeworkInputModel.sheet,
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries
)

fun toHomeworkStaged(classMiscUnitStaged: ClassMiscUnitStage, homeworkInputModel: HomeworkInputModel) = HomeworkStage(
        stageId = classMiscUnitStaged.stageId,
        createdBy = homeworkInputModel.createdBy,
        sheet = homeworkInputModel.sheet,
        dueDate = homeworkInputModel.dueDate,
        lateDelivery = homeworkInputModel.lateDelivery,
        multipleDeliveries = homeworkInputModel.multipleDeliveries
)

fun stagedToHomework(classMiscUnit: ClassMiscUnit, stagedHomework: HomeworkStage) = Homework(
        homeworkId = classMiscUnit.classMiscUnitId,
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

fun toReport(homeworkReportInputModel: HomeworkReportInputModel) =  HomeworkReport(
        homeworkId = homeworkReportInputModel.homeworkId,
        sheet = homeworkReportInputModel.sheet,
        dueDate = homeworkReportInputModel.dueDate,
        lateDelivery = homeworkReportInputModel.lateDelivery,
        multipleDeliveries = homeworkReportInputModel.multipleDeliveries,
        reportedBy = homeworkReportInputModel.reportedBy
)