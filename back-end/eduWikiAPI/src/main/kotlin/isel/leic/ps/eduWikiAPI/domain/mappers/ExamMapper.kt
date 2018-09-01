package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ExamOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ExamCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ExamReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ExamStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ExamVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ExamReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ExamStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ExamVersionOutputModel
import org.springframework.web.multipart.MultipartFile
import java.util.*

fun toExam(input: ExamInputModel, sheet: MultipartFile?, createdBy: String) = Exam(
        createdBy = createdBy,
        dueDate = input.dueDate,
        type = ExamType.valueOf(input.type.toUpperCase()),
        phase = input.phase,
        location = input.location,
        sheetId = if (sheet == null) null else UUID.randomUUID()
)

fun toExamVersion(exam: Exam) = ExamVersion(
        examId = exam.examId,
        version = exam.version,
        createdBy = exam.createdBy,
        timestamp = exam.timestamp,
        sheetId = exam.sheetId,
        dueDate = exam.dueDate,
        type = exam.type,
        phase = exam.phase,
        location = exam.location
)

fun toExamReport(examId: Int, inputExamReport: ExamReportInputModel, reportedBy: String) = ExamReport(
        examId = examId,
        reportedBy = reportedBy,
        sheetId = inputExamReport.sheetId,
        dueDate = inputExamReport.dueDate,
        type = inputExamReport.type,
        phase = inputExamReport.phase,
        location = inputExamReport.location
)

fun toStageExam(inputExam: ExamInputModel, sheet: MultipartFile?, createdBy: String) = ExamStage(
        createdBy = createdBy,
        dueDate = inputExam.dueDate,
        type = ExamType.valueOf(inputExam.type.toUpperCase()),
        phase = inputExam.phase,
        location = inputExam.location,
        sheetId = if (sheet == null) null else UUID.randomUUID()
)

fun stagedToExam(stage: ExamStage) = Exam(
        createdBy = stage.createdBy,
        sheetId = stage.sheetId,
        dueDate = stage.dueDate,
        type = stage.type,
        phase = stage.phase,
        location = stage.location
)

fun toExamOutputModel(exam: Exam, course: Course, term: Term) = ExamOutputModel(
        examId = exam.examId,
        version = exam.version,
        votes = exam.votes,
        createdBy = exam.createdBy,
        location = exam.location,
        dueDate = exam.dueDate,
        type = exam.type,
        phase = exam.phase,
        sheetId = exam.sheetId,
        timestamp = exam.timestamp,
        courseShortName = course.shortName,
        termShortName = term.shortName
)

fun toExamStageOutputModel(examStage: ExamStage) = ExamStageOutputModel(
        stagedId = examStage.stageId,
        votes = examStage.votes,
        createdBy = examStage.createdBy,
        location = examStage.location,
        dueDate = examStage.dueDate,
        type = examStage.type,
        phase = examStage.phase,
        sheetId = examStage.sheetId,
        timestamp = examStage.timestamp
)

fun toExamReportOutputModel(examReport: ExamReport, course: Course, term: Term) = ExamReportOutputModel(
        examId = examReport.examId,
        reportedBy = examReport.reportedBy,
        votes = examReport.votes,
        location = examReport.location,
        dueDate = examReport.dueDate,
        type = examReport.type,
        phase = examReport.phase,
        sheetId = examReport.sheetId,
        timestamp = examReport.timestamp,
        courseShortName = course.shortName,
        termShortName = term.shortName,
        reportId = examReport.reportId
)

fun toExamVersionOutputModel(examVersion: ExamVersion, course: Course, term: Term) = ExamVersionOutputModel(
        examId = examVersion.examId,
        version = examVersion.version,
        createdBy = examVersion.createdBy,
        location = examVersion.location,
        dueDate = examVersion.dueDate,
        type = examVersion.type,
        phase = examVersion.phase,
        sheetId = examVersion.sheetId,
        timestamp = examVersion.timestamp,
        courseShortName = course.shortName,
        termShortName = term.shortName
)

fun toExamCollectionOutputModel(examList: List<ExamOutputModel>) = ExamCollectionOutputModel(
        examList = examList
)

fun toExamReportCollectionOutputModel(examReportList: List<ExamReportOutputModel>) = ExamReportCollectionOutputModel(
        examReportList = examReportList
)

fun toExamVersionCollectionOutputModel(examVersionList: List<ExamVersionOutputModel>) = ExamVersionCollectionOutputModel(
        examVersionList = examVersionList
)

fun toExamStageCollectionOutputModel(examStageList: List<ExamStageOutputModel>) = ExamStageCollectionOutputModel(
        examStageList = examStageList
)

fun Exam.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "courses/$courseId/terms/$termId/exams/$examId",
        timestamp = actionLog.timestamp
)

fun ExamReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "courses/$courseId/terms/$termId/exams/$examId/reports/$reportId",
        timestamp = actionLog.timestamp
)

fun ExamStage.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "courses/$courseId/terms/$termId/exams/stage/$stageId",
        timestamp = actionLog.timestamp
)