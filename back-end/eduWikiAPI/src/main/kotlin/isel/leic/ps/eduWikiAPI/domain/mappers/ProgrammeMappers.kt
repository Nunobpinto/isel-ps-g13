package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import java.sql.Timestamp
import java.time.LocalDateTime

fun toProgramme(input: ProgrammeInputModel) = Programme(
        createdBy = input.createdBy,
        fullName = input.fullName,
        shortName = input.shortName,
        academicDegree = input.academicDegree,
        totalCredits = input.totalCredits,
        duration = input.duration
)

fun toProgrammeStage(input: ProgrammeInputModel) = ProgrammeStage(
        createdBy = input.createdBy,
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

fun toProgrammeReport(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel) = ProgrammeReport(
        programmeId = programmeId,
        fullName = inputProgrammeReport.fullName,
        shortName = inputProgrammeReport.shortName,
        academicDegree = inputProgrammeReport.academicDegree,
        duration = inputProgrammeReport.duration,
        totalCredits = inputProgrammeReport.totalCredits,
        reportedBy = inputProgrammeReport.reportedBy
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
