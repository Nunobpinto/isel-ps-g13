package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.ClassVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.ClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.ClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.ClassStageOutputModel
import java.sql.Timestamp
import java.time.LocalDateTime

fun toClass(input: ClassInputModel) = Class(
        termId = input.termId,
        className = input.className,
        createdBy = input.createdBy
)

fun toClassReport(classId: Int, report: ClassReportInputModel) = ClassReport(
        classId = classId,
        termId = report.termId,
        className = report.className,
        reportedBy = report.reportedBy,
        timestamp = Timestamp.valueOf(LocalDateTime.now())
)

fun stagedToClass(stage: ClassStage) = Class(
        termId = stage.termId,
        className = stage.className,
        createdBy = stage.createdBy
)

fun toClassVersion(klass: Class) = ClassVersion(
        classId = klass.classId,
        termId = klass.termId,
        version = klass.version,
        className = klass.className,
        createdBy = klass.createdBy,
        timestamp = klass.timestamp
)

fun toClassOutputModel(klass: Class, term: Term) = ClassOutputModel(
        classId = klass.classId,
        version = klass.version,
        votes = klass.votes,
        username = klass.createdBy,
        className = klass.className,
        termId = klass.termId,
        lecturedTerm = term.shortName,
        timestamp = klass.timestamp
)

fun toClassStagedOutputModel(classStage: ClassStage, term: Term) = ClassStageOutputModel(
        stagedId = classStage.stageId,
        votes = classStage.votes,
        username = classStage.createdBy,
        className = classStage.className,
        termId = classStage.termId,
        lecturedTerm = term.shortName,
        timestamp = classStage.timestamp
)

fun toClassReportOutputModel(classReport: ClassReport) = ClassReportOutputModel(
        reportId = classReport.reportId,
        classId = classReport.classId,
        className = classReport.className,
        termId = classReport.termId,
        reportedBy = classReport.reportedBy,
        votes = classReport.votes,
        timestamp = classReport.timestamp
)

fun toClassVersionOutputModel(classVersion: ClassVersion, term: Term) = ClassVersionOutputModel(
        classId = classVersion.classId,
        version = classVersion.version,
        createdBy = classVersion.createdBy,
        className = classVersion.className,
        termId = classVersion.termId,
        lecturedTerm = term.shortName,
        timestamp = classVersion.timestamp
)