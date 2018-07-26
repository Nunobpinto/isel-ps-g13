package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion

fun toWorkAssignment(input: WorkAssignmentInputModel) = WorkAssignment(
        createdBy = input.createdBy,
        sheet = input.sheet,
        supplement = input.supplement,
        dueDate = input.dueDate,
        individual = input.individual,
        lateDelivery = input.lateDelivery,
        multipleDeliveries = input.multipleDeliveries,
        requiresReport = input.requiresReport
)

fun toWorkAssignmentVersion(workAssignment: WorkAssignment) = WorkAssignmentVersion(
        workAssignmentId = workAssignment.workAssignmentId,
        version = workAssignment.version,
        sheet = workAssignment.sheet,
        supplement = workAssignment.supplement,
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
        sheet = inputWorkAssignmentReport.sheet,
        supplement = inputWorkAssignmentReport.supplement,
        dueDate = inputWorkAssignmentReport.dueDate,
        individual = inputWorkAssignmentReport.individual,
        lateDelivery = inputWorkAssignmentReport.lateDelivery,
        multipleDeliveries = inputWorkAssignmentReport.multipleDeliveries,
        requiresReport = inputWorkAssignmentReport.requiresReport,
        reportedBy = inputWorkAssignmentReport.reportedBy
)

fun toStageWorkAssignment(inputWorkAssignment: WorkAssignmentInputModel) = WorkAssignmentStage(
        sheet = inputWorkAssignment.sheet,
        supplement = inputWorkAssignment.supplement,
        dueDate = inputWorkAssignment.dueDate,
        individual = inputWorkAssignment.individual,
        lateDelivery = inputWorkAssignment.lateDelivery,
        multipleDeliveries = inputWorkAssignment.multipleDeliveries,
        requiresReport = inputWorkAssignment.requiresReport,
        createdBy = inputWorkAssignment.createdBy
)

fun stagedToWorkAssignment(stage: WorkAssignmentStage) = WorkAssignment(
        createdBy = stage.createdBy,
        sheet = stage.sheet,
        supplement = stage.supplement,
        dueDate = stage.dueDate,
        individual = stage.individual,
        lateDelivery = stage.lateDelivery,
        multipleDeliveries = stage.multipleDeliveries,
        requiresReport = stage.requiresReport
)