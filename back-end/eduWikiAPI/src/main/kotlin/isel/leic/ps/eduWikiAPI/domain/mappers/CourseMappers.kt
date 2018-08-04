package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.CourseReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.CourseStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.CourseVersionOutputModel

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

fun toCourseOutputModel(course: Course) = CourseOutputModel(
        courseId = course.courseId,
        organizationId = course.organizationId,
        version = course.version,
        votes = course.votes,
        timestamp = course.timestamp,
        fullName = course.fullName,
        shortName = course.shortName,
        username = course.createdBy
)

fun toCourseReportOutputModel(courseReport: CourseReport) = CourseReportOutputModel(
        courseId = courseReport.courseId,
        reportedBy = courseReport.reportedBy,
        reportId = courseReport.reportId,
        votes = courseReport.votes,
        timestamp = courseReport.timestamp,
        fullName = courseReport.fullName,
        shortName = courseReport.shortName
)

fun toCourseStageOutputModel(courseStage: CourseStage) = CourseStageOutputModel(
        stagedId = courseStage.stageId,
        organizationId = courseStage.organizationId,
        votes = courseStage.votes,
        timestamp = courseStage.timestamp,
        fullName = courseStage.fullName,
        shortName = courseStage.shortName,
        username = courseStage.createdBy
)

fun toCourseVersionOutputModel(courseVersion: CourseVersion) = CourseVersionOutputModel(
        courseId = courseVersion.courseId,
        organizationId = courseVersion.organizationId,
        version = courseVersion.version,
        timestamp = courseVersion.timestamp,
        fullName = courseVersion.fullName,
        shortName = courseVersion.shortName,
        createdBy = courseVersion.createdBy
)
