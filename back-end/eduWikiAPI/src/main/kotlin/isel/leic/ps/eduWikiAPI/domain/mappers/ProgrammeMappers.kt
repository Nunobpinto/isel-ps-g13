package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ProgrammeCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ProgrammeReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ProgrammeStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ProgrammeVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ProgrammeVersionOutputModel

fun toProgramme(input: ProgrammeInputModel, createdBy: String) = Programme(
        createdBy = createdBy,
        fullName = input.fullName,
        shortName = input.shortName,
        academicDegree = input.academicDegree,
        totalCredits = input.totalCredits,
        duration = input.duration
)

fun toProgrammeStage(input: ProgrammeInputModel, createdBy: String) = ProgrammeStage(
        createdBy = createdBy,
        fullName = input.fullName,
        shortName = input.shortName,
        academicDegree = input.academicDegree,
        totalCredits = input.totalCredits,
        duration = input.duration
)

fun stagedToProgramme(stage: ProgrammeStage) = Programme(
        createdBy = stage.createdBy,
        fullName = stage.fullName,
        shortName = stage.shortName,
        academicDegree = stage.academicDegree,
        totalCredits = stage.totalCredits,
        duration = stage.duration
)

fun toProgrammeReport(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel, reportedBy: String) = ProgrammeReport(
        programmeId = programmeId,
        reportedBy = reportedBy,
        fullName = inputProgrammeReport.fullName,
        shortName = inputProgrammeReport.shortName,
        academicDegree = inputProgrammeReport.academicDegree,
        duration = inputProgrammeReport.duration,
        totalCredits = inputProgrammeReport.totalCredits
)

fun toProgrammeVersion(programme: Programme) = ProgrammeVersion(
        version = programme.version,
        programmeId = programme.programmeId,
        fullName = programme.fullName,
        shortName = programme.shortName,
        academicDegree = programme.academicDegree,
        totalCredits = programme.totalCredits,
        duration = programme.duration,
        createdBy = programme.createdBy,
        timestamp = programme.timestamp
)

fun toProgrammeOutput(programme: Programme) = ProgrammeOutputModel(
        programmeId = programme.programmeId,
        version = programme.version,
        votes = programme.votes,
        duration = programme.duration,
        fullName = programme.fullName,
        shortName = programme.shortName,
        academicDegree = programme.academicDegree,
        timestamp = programme.timestamp,
        totalCredits = programme.totalCredits,
        createdBy = programme.createdBy
)

fun toProgrammeStageOutputModel(programmeStage: ProgrammeStage) = ProgrammeStageOutputModel(
        stagedId = programmeStage.stageId,
        votes = programmeStage.votes,
        duration = programmeStage.duration,
        fullName = programmeStage.fullName,
        shortName = programmeStage.shortName,
        academicDegree = programmeStage.academicDegree,
        timestamp = programmeStage.timestamp,
        totalCredits = programmeStage.totalCredits,
        createdBy = programmeStage.createdBy
)

fun toProgrammeReportOutputModel(programmeReport: ProgrammeReport) = ProgrammeReportOutputModel(
        reportId = programmeReport.reportId,
        programmeId = programmeReport.programmeId,
        fullName = programmeReport.fullName,
        shortName = programmeReport.shortName,
        academicDegree = programmeReport.academicDegree,
        totalCredits = programmeReport.totalCredits,
        duration = programmeReport.duration,
        reportedBy = programmeReport.reportedBy,
        votes = programmeReport.votes,
        timestamp = programmeReport.timestamp
)

fun toProgrammeVersionOutputModel(programmeVersion: ProgrammeVersion) = ProgrammeVersionOutputModel(
        version = programmeVersion.version,
        programmeId = programmeVersion.programmeId,
        fullName = programmeVersion.fullName,
        shortName = programmeVersion.shortName,
        academicDegree = programmeVersion.academicDegree,
        totalCredits = programmeVersion.totalCredits,
        duration = programmeVersion.duration,
        timestamp = programmeVersion.timestamp,
        createdBy = programmeVersion.createdBy
)

fun toProgrammeCollectionOutputModel(programmeList: List<ProgrammeOutputModel>) = ProgrammeCollectionOutputModel(
        programmeList = programmeList
)

fun toProgrammeStageCollectionOutputModel(programmeStageList: List<ProgrammeStageOutputModel>) = ProgrammeStageCollectionOutputModel(
        programmeStageList = programmeStageList
)

fun toProgrammeReportCollectionOutputModel(programmeReportList: List<ProgrammeReportOutputModel>) = ProgrammeReportCollectionOutputModel(
        programmeReportList = programmeReportList
)

fun toProgrammeVersionCollectionOutputModel(programmeVersionList: List<ProgrammeVersionOutputModel>) = ProgrammeVersionCollectionOutputModel(
        programmeVersionList = programmeVersionList
)