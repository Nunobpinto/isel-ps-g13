package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import java.sql.Timestamp
import java.time.LocalDateTime

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