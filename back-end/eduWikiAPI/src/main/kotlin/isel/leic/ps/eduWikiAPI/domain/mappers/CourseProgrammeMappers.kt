package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.CourseProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.CourseProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.CourseProgrammeVersionOutputModel

fun toCourseProgramme(input: CourseProgrammeInputModel) = Course(
        courseId = input.courseId,
        lecturedTerm = input.lecturedTerm,
        programmeId = input.programmeId,
        credits = input.credits,
        optional = input.optional,
        createdBy = input.createdBy
)

fun toCourseProgrammeReport(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel) = CourseProgrammeReport(
        courseId = courseId,
        programmeId = programmeId,
        reportedBy = inputCourseReport.reportedBy,
        lecturedTerm = inputCourseReport.lecturedTerm,
        optional = inputCourseReport.optional,
        credits = inputCourseReport.credits
)

fun toCourseProgrammeStage(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel) = CourseProgrammeStage(
        courseId = inputCourseProgramme.courseId,
        programmeId = programmeId,
        credits = inputCourseProgramme.credits,
        optional = inputCourseProgramme.optional,
        lecturedTerm = inputCourseProgramme.lecturedTerm,
        createdBy = inputCourseProgramme.createdBy
)

fun stagedToCourseProgramme(programmeId: Int, courseProgrammeStage: CourseProgrammeStage) = Course(
        createdBy = courseProgrammeStage.createdBy,
        programmeId = programmeId,
        lecturedTerm = courseProgrammeStage.lecturedTerm,
        optional = courseProgrammeStage.optional,
        credits = courseProgrammeStage.credits
)

fun toCourseProgrammeVersion(course: Course) = CourseProgrammeVersion(
        version = course.version,
        courseId = course.courseId,
        programmeId = course.programmeId,
        lecturedTerm = course.lecturedTerm,
        optional = course.optional,
        credits = course.credits,
        timestamp = course.timestamp,
        createdBy = course.createdBy
)

fun toCourseProgrammeOutputModel(course: Course) = CourseProgrammeOutputModel(
        courseId = course.courseId,
        organizationId = course.organizationId,
        version = course.version,
        votes = course.votes,
        timestamp = course.timestamp,
        fullName = course.fullName,
        shortName = course.shortName,
        username = course.createdBy,
        programmeId = course.programmeId,
        optional = course.optional,
        credits = course.credits,
        lecturedTerm = course.lecturedTerm
)

fun toCourseProgrammeReportOutputModel(courseProgrammeReport: CourseProgrammeReport) = CourseProgrammeReportOutputModel(
        reportId = courseProgrammeReport.reportId,
        courseId = courseProgrammeReport.courseId,
        programmeId = courseProgrammeReport.programmeId,
        lecturedTerm = courseProgrammeReport.lecturedTerm,
        optional = courseProgrammeReport.optional,
        credits = courseProgrammeReport.credits,
        timestamp =courseProgrammeReport.timestamp,
        reportedBy = courseProgrammeReport.reportedBy,
        votes = courseProgrammeReport.votes
)

fun toCourseProgrammeVersionOutput(courseProgrammeVersion: CourseProgrammeVersion) = CourseProgrammeVersionOutputModel(
        courseId = courseProgrammeVersion.courseId,
        version = courseProgrammeVersion.version,
        programmeId = courseProgrammeVersion.programmeId,
        lecturedTerm = courseProgrammeVersion.lecturedTerm,
        optional = courseProgrammeVersion.optional,
        credits = courseProgrammeVersion.credits,
        timestamp =courseProgrammeVersion.timestamp,
        createdBy = courseProgrammeVersion.createdBy
)

fun toCourseProgrammeStageOutputModel(courseProgrammeStage: CourseProgrammeStage) = CourseProgrammeStageOutputModel(
        stagedId = courseProgrammeStage.stageId,
        courseId = courseProgrammeStage.courseId,
        votes = courseProgrammeStage.votes,
        timestamp = courseProgrammeStage.timestamp,
        username = courseProgrammeStage.createdBy,
        programmeId = courseProgrammeStage.programmeId,
        optional = courseProgrammeStage.optional,
        credits = courseProgrammeStage.credits,
        lecturedTerm = courseProgrammeStage.lecturedTerm
)