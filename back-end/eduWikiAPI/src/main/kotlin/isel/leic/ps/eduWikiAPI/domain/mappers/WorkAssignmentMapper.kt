package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion

fun toWorkAssignment(input: WorkAssignmentInputModel) = WorkAssignment(
        createdBy = input.createdBy,
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

fun toWorkAssignmentReport(workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel) = WorkAssignmentReport(
        workAssignmentId = workAssignmentId,
        sheetId = inputWorkAssignmentReport.sheetId,
        supplementId = inputWorkAssignmentReport.supplementId,
        dueDate = inputWorkAssignmentReport.dueDate,
        individual = inputWorkAssignmentReport.individual,
        lateDelivery = inputWorkAssignmentReport.lateDelivery,
        multipleDeliveries = inputWorkAssignmentReport.multipleDeliveries,
        requiresReport = inputWorkAssignmentReport.requiresReport,
        reportedBy = inputWorkAssignmentReport.reportedBy
)

fun toStageWorkAssignment(inputWorkAssignment: WorkAssignmentInputModel) = WorkAssignmentStage(
        sheetId = inputWorkAssignment.sheetId,
        supplementId = inputWorkAssignment.supplementId,
        dueDate = inputWorkAssignment.dueDate,
        individual = inputWorkAssignment.individual,
        lateDelivery = inputWorkAssignment.lateDelivery,
        multipleDeliveries = inputWorkAssignment.multipleDeliveries,
        requiresReport = inputWorkAssignment.requiresReport,
        createdBy = inputWorkAssignment.createdBy
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