package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import java.sql.Timestamp
import java.time.LocalDateTime

fun toClass(input: ClassInputModel) = Class(
        termId = input.termId,
        className = input.className,
        createdBy = input.createdBy
)

fun toReport(classId: Int, report: ClassReportInputModel) = ClassReport(
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