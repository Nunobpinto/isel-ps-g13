package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
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

fun toProgrammeStaged(input: ProgrammeInputModel) = ProgrammeStage(
        createdBy = input.createdBy,
        fullName = input.fullName,
        shortName = input.shortName,
        academicDegree = input.academicDegree,
        totalCredits = input.totalCredits,
        duration = input.duration
)

fun stageToProgramme(stage: ProgrammeStage) = Programme(
        createdBy = stage.createdBy,
        fullName = stage.fullName,
        shortName = stage.shortName,
        academicDegree = stage.academicDegree,
        totalCredits = stage.totalCredits,
        duration = stage.duration
)

fun toReport(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel) = ProgrammeReport(
        programmeId = programmeId,
        programmeFullName = inputProgrammeReport.fullName,
        programmeShortName = inputProgrammeReport.shortName,
        programmeAcademicDegree = inputProgrammeReport.academicDegree,
        programmeDuration = inputProgrammeReport.duration,
        programmeTotalCredits = inputProgrammeReport.totalCredits,
        reportedBy = inputProgrammeReport.reportedBy,
        timestamp = Timestamp.valueOf(LocalDateTime.now())
)