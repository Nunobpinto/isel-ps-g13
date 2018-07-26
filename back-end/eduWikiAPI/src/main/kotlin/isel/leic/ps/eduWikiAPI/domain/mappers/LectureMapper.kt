package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.LectureInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ClassMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion


fun toLecture(classMiscUnit: ClassMiscUnit, lectureInputModel: LectureInputModel) = Lecture(
        lectureId = classMiscUnit.classMiscUnitId,
        createdBy = lectureInputModel.createdBy,
        weekDay = lectureInputModel.weekDay,
        begins = lectureInputModel.begins,
        duration = lectureInputModel.duration,
        location = lectureInputModel.location
)

fun toLectureStage(classMiscUnitStaged: ClassMiscUnitStage, lectureInputModel: LectureInputModel) = LectureStage(
        stageId = classMiscUnitStaged.stageId,
        createdBy = lectureInputModel.createdBy,
        weekDay = lectureInputModel.weekDay,
        begins = lectureInputModel.begins,
        duration = lectureInputModel.duration,
        location = lectureInputModel.location
)

fun stagedToLecture(classMiscUnit: ClassMiscUnit, stagedLecture: LectureStage) = Lecture(
        lectureId = classMiscUnit.classMiscUnitId,
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
        duration = lectureReportInputModel.duration,
        location = lectureReportInputModel.location,
        reportedBy = lectureReportInputModel.reportedBy
)