package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import java.sql.Timestamp
import java.time.LocalDateTime

fun toCourse(input: CourseInputModel) = Course(
        organizationId = input.organizationId,
        createdBy = input.createdBy,
        fullName = input.fullName,
        shortName = input.shortName
)

fun toCourseStaged(input: CourseInputModel) = CourseStage(
        organizationId = input.organizationId,
        fullName = input.fullName,
        shortName = input.shortName,
        createdBy = input.createdBy,
        timestamp = Timestamp.valueOf(LocalDateTime.now())
)

fun stageToCourse(stage: CourseStage) = Course(
        organizationId = stage.organizationId,
        createdBy = stage.createdBy,
        fullName = stage.fullName,
        shortName = stage.shortName
)

fun toReport(courseId: Int, inputCourseReport: CourseReportInputModel) = CourseReport(
        courseId = courseId,
        courseFullName = inputCourseReport.fullName,
        courseShortName = inputCourseReport.shortName,
        reportedBy = inputCourseReport.reportedBy,
        timestamp = Timestamp.valueOf(LocalDateTime.now())
)