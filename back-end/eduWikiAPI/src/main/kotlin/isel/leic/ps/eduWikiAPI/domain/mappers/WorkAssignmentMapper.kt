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

fun toWorkAssignmentVersion(wrs: WorkAssignment) = WorkAssignmentVersion(
        workAssignmentId = wrs.workAssignmentId,
        version = wrs.version,
        sheet = wrs.sheet,
        supplement = wrs.supplement,
        dueDate = wrs.dueDate,
        individual = wrs.individual,
        lateDelivery = wrs.lateDelivery,
        multipleDeliveries = wrs.multipleDeliveries,
        requiresReport = wrs.requiresReport,
        createdBy = wrs.createdBy,
        timestamp = wrs.timestamp
)

fun toReport(workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel) =  WorkAssignmentReport(
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

fun toStagedWorkAssignment(inputWorkAssignment: WorkAssignmentInputModel) = WorkAssignmentStage(
        sheet = inputWorkAssignment.sheet,
        supplement = inputWorkAssignment.supplement,
        dueDate = inputWorkAssignment.dueDate,
        individual = inputWorkAssignment.individual,
        lateDelivery = inputWorkAssignment.lateDelivery,
        multipleDeliveries = inputWorkAssignment.multipleDeliveries,
        requiresReport = inputWorkAssignment.requiresReport,
        createdBy = inputWorkAssignment.createdBy
)

fun stageToWorkAssignment(stage: WorkAssignmentStage) = WorkAssignment(
        createdBy = stage.createdBy,
        sheet = stage.sheet,
        supplement = stage.supplement,
        dueDate = stage.dueDate,
        individual = stage.individual,
        lateDelivery = stage.lateDelivery,
        multipleDeliveries = stage.multipleDeliveries,
        requiresReport = stage.requiresReport
)