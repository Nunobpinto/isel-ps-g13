package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.WorkAssignmentOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.WorkAssignmentCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.WorkAssignmentReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.WorkAssignmentVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.WorkAssignmentReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.WorkAssignmentStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.WorkAssignmentVersionOutputModel

fun toWorkAssignment(input: WorkAssignmentInputModel, createdBy: String) = WorkAssignment(
        createdBy = createdBy,
        sheetId = input.sheetId,
        supplementId = input.supplementId,
        dueDate = input.dueDate,
        individual = input.individual,
        lateDelivery = input.lateDelivery,
        multipleDeliveries = input.multipleDeliveries,
        requiresReport = input.requiresReport
)

fun toWorkAssignmentVersion(workAssignment: WorkAssignment) = WorkAssignmentVersion(
        workAssignmentId = workAssignment.workAssignmentId,
        version = workAssignment.version,
        sheetId = workAssignment.sheetId,
        supplementId = workAssignment.supplementId,
        dueDate = workAssignment.dueDate,
        individual = workAssignment.individual,
        lateDelivery = workAssignment.lateDelivery,
        multipleDeliveries = workAssignment.multipleDeliveries,
        requiresReport = workAssignment.requiresReport,
        createdBy = workAssignment.createdBy,
        timestamp = workAssignment.timestamp
)

fun toWorkAssignmentReport(workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel, reportedBy: String) = WorkAssignmentReport(
        workAssignmentId = workAssignmentId,
        sheetId = inputWorkAssignmentReport.sheetId,
        supplementId = inputWorkAssignmentReport.supplementId,
        dueDate = inputWorkAssignmentReport.dueDate,
        individual = inputWorkAssignmentReport.individual,
        lateDelivery = inputWorkAssignmentReport.lateDelivery,
        multipleDeliveries = inputWorkAssignmentReport.multipleDeliveries,
        requiresReport = inputWorkAssignmentReport.requiresReport,
        reportedBy = reportedBy
)

fun toStageWorkAssignment(inputWorkAssignment: WorkAssignmentInputModel, createdBy: String) = WorkAssignmentStage(
        sheetId = inputWorkAssignment.sheetId,
        supplementId = inputWorkAssignment.supplementId,
        dueDate = inputWorkAssignment.dueDate,
        individual = inputWorkAssignment.individual,
        lateDelivery = inputWorkAssignment.lateDelivery,
        multipleDeliveries = inputWorkAssignment.multipleDeliveries,
        requiresReport = inputWorkAssignment.requiresReport,
        createdBy = createdBy
)

fun stagedToWorkAssignment(stage: WorkAssignmentStage) = WorkAssignment(
        createdBy = stage.createdBy,
        sheetId = stage.sheetId,
        supplementId = stage.supplementId,
        dueDate = stage.dueDate,
        individual = stage.individual,
        lateDelivery = stage.lateDelivery,
        multipleDeliveries = stage.multipleDeliveries,
        requiresReport = stage.requiresReport
)

fun toWorkAssignmentOutputModel(workAssignment: WorkAssignment) = WorkAssignmentOutputModel(
        workAssignmentId = workAssignment.workAssignmentId,
        version = workAssignment.version,
        votes = workAssignment.votes,
        createdBy = workAssignment.createdBy,
        sheetId = workAssignment.sheetId,
        supplementId = workAssignment.supplementId,
        dueDate = workAssignment.dueDate,
        individual = workAssignment.individual,
        lateDelivery = workAssignment.lateDelivery,
        multipleDeliveries = workAssignment.multipleDeliveries,
        requiresReport = workAssignment.requiresReport,
        timestamp = workAssignment.timestamp
)

fun toWorkAssignmentStageOutputModel(workAssignmentStage: WorkAssignmentStage) = WorkAssignmentStageOutputModel(
        stagedId = workAssignmentStage.stageId,
        votes = workAssignmentStage.votes,
        username = workAssignmentStage.createdBy,
        sheetId = workAssignmentStage.sheetId,
        supplementId = workAssignmentStage.supplementId,
        dueDate = workAssignmentStage.dueDate,
        individual = workAssignmentStage.individual,
        lateDelivery = workAssignmentStage.lateDelivery,
        multipleDeliveries = workAssignmentStage.multipleDeliveries,
        requiresReport = workAssignmentStage.requiresReport,
        timestamp = workAssignmentStage.timestamp
)

fun toWorkAssignmentReportOutputModel(workAssignmentReport: WorkAssignmentReport) = WorkAssignmentReportOutputModel(
        workAssignmentId = workAssignmentReport.workAssignmentId,
        reportId = workAssignmentReport.reportId,
        votes = workAssignmentReport.votes,
        reportedBy = workAssignmentReport.reportedBy,
        sheetId = workAssignmentReport.sheetId,
        supplementId = workAssignmentReport.supplementId,
        dueDate = workAssignmentReport.dueDate,
        individual = workAssignmentReport.individual,
        lateDelivery = workAssignmentReport.lateDelivery,
        multipleDeliveries = workAssignmentReport.multipleDeliveries,
        requiresReport = workAssignmentReport.requiresReport,
        timestamp = workAssignmentReport.timestamp
)

fun toWorkAssignmentVersionOutputModel(workAssignmentVersion: WorkAssignmentVersion) = WorkAssignmentVersionOutputModel(
        workAssignmentId = workAssignmentVersion.workAssignmentId,
        version = workAssignmentVersion.version,
        createdBy = workAssignmentVersion.createdBy,
        sheetId = workAssignmentVersion.sheetId,
        supplementId = workAssignmentVersion.supplementId,
        dueDate = workAssignmentVersion.dueDate,
        individual = workAssignmentVersion.individual,
        lateDelivery = workAssignmentVersion.lateDelivery,
        multipleDeliveries = workAssignmentVersion.multipleDeliveries,
        requiresReport = workAssignmentVersion.requiresReport,
        timestamp = workAssignmentVersion.timestamp
)

fun toWorkAssignmentCollectionOutputModel(workAssignmentList: List<WorkAssignmentOutputModel>) = WorkAssignmentCollectionOutputModel(
        workAssignmentList = workAssignmentList
)

fun toWorkAssignmentReportCollectionOutputModel(workAssignmentReportList: List<WorkAssignmentReportOutputModel>) = WorkAssignmentReportCollectionOutputModel(
        workAssignmentReportList = workAssignmentReportList
)

fun toWorkAssignmentVersionCollectionOutputModel(workAssignmentVersionList: List<WorkAssignmentVersionOutputModel>) = WorkAssignmentVersionCollectionOutputModel(
        workAssignmentVersionList = workAssignmentVersionList
)