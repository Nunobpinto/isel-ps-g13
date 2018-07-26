package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import java.sql.Timestamp
import java.time.LocalDateTime

fun toCourse(input: CourseInputModel) = Course(
        organizationId = input.organizationId,
        createdBy = input.createdBy,
        fullName = input.fullName,
        shortName = input.shortName
)

fun toCourseStage(input: CourseInputModel) = CourseStage(
        organizationId = input.organizationId,
        fullName = input.fullName,
        shortName = input.shortName,
        createdBy = input.createdBy
)

fun stagedToCourse(stage: CourseStage) = Course(
        organizationId = stage.organizationId,
        createdBy = stage.createdBy,
        fullName = stage.fullName,
        shortName = stage.shortName
)

fun toCourseReport(courseId: Int, inputCourseReport: CourseReportInputModel) = CourseReport(
        courseId = courseId,
        fullName = inputCourseReport.fullName,
        shortName = inputCourseReport.shortName,
        reportedBy = inputCourseReport.reportedBy
)

fun toCourseVersion(course: Course) = CourseVersion(
        courseId = course.courseId,
        organizationId = course.organizationId,
        version = course.version,
        fullName = course.fullName,
        shortName = course.shortName,
        createdBy = course.createdBy,
        timestamp = course.timestamp
)