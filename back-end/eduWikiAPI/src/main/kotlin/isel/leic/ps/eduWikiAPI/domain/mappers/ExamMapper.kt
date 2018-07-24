package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion

fun toExam(input: ExamInputModel) = Exam(
        createdBy = input.createdBy,
        sheet = input.sheet,
        dueDate = input.dueDate,
        type = input.type,
        phase = input.phase,
        location = input.location
)

fun toExamVersion(exam: Exam) = ExamVersion(
        examId = exam.examId,
        version = exam.version,
        createdBy = exam.createdBy,
        timestamp = exam.timestamp,
        sheet = exam.sheet,
        dueDate = exam.dueDate,
        type = exam.type,
        phase = exam.phase,
        location = exam.location
)

fun toReport(examId: Int, inputExamReport: ExamReportInputModel) =  ExamReport(
        examId = examId,
        sheet = inputExamReport.sheet,
        dueDate = inputExamReport.dueDate,
        type = inputExamReport.type,
        phase = inputExamReport.phase,
        location = inputExamReport.location,
        reportedBy = inputExamReport.reportedBy
)

fun toStagedExam(inputExam: ExamInputModel) = ExamStage(
        sheet = inputExam.sheet,
        dueDate = inputExam.dueDate,
        type = inputExam.type,
        phase = inputExam.phase,
        location = inputExam.location,
        createdBy = inputExam.createdBy
)

fun stageToExam(stage: ExamStage) = Exam(
        createdBy = stage.createdBy,
        sheet = stage.sheet,
        dueDate = stage.dueDate,
        type = stage.type,
        phase = stage.phase,
        location = stage.location
)