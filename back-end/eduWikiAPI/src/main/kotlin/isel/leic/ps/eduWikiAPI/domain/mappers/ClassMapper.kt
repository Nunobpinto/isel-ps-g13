package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ClassVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ClassReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ClassStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ClassVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ClassStageOutputModel
import java.security.Principal
import java.sql.Timestamp
import java.time.LocalDateTime

fun toClass(input: ClassInputModel, createdBy: String) = Class(
        termId = input.termId,
        className = input.className,
        createdBy = createdBy,
        programmeId = input.programmeId
)

fun toClassStage(input: ClassInputModel, createdBy: String): ClassStage = ClassStage(
        termId = input.termId,
        className = input.className,
        createdBy = createdBy,
        programmeId = input.programmeId
)

fun toClassReport(klass: Class, report: ClassReportInputModel, reportedBy: String) = ClassReport(
        classId = klass.classId,
        termId = klass.termId,
        className = report.className,
        reportedBy = reportedBy,
        timestamp = Timestamp.valueOf(LocalDateTime.now()),
        programmeId = report.programmeId
)

fun stagedToClass(stage: ClassStage) = Class(
        termId = stage.termId,
        className = stage.className,
        createdBy = stage.createdBy,
        programmeId = stage.programmeId
)

fun toClassVersion(klass: Class) = ClassVersion(
        classId = klass.classId,
        termId = klass.termId,
        version = klass.version,
        className = klass.className,
        createdBy = klass.createdBy,
        timestamp = klass.timestamp,
        programmeId = klass.programmeId
)

fun toClassOutputModel(klass: Class, term: Term, programme: Programme) = ClassOutputModel(
        classId = klass.classId,
        version = klass.version,
        votes = klass.votes,
        createdBy = klass.createdBy,
        className = klass.className,
        termId = klass.termId,
        lecturedTerm = term.shortName,
        timestamp = klass.timestamp,
        programmeId = programme.programmeId,
        programmeShortName = programme.shortName
)

fun toClassStagedOutputModel(classStage: ClassStage, term: Term, programme: Programme) = ClassStageOutputModel(
        stagedId = classStage.stageId,
        votes = classStage.votes,
        createdBy = classStage.createdBy,
        className = classStage.className,
        termId = classStage.termId,
        lecturedTerm = term.shortName,
        timestamp = classStage.timestamp,
        programmeId = programme.programmeId,
        programmeShortName = programme.shortName
)

fun toClassReportOutputModel(classReport: ClassReport, term: Term, programme: Programme?) = ClassReportOutputModel(
        reportId = classReport.reportId,
        classId = classReport.classId,
        className = classReport.className,
        termId = classReport.termId,
        reportedBy = classReport.reportedBy,
        votes = classReport.votes,
        timestamp = classReport.timestamp,
        lecturedTerm = term.shortName,
        programmeShortName = programme?.shortName,
        programmeId = programme?.programmeId
)

fun toClassVersionOutputModel(classVersion: ClassVersion, term: Term, programme: Programme) = ClassVersionOutputModel(
        classId = classVersion.classId,
        version = classVersion.version,
        createdBy = classVersion.createdBy,
        className = classVersion.className,
        termId = classVersion.termId,
        lecturedTerm = term.shortName,
        timestamp = classVersion.timestamp,
        programmeShortName = programme.shortName,
        programmeId = programme.programmeId
)

fun toClassCollectionOutputModel(classList: List<ClassOutputModel>) = ClassCollectionOutputModel(
        classList = classList
)

fun toClassReportCollectionOutputModel(classReportList: List<ClassReportOutputModel>) = ClassReportCollectionOutputModel(
        classReportList = classReportList
)

fun toClassStageCollectionOutputModel(classStageList: List<ClassStageOutputModel>) = ClassStageCollectionOutputModel(
        classStageList = classStageList
)

fun toClassVersionCollectionOutputModel(classVersionList: List<ClassVersionOutputModel>) = ClassVersionCollectionOutputModel(
        classVersionList = classVersionList
)

fun Class.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId",
        timestamp = actionLog.timestamp
)

fun ClassReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId/reports/$reportId",
        timestamp = actionLog.timestamp
)

fun ClassStage.toUserActionOutputModel(actionLog: ActionLog)= UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/stage/$stageId",
        timestamp = actionLog.timestamp
)