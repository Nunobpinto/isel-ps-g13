package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.ClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.LectureOutputModel
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

interface ClassService {
    /**
     * Class
     */
    fun getAllClasses(): ClassCollectionOutputModel

    fun getSpecificClass(classId: Int): ClassOutputModel

    fun createClass(input: ClassInputModel): ClassOutputModel

    fun voteOnClass(classId: Int, vote: VoteInputModel): Int

    fun partialUpdateOnClass(classId: Int, input: ClassInputModel): ClassOutputModel

    fun deleteSpecificClass(classId: Int): Int

    /**
     * Class Report
     */
    fun getAllReportsOfClass(classId: Int): ClassReportCollectionOutputModel

    fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReportOutputModel

    fun reportClass(classId: Int, report: ClassReportInputModel): ClassReportOutputModel

    fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateClassFromReport(classId: Int, reportId: Int): ClassOutputModel

    fun deleteAllReportsInClass(classId: Int): Int

    fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int

    /**
     * Class Stage
     */
    fun getAllStagedClasses(): ClassStageCollectionOutputModel

    fun getSpecificStagedClass(stageId: Int): ClassStageOutputModel

    fun createStagingClass(classStage: ClassStage): ClassStageOutputModel

    fun createClassFromStaged(stageId: Int): ClassOutputModel

    fun voteOnStagedClass(stageId: Int, vote: VoteInputModel): Int

    fun deleteAllStagedClasses(): Int

    fun deleteSpecificStagedClass(stageId: Int): Int

    /**
     * Class version
     */
    fun getAllVersionsOfClass(classId: Int): ClassVersionCollectionOutputModel

    fun getSpecificVersionOfClass(classId: Int, versionId: Int): ClassVersionOutputModel

    fun deleteAllVersionsOfClass(courseId: Int): Int

    fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int

    /**
     * Course Class
     */
    fun getAllCoursesOfClass(classId: Int): CourseClassCollectionOutputModel

    fun getSpecificCourseOfClass(classId: Int, courseId: Int): CourseClassOutputModel

    fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): CourseClassOutputModel

    fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel): Int

    fun deleteAllCoursesInClass(classId: Int): Int

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    /**
     * Course Class Report
     */
    fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): CourseClassReportCollectionOutputModel

    fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): CourseClassReportOutputModel

    fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): CourseClassReportOutputModel

    fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): CourseClassOutputModel

    fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int

    fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    /**
     * Course Class Stage
     */
    fun getStageEntriesOfCoursesInClass(classId: Int): CourseClassStageCollectionOutputModel

    fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): CourseClassStageOutputModel
    
    fun createStagingCourseInClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): CourseClassStageOutputModel
    
    fun addCourseInClassFromStaged(classId: Int, stageId: Int): CourseClassOutputModel
    
    fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel): Int
    
    fun deleteStageEntriesOfCourseInSpecificClass(classId: Int): Int
    
    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int

    /**
     * Lectures Of Class
     */
    fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): LectureCollectionOutputModel

    fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureOutputModel

    fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): LectureOutputModel

    fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel): Int

    fun deleteAllLecturesOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int

    /**
     * Lecture Report
     */
    fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureReportCollectionOutputModel

    fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureReportOutputModel

    fun deleteAllReportsOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int

    fun deleteSpecificReportOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Int

    fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel): LectureReport

    fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureOutputModel

    /**
     * Lecture Stage
     */
    fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): LectureStageCollectionOutputModel

    fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): LectureStageOutputModel

    fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): LectureStageOutputModel

    fun deleteAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int

    fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int

    fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int): LectureOutputModel

    /**
     * Lecture Version
     */
    fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureVersionCollectionOutputModel

    fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): LectureVersionOutputModel

    fun deleteAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int

    fun deleteSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): Int

    /**
     * Homework
     */
    fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkCollectionOutputModel

    fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkOutputModel

    fun createHomeworkOnCourseInClass(sheet: MultipartFile, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): HomeworkOutputModel

    fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel): Int

    fun deleteAllHomeworksOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int

    /**
     * Homework Stage
     */
    fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkStageCollectionOutputModel

    fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): HomeworkStageOutputModel

    fun createStagingHomeworkOnCourseInClass(sheet: MultipartFile, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): HomeworkStageOutputModel

    fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int): HomeworkOutputModel

    fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int

    fun deleteAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int

    /**
     * Homework Report
     */
    fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): HomeworkReportCollectionOutputModel

    fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkReportOutputModel

    fun createReportOnHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, homeworkReportInputModel: HomeworkReportInputModel): HomeworkReportOutputModel

    fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkOutputModel

    fun voteOnReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, vote: VoteInputModel): Int

    fun deleteAllReportsOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int

    fun deleteSpecificReportOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Int
    
    /**
     * Homework version
     */
    fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkVersionCollectionOutputModel

    fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): HomeworkVersionOutputModel

    fun deleteAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int

    fun deleteSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): Int


}