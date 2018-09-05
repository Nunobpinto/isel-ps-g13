package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.LectureInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.LectureOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.LectureCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.LectureReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.LectureStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.LectureVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.LectureReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.LectureStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.LectureVersionOutputModel
import java.time.Duration
import java.time.LocalTime


fun toLecture(lectureInputModel: LectureInputModel, createdBy: String) = Lecture(
        createdBy = createdBy,
        weekDay = lectureInputModel.weekDay,
        begins = LocalTime.parse(lectureInputModel.begins),
        duration = Duration.ofHours(lectureInputModel.duration),
        location = lectureInputModel.location
)

fun toLectureStage(lectureInputModel: LectureInputModel, createdBy: String) = LectureStage(
        createdBy = createdBy,
        weekDay = lectureInputModel.weekDay,
        begins = LocalTime.parse(lectureInputModel.begins),
        duration = Duration.ofHours(lectureInputModel.duration),
        location = lectureInputModel.location
)

fun stagedToLecture(stagedLecture: LectureStage) = Lecture(
        createdBy = stagedLecture.createdBy,
        weekDay = stagedLecture.weekDay,
        begins = stagedLecture.begins,
        duration = stagedLecture.duration,
        location = stagedLecture.location
)

fun toLectureVersion(lecture: Lecture) = LectureVersion(
        lectureId = lecture.lectureId,
        createdBy = lecture.createdBy,
        weekDay = lecture.weekDay,
        begins = lecture.begins,
        duration = lecture.duration,
        location = lecture.location,
        version = lecture.version,
        timestamp = lecture.timestamp
)

fun toLectureReport(lectureReportInputModel: LectureReportInputModel, lectureId: Int, reportedBy: String) = LectureReport(
        lectureId = lectureId,
        weekDay = lectureReportInputModel.weekday,
        begins = lectureReportInputModel.begins,
        duration = if (lectureReportInputModel.duration != null) Duration.ofMinutes(lectureReportInputModel.duration) else null,
        location = lectureReportInputModel.location,
        reportedBy = reportedBy
)

fun toLectureOutputModel(lecture: Lecture, course: Course, klass: Class, term: Term) = LectureOutputModel(
        createdBy = lecture.createdBy,
        weekDay = lecture.weekDay,
        begins = lecture.begins,
        duration = lecture.duration,
        location = lecture.location,
        lectureId = lecture.lectureId,
        timestamp = lecture.timestamp,
        version = lecture.version,
        votes = lecture.votes,
        className = klass.className,
        courseShortName = course.shortName,
        lecturedTerm = term.shortName
)

fun toLectureReportOutputModel(lectureReport: LectureReport, course: Course, klass: Class, term: Term) = LectureReportOutputModel(
        reportedBy = lectureReport.reportedBy,
        weekDay = lectureReport.weekDay,
        begins = lectureReport.begins,
        duration = lectureReport.duration,
        location = lectureReport.location,
        lectureId = lectureReport.lectureId,
        timestamp = lectureReport.timestamp,
        votes = lectureReport.votes,
        reportId = lectureReport.reportId,
        className = klass.className,
        courseShortName = course.shortName,
        lecturedTerm = term.shortName
)

fun toLectureStageOutputModel(lectureStage: LectureStage) = LectureStageOutputModel(
        stagedId = lectureStage.stageId,
        weekDay = lectureStage.weekDay,
        begins = lectureStage.begins,
        duration = lectureStage.duration,
        location = lectureStage.location,
        timestamp = lectureStage.timestamp,
        votes = lectureStage.votes,
        createdBy = lectureStage.createdBy
)

fun toLectureVersionOutputModel(lectureVersion: LectureVersion, course: Course, klass: Class, term: Term) = LectureVersionOutputModel(
        createdBy = lectureVersion.createdBy,
        weekDay = lectureVersion.weekDay,
        begins = lectureVersion.begins,
        duration = lectureVersion.duration,
        location = lectureVersion.location,
        lectureId = lectureVersion.lectureId,
        timestamp = lectureVersion.timestamp,
        version = lectureVersion.version,
        className = klass.className,
        courseShortName = course.shortName,
        lecturedTerm = term.shortName
)

fun toLectureCollectionOutputModel(lectureList: List<LectureOutputModel>) = LectureCollectionOutputModel(
        lectureList = lectureList
)

fun toLectureReportCollectionOutputModel(lectureReportList: List<LectureReportOutputModel>) = LectureReportCollectionOutputModel(
        lectureReportList = lectureReportList
)

fun toLectureStageCollectionOutputModel(lectureStageList: List<LectureStageOutputModel>) = LectureStageCollectionOutputModel(
        lectureStageList = lectureStageList
)

fun toLectureVersionCollectionOutputModel(lectureVersionList: List<LectureVersionOutputModel>) = LectureVersionCollectionOutputModel(
        lectureVersionList = lectureVersionList
)

fun Lecture.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId/courses/$courseId/lectures/$lectureId",
        timestamp = actionLog.timestamp
)

fun LectureReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId/courses/$courseId/lectures/$lectureId/reports/$reportId",
        timestamp = actionLog.timestamp
)

fun LectureStage.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "classes/$classId/courses/$courseId/lectures/stage/$stageId",
        timestamp = actionLog.timestamp
)