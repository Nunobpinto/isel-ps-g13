package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.WorkAssignmentOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.WorkAssignmentCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.WorkAssignmentReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.WorkAssignmentStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.WorkAssignmentVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.WorkAssignmentReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.WorkAssignmentStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.WorkAssignmentVersionOutputModel
import org.springframework.web.multipart.MultipartFile
import java.util.*

fun toWorkAssignment(input: WorkAssignmentInputModel, sheet: MultipartFile?, supplement: MultipartFile?, createdBy: String) = WorkAssignment(
        createdBy = createdBy,
        phase = input.phase,
        dueDate = input.dueDate,
        individual = input.individual,
        lateDelivery = input.lateDelivery,
        multipleDeliveries = input.multipleDeliveries,
        requiresReport = input.requiresReport,
        sheetId = if (sheet == null) null else UUID.randomUUID(),
        supplementId = if (supplement == null) null else UUID.randomUUID()
)

fun toWorkAssignmentVersion(workAssignment: WorkAssignment) = WorkAssignmentVersion(
        workAssignmentId = workAssignment.workAssignmentId,
        version = workAssignment.version,
        sheetId = workAssignment.sheetId,
        phase = workAssignment.phase,
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
        phase = inputWorkAssignmentReport.phase,
        supplementId = inputWorkAssignmentReport.supplementId,
        dueDate = inputWorkAssignmentReport.dueDate,
        individual = inputWorkAssignmentReport.individual,
        lateDelivery = inputWorkAssignmentReport.lateDelivery,
        multipleDeliveries = inputWorkAssignmentReport.multipleDeliveries,
        requiresReport = inputWorkAssignmentReport.requiresReport,
        reportedBy = reportedBy
)

fun toStageWorkAssignment(inputWorkAssignment: WorkAssignmentInputModel, sheet: MultipartFile?, supplement: MultipartFile?, createdBy: String) = WorkAssignmentStage(
        phase = inputWorkAssignment.phase,
        dueDate = inputWorkAssignment.dueDate,
        individual = inputWorkAssignment.individual,
        lateDelivery = inputWorkAssignment.lateDelivery,
        multipleDeliveries = inputWorkAssignment.multipleDeliveries,
        requiresReport = inputWorkAssignment.requiresReport,
        sheetId = if (sheet == null) null else UUID.randomUUID(),
        supplementId = if (supplement == null) null else UUID.randomUUID(),
        createdBy = createdBy
)

fun stagedToWorkAssignment(stage: WorkAssignmentStage) = WorkAssignment(
        createdBy = stage.createdBy,
        sheetId = stage.sheetId,
        supplementId = stage.supplementId,
        phase = stage.phase,
        dueDate = stage.dueDate,
        individual = stage.individual,
        lateDelivery = stage.lateDelivery,
        multipleDeliveries = stage.multipleDeliveries,
        requiresReport = stage.requiresReport
)

fun toWorkAssignmentOutputModel(workAssignment: WorkAssignment, course: Course, term: Term) = WorkAssignmentOutputModel(
        workAssignmentId = workAssignment.workAssignmentId,
        version = workAssignment.version,
        votes = workAssignment.votes,
        createdBy = workAssignment.createdBy,
        phase = workAssignment.phase,
        sheetId = workAssignment.sheetId,
        supplementId = workAssignment.supplementId,
        dueDate = workAssignment.dueDate,
        individual = workAssignment.individual,
        lateDelivery = workAssignment.lateDelivery,
        multipleDeliveries = workAssignment.multipleDeliveries,
        requiresReport = workAssignment.requiresReport,
        timestamp = workAssignment.timestamp,
        courseShortName = course.shortName,
        termShortName = term.shortName
)

fun toWorkAssignmentStageOutputModel(workAssignmentStage: WorkAssignmentStage) = WorkAssignmentStageOutputModel(
        stagedId = workAssignmentStage.stageId,
        votes = workAssignmentStage.votes,
        createdBy = workAssignmentStage.createdBy,
        phase = workAssignmentStage.phase,
        sheetId = workAssignmentStage.sheetId,
        supplementId = workAssignmentStage.supplementId,
        dueDate = workAssignmentStage.dueDate,
        individual = workAssignmentStage.individual,
        lateDelivery = workAssignmentStage.lateDelivery,
        multipleDeliveries = workAssignmentStage.multipleDeliveries,
        requiresReport = workAssignmentStage.requiresReport,
        timestamp = workAssignmentStage.timestamp
)

fun toWorkAssignmentReportOutputModel(workAssignmentReport: WorkAssignmentReport, course: Course, term: Term) = WorkAssignmentReportOutputModel(
        workAssignmentId = workAssignmentReport.workAssignmentId,
        reportId = workAssignmentReport.reportId,
        votes = workAssignmentReport.votes,
        phase = workAssignmentReport.phase,
        reportedBy = workAssignmentReport.reportedBy,
        sheetId = workAssignmentReport.sheetId,
        supplementId = workAssignmentReport.supplementId,
        dueDate = workAssignmentReport.dueDate,
        individual = workAssignmentReport.individual,
        lateDelivery = workAssignmentReport.lateDelivery,
        multipleDeliveries = workAssignmentReport.multipleDeliveries,
        requiresReport = workAssignmentReport.requiresReport,
        timestamp = workAssignmentReport.timestamp,
        courseShortName = course.shortName,
        termShortName = term.shortName
)

fun toWorkAssignmentVersionOutputModel(workAssignmentVersion: WorkAssignmentVersion, course: Course, term: Term) = WorkAssignmentVersionOutputModel(
        workAssignmentId = workAssignmentVersion.workAssignmentId,
        version = workAssignmentVersion.version,
        createdBy = workAssignmentVersion.createdBy,
        sheetId = workAssignmentVersion.sheetId,
        phase = workAssignmentVersion.phase,
        supplementId = workAssignmentVersion.supplementId,
        dueDate = workAssignmentVersion.dueDate,
        individual = workAssignmentVersion.individual,
        lateDelivery = workAssignmentVersion.lateDelivery,
        multipleDeliveries = workAssignmentVersion.multipleDeliveries,
        requiresReport = workAssignmentVersion.requiresReport,
        timestamp = workAssignmentVersion.timestamp,
        courseShortName = course.shortName,
        termShortName = term.shortName
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

fun toWorkAssignmentStageCollectionOutputModel(workAssignmentStageList: List<WorkAssignmentStageOutputModel>) = WorkAssignmentStageCollectionOutputModel(
        workAssignmentStageList = workAssignmentStageList
)

fun WorkAssignment.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "courses/$courseId/terms/$termId/work-assignments/$workAssignmentId",
        timestamp = actionLog.timestamp
)

fun WorkAssignmentReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "courses/$courseId/terms/$termId/work-assignments/$workAssignmentId/reports/$reportId",
        timestamp = actionLog.timestamp
)

fun WorkAssignmentStage.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "courses/$courseId/terms/$termId/work-assignments/stage/$stageId",
        timestamp = actionLog.timestamp
)