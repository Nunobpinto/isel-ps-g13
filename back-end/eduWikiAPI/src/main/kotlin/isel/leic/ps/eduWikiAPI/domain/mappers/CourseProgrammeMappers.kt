package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.CourseProgramme
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseProgrammeCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseProgrammeReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseProgrammeStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.CourseProgrammeVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.CourseProgrammeVersionOutputModel

fun toCourseProgramme(input: CourseProgrammeInputModel, createdBy: String) = CourseProgramme(
        courseId = input.courseId,
        lecturedTerm = input.lecturedTerm,
        programmeId = input.programmeId,
        credits = input.credits,
        optional = input.optional,
        createdBy = createdBy
)

fun toCourseProgrammeReport(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel, reportedBy: String) = CourseProgrammeReport(
        courseId = courseId,
        programmeId = programmeId,
        reportedBy = reportedBy,
        lecturedTerm = inputCourseReport.lecturedTerm,
        optional = inputCourseReport.optional,
        credits = inputCourseReport.credits,
        deleteFlag = inputCourseReport.deleteFlag
)

fun toCourseProgrammeStage(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel, createdBy: String) = CourseProgrammeStage(
        courseId = inputCourseProgramme.courseId,
        programmeId = programmeId,
        credits = inputCourseProgramme.credits,
        optional = inputCourseProgramme.optional,
        lecturedTerm = inputCourseProgramme.lecturedTerm,
        createdBy = createdBy
)

fun stagedToCourseProgramme(programmeId: Int, courseProgrammeStage: CourseProgrammeStage) = CourseProgramme(
        createdBy = courseProgrammeStage.createdBy,
        programmeId = programmeId,
        lecturedTerm = courseProgrammeStage.lecturedTerm,
        optional = courseProgrammeStage.optional,
        credits = courseProgrammeStage.credits
)

fun toCourseProgrammeVersion(course: CourseProgramme) = CourseProgrammeVersion(
        version = course.version,
        courseId = course.courseId,
        programmeId = course.programmeId,
        lecturedTerm = course.lecturedTerm,
        optional = course.optional,
        credits = course.credits,
        timestamp = course.timestamp,
        createdBy = course.createdBy
)

fun toCourseProgrammeOutputModel(courseProgramme: CourseProgramme, course: Course) = CourseProgrammeOutputModel(
        courseId = courseProgramme.courseId,
        version = courseProgramme.version,
        votes = courseProgramme.votes,
        timestamp = courseProgramme.timestamp,
        fullName = course.fullName,
        shortName = course.shortName,
        createdBy = courseProgramme.createdBy,
        programmeId = courseProgramme.programmeId,
        optional = courseProgramme.optional,
        credits = courseProgramme.credits,
        lecturedTerm = courseProgramme.lecturedTerm
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
        votes = courseProgrammeReport.votes,
        deleteFlag = courseProgrammeReport.deleteFlag
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

fun toCourseProgrammeStageOutputModel(course: Course, courseProgrammeStage: CourseProgrammeStage) = CourseProgrammeStageOutputModel(
        stagedId = courseProgrammeStage.stageId,
        courseId = courseProgrammeStage.courseId,
        votes = courseProgrammeStage.votes,
        timestamp = courseProgrammeStage.timestamp,
        createdBy = courseProgrammeStage.createdBy,
        programmeId = courseProgrammeStage.programmeId,
        optional = courseProgrammeStage.optional,
        credits = courseProgrammeStage.credits,
        lecturedTerm = courseProgrammeStage.lecturedTerm,
        courseShortName = course.shortName
)

fun toCourseProgrammeCollectionOutputModel(courseProgrammeList: List<CourseProgrammeOutputModel>) = CourseProgrammeCollectionOutputModel(
        courseProgrammeList = courseProgrammeList
)

fun toCourseProgrammeVersionCollectionOutputModel(courseProgrammeVersionList: List<CourseProgrammeVersionOutputModel>) = CourseProgrammeVersionCollectionOutputModel(
        courseProgrammeVersionList = courseProgrammeVersionList
)

fun toCourseProgrammeReportCollectionOutputModel(courseProgrammeReportList: List<CourseProgrammeReportOutputModel>) = CourseProgrammeReportCollectionOutputModel(
        courseProgrammeReportList = courseProgrammeReportList
)

fun toCourseProgrammeStageCollectionOutputModel(courseProgrammeStageList: List<CourseProgrammeStageOutputModel>) = CourseProgrammeStageCollectionOutputModel(
        courseProgrammeStageList = courseProgrammeStageList
)

fun CourseProgramme.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "programmes/$programmeId/courses/$courseId",
        timestamp = actionLog.timestamp
)

fun CourseProgrammeReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "programmes/$programmeId/courses/$courseId/reports/$reportId",
        timestamp = actionLog.timestamp
)

fun CourseProgrammeStage.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "programmes/$programmeId/courses/stage/$stageId",
        timestamp = actionLog.timestamp
)