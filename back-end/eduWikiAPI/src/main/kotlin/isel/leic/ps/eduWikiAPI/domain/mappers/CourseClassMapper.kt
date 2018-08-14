package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.CourseClass
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseClassReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseClassStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseClassStageOutputModel

fun stagedToCourseClass(stage: CourseClassStage) = CourseClass(
        courseId = stage.courseId,
        classId = stage.classId,
        termId = stage.termId,
        createdBy = stage.createdBy
)

fun toCourseClassReport(courseClassId: Int, courseClassReportInputModel: CourseClassReportInputModel, reportedBy: String): CourseClassReport = CourseClassReport(
        courseClassId = courseClassId,
        classId = courseClassReportInputModel.classId,
        courseId = courseClassReportInputModel.courseId,
        termId = courseClassReportInputModel.termId,
        deletePermanently = courseClassReportInputModel.deletePermanently,
        reportedBy = reportedBy
)

fun toCourseClassOutputModel(course: Course, klass: Class, courseClass: CourseClass, term: Term) = CourseClassOutputModel(
        courseId = course.courseId,
        createdBy= courseClass.createdBy,
        timestamp = courseClass.timestamp,
        votes = courseClass.votes,
        lecturedTerm = term.shortName,
        classId = klass.classId,
        className = klass.className,
        courseName = course.shortName,
        termId = courseClass.termId,
        courseClassId = courseClass.courseClassId
)

fun toCourseClassReportOutputModel(courseClassReport: CourseClassReport) = CourseClassReportOutputModel(
        reportId = courseClassReport.reportId,
        courseClassId = courseClassReport.courseClassId,
        classId = courseClassReport.classId,
        termId = courseClassReport.termId,
        courseId = courseClassReport.courseId,
        reportedBy = courseClassReport.reportedBy,
        votes = courseClassReport.votes,
        timestamp = courseClassReport.timestamp,
        deletePermanently = courseClassReport.deletePermanently
)

fun toCourseClassStageOutputModel(courseClassStage: CourseClassStage) = CourseClassStageOutputModel(
        stagedId = courseClassStage.stageId,
        classId = courseClassStage.classId,
        courseId = courseClassStage.courseId,
        timestamp = courseClassStage.timestamp,
        votes = courseClassStage.votes,
        username = courseClassStage.createdBy
)

fun toCourseClassCollectionOutputModel(courseClasseList: List<CourseClassOutputModel>) = CourseClassCollectionOutputModel(
        courseClasseList = courseClasseList
)

fun toCourseClassReportCollectionOutputModel(courseClassReportList: List<CourseClassReportOutputModel>) = CourseClassReportCollectionOutputModel(
        courseClassReportList = courseClassReportList
)

fun toCourseClassStageCollectionOutputModel(courseClassStageList: List<CourseClassStageOutputModel>) = CourseClassStageCollectionOutputModel(
        courseClassStageList = courseClassStageList
)