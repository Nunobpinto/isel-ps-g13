package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
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

fun toReportOfCourseProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel) = CourseProgrammeReport(
        courseId = courseId,
        programmeId = programmeId,
        reportedBy = inputCourseReport.reportedBy,
        lecturedTerm = inputCourseReport.lecturedTerm,
        optional = inputCourseReport.optional,
        credits = inputCourseReport.credits
)

fun toStageCourseProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel) = CourseProgrammeStage(
        courseId = inputCourseProgramme.courseId,
        programmeId = programmeId,
        credits = inputCourseProgramme.credits,
        optional = inputCourseProgramme.optional,
        lecturedTerm = inputCourseProgramme.lecturedTerm,
        timestamp = Timestamp.valueOf(LocalDateTime.now())
)

fun stageToCourseProgramme(courseProgrammeStage: CourseProgrammeStage) = Course (
        createdBy = courseProgrammeStage.createdBy,
        programmeId = courseProgrammeStage.programmeId,
        lecturedTerm = courseProgrammeStage.lecturedTerm,
        optional = courseProgrammeStage.optional,
        credits = courseProgrammeStage.credits,
        timestamp = Timestamp.valueOf(LocalDateTime.now())
)

