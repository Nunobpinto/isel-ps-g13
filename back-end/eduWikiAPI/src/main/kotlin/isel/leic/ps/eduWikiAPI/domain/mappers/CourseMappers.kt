package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.CourseVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.CourseVersionOutputModel

fun toCourse(input: CourseInputModel, createdBy: String) = Course(
        createdBy = createdBy,
        fullName = input.fullName,
        shortName = input.shortName
)

fun toCourseStage(input: CourseInputModel, createdBy: String) = CourseStage(
        createdBy = createdBy,
        fullName = input.fullName,
        shortName = input.shortName
)

fun stagedToCourse(stage: CourseStage) = Course(
        createdBy = stage.createdBy,
        fullName = stage.fullName,
        shortName = stage.shortName
)

fun toCourseReport(courseId: Int, inputCourseReport: CourseReportInputModel, reportedBy: String) = CourseReport(
        reportedBy = reportedBy,
        courseId = courseId,
        fullName = inputCourseReport.fullName,
        shortName = inputCourseReport.shortName
)

fun toCourseVersion(course: Course) = CourseVersion(
        courseId = course.courseId,
        version = course.version,
        fullName = course.fullName,
        shortName = course.shortName,
        createdBy = course.createdBy,
        timestamp = course.timestamp
)

fun toCourseOutputModel(course: Course) = CourseOutputModel(
        courseId = course.courseId,
        version = course.version,
        votes = course.votes,
        timestamp = course.timestamp,
        fullName = course.fullName,
        shortName = course.shortName,
        createdBy = course.createdBy
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
        votes = courseStage.votes,
        timestamp = courseStage.timestamp,
        fullName = courseStage.fullName,
        shortName = courseStage.shortName,
        createdBy = courseStage.createdBy
)

fun toCourseVersionOutputModel(courseVersion: CourseVersion) = CourseVersionOutputModel(
        courseId = courseVersion.courseId,
        version = courseVersion.version,
        timestamp = courseVersion.timestamp,
        fullName = courseVersion.fullName,
        shortName = courseVersion.shortName,
        createdBy = courseVersion.createdBy
)

fun toCourseCollectionOutputModel(courseList: List<CourseOutputModel>) = CourseCollectionOutputModel(
        courseList = courseList
)

fun toCourseReportCollectionOutputModel(courseReportList: List<CourseReportOutputModel>) = CourseReportCollectionOutputModel(
        courseReportList = courseReportList
)

fun toCourseStageCollectionOutputModel(courseStageList: List<CourseStageOutputModel>) = CourseStageCollectionOutputModel(
        courseStageList = courseStageList
)

fun toCourseVersionCollectionOutputModel(courseVersionList: List<CourseVersionOutputModel>) = CourseVersionCollectionOutputModel(
        courseVersionList = courseVersionList
)

fun Course.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        entity_type = actionLog.entity,
        entity_link = "/courses/$courseId",
        timestamp = actionLog.timestamp
)

fun CourseStage.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        entity_type = actionLog.entity,
        entity_link = "/courses/stage/$stageId",
        timestamp = actionLog.timestamp
)

fun CourseReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        entity_type = actionLog.entity,
        entity_link = "/courses/$courseId/reports/$reportId",
        timestamp = actionLog.timestamp
)