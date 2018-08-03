package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.ExamOutputModel

fun toExam(input: ExamInputModel) = Exam(
        createdBy = input.createdBy,
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
        sheetId = exam.sheetId,
        dueDate = exam.dueDate,
        type = exam.type,
        phase = exam.phase,
        location = exam.location
)

fun toExamReport(examId: Int, inputExamReport: ExamReportInputModel) =  ExamReport(
        examId = examId,
        sheetId = inputExamReport.sheetId,
        dueDate = inputExamReport.dueDate,
        type = inputExamReport.type,
        phase = inputExamReport.phase,
        location = inputExamReport.location,
        reportedBy = inputExamReport.reportedBy
)

fun toStageExam(inputExam: ExamInputModel) = ExamStage(
        dueDate = inputExam.dueDate,
        type = inputExam.type,
        phase = inputExam.phase,
        location = inputExam.location,
        createdBy = inputExam.createdBy
)

fun stagedToExam(stage: ExamStage) = Exam(
        createdBy = stage.createdBy,
        sheetId = stage.sheetId,
        dueDate = stage.dueDate,
        type = stage.type,
        phase = stage.phase,
        location = stage.location
)

fun toExamOutputModel(exam: Exam) = ExamOutputModel(
        examId = exam.examId,
        version = exam.version,
        votes = exam.votes,
        username = exam.createdBy,
        location = exam.location,
        dueDate = exam.dueDate,
        type = exam.type,
        phase = exam.phase,
        //sheet = exam.sheet,
        timestamp = exam.timestamp
)