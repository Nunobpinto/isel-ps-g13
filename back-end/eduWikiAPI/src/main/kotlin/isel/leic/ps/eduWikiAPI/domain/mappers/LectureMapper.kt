package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.LectureInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.LectureOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.LectureReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.LectureStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.LectureVersionOutputModel
import java.time.Duration


fun toLecture(lectureInputModel: LectureInputModel) = Lecture(
        createdBy = lectureInputModel.createdBy,
        weekDay = lectureInputModel.weekDay,
        begins = lectureInputModel.begins,
        duration = lectureInputModel.duration,
        location = lectureInputModel.location
)

fun toLectureStage(lectureInputModel: LectureInputModel) = LectureStage(
        createdBy = lectureInputModel.createdBy,
        weekDay = lectureInputModel.weekDay,
        begins = lectureInputModel.begins,
        duration = lectureInputModel.duration,
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

fun toLectureReport(lectureReportInputModel: LectureReportInputModel) = LectureReport(
        lectureId = lectureReportInputModel.lectureId,
        weekDay = lectureReportInputModel.weekday,
        begins = lectureReportInputModel.begins,
        duration = if(lectureReportInputModel.duration != null) Duration.ofMinutes(lectureReportInputModel.duration) else null,
        location = lectureReportInputModel.location,
        reportedBy = lectureReportInputModel.reportedBy
)

fun toLectureOutputModel(lecture: Lecture) = LectureOutputModel(
        username = lecture.createdBy,
        weekDay = lecture.weekDay,
        begins = lecture.begins,
        duration = lecture.duration,
        location = lecture.location,
        lectureId = lecture.lectureId,
        timestamp = lecture.timestamp,
        version = lecture.version,
        votes = lecture.votes
)

fun toLectureReportOutputModel(lectureReport: LectureReport) = LectureReportOutputModel(
        reportedBy = lectureReport.reportedBy,
        weekDay = lectureReport.weekDay,
        begins = lectureReport.begins,
        duration = lectureReport.duration,
        location = lectureReport.location,
        lectureId = lectureReport.lectureId,
        timestamp = lectureReport.timestamp,
        votes = lectureReport.votes,
        reportId = lectureReport.reportId
)

fun toLectureStageOutputModel(lectureStage: LectureStage) = LectureStageOutputModel(
        stagedId = lectureStage.stageId,
        weekDay = lectureStage.weekDay,
        begins = lectureStage.begins,
        duration = lectureStage.duration,
        location = lectureStage.location,
        timestamp = lectureStage.timestamp,
        votes = lectureStage.votes
)

fun toLectureVersionOutputModel(lectureVersion: LectureVersion) = LectureVersionOutputModel(
        createdBy = lectureVersion.createdBy,
        weekDay = lectureVersion.weekDay,
        begins = lectureVersion.begins,
        duration = lectureVersion.duration,
        location = lectureVersion.location,
        lectureId = lectureVersion.lectureId,
        timestamp = lectureVersion.timestamp,
        version = lectureVersion.version
)