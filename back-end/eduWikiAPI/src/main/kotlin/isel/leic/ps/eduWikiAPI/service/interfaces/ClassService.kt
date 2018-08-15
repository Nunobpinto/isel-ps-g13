package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.LectureOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.HomeworkCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.LectureCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ClassReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseClassReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.HomeworkReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.LectureReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ClassStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseClassStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.HomeworkStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.LectureStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ClassVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.HomeworkVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.LectureVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.HomeworkReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.LectureReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ClassStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseClassStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.HomeworkStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.LectureStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ClassVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.HomeworkVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.LectureVersionOutputModel
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

interface ClassService {
    /**
     * Class
     */
    fun getAllClasses(): ClassCollectionOutputModel

    fun getSpecificClass(classId: Int): ClassOutputModel

    fun createClass(input: ClassInputModel, principal: Principal): ClassOutputModel

    fun voteOnClass(classId: Int, vote: VoteInputModel, principal: Principal): Int

    fun partialUpdateOnClass(classId: Int, input: ClassInputModel, principal: Principal): ClassOutputModel

    fun deleteSpecificClass(classId: Int, principal: Principal): Int

    /**
     * Class Report
     */
    fun getAllReportsOfClass(classId: Int): ClassReportCollectionOutputModel

    fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReportOutputModel

    fun createClassReport(classId: Int, report: ClassReportInputModel, principal: Principal): ClassReportOutputModel

    fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun updateClassFromReport(classId: Int, reportId: Int, principal: Principal): ClassOutputModel

    fun deleteSpecificReportInClass(classId: Int, reportId: Int, principal: Principal): Int

    /**
     * Class Stage
     */
    fun getAllStagedClasses(): ClassStageCollectionOutputModel

    fun getSpecificStagedClass(stageId: Int): ClassStageOutputModel

    fun createStagingClass(classStageInputModel: ClassInputModel, principal: Principal): ClassStageOutputModel

    fun createClassFromStaged(stageId: Int, principal: Principal): ClassOutputModel

    fun voteOnStagedClass(stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificStagedClass(stageId: Int, principal: Principal): Int

    /**
     * Class version
     */
    fun getAllVersionsOfClass(classId: Int): ClassVersionCollectionOutputModel

    fun getSpecificVersionOfClass(classId: Int, versionId: Int): ClassVersionOutputModel

    /**
     * Course Class
     */
    fun getAllCoursesOfClass(classId: Int): CourseClassCollectionOutputModel

    fun getSpecificCourseOfClass(classId: Int, courseId: Int): CourseClassOutputModel

    fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel, principal: Principal): CourseClassOutputModel

    fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int, principal: Principal): Int

    /**
     * Course Class Report
     */
    fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): CourseClassReportCollectionOutputModel

    fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): CourseClassReportOutputModel

    fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel, principal: Principal): CourseClassReportOutputModel

    fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int, principal: Principal): CourseClassOutputModel

    fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, principal: Principal): Int

    /**
     * Course Class Stage
     */
    fun getStageEntriesOfCoursesInClass(classId: Int): CourseClassStageCollectionOutputModel

    fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): CourseClassStageOutputModel
    
    fun createStagingCourseInClass(classId: Int, courseId: Int, principal: Principal): CourseClassStageOutputModel
    
    fun addCourseInClassFromStaged(classId: Int, stageId: Int, principal: Principal): CourseClassOutputModel
    
    fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int
    
    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int, principal: Principal): Int

    /**
     * Lectures Of Class
     */
    fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): LectureCollectionOutputModel

    fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureOutputModel

    fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel, principal: Principal): LectureOutputModel

    fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, principal: Principal): Int

    /**
     * Lecture Report
     */
    fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureReportCollectionOutputModel

    fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureReportOutputModel

    fun deleteSpecificReportOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, principal: Principal): Int

    fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel, principal: Principal): LectureReport

    fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int, principal: Principal): LectureOutputModel

    /**
     * Lecture Stage
     */
    fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): LectureStageCollectionOutputModel

    fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): LectureStageOutputModel

    fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel, principal: Principal): LectureStageOutputModel

    fun deleteSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, principal: Principal): Int

    fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): LectureOutputModel

    /**
     * Lecture Version
     */
    fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureVersionCollectionOutputModel

    fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): LectureVersionOutputModel

    /**
     * Homework
     */
    fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkCollectionOutputModel

    fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkOutputModel

    fun createHomeworkOnCourseInClass(sheet: MultipartFile, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel, principal: Principal): HomeworkOutputModel

    fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, principal: Principal): Int

    /**
     * Homework Stage
     */
    fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkStageCollectionOutputModel

    fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): HomeworkStageOutputModel

    fun createStagingHomeworkOnCourseInClass(sheet: MultipartFile, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel, principal: Principal): HomeworkStageOutputModel

    fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): HomeworkOutputModel

    fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, principal: Principal): Int

    /**
     * Homework Report
     */
    fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): HomeworkReportCollectionOutputModel

    fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkReportOutputModel

    fun createReportOnHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, homeworkReportInputModel: HomeworkReportInputModel, principal: Principal): HomeworkReportOutputModel

    fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, principal: Principal): HomeworkOutputModel

    fun voteOnReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificReportOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, principal: Principal): Int
    
    /**
     * Homework version
     */
    fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkVersionCollectionOutputModel

    fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): HomeworkVersionOutputModel

}