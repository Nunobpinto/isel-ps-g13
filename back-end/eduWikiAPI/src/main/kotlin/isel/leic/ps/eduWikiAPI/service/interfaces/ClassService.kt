package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.*
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import java.util.*

interface ClassService {
    /**
     * Class
     */
    fun getAllClasses(): List<Class>

    fun getSpecificClass(classId: Int): Optional<Class>

    fun createClass(input: ClassInputModel): Optional<Class>

    fun voteOnClass(classId: Int, vote: VoteInputModel): Int

    fun partialUpdateOnClass(classId: Int, input: ClassInputModel): Int

    fun deleteSpecificClass(classId: Int): Int

    /**
     * Class Report
     */
    fun getAllReportsOfClass(classId: Int): List<ClassReport>

    fun getSpecificReportOfClass(classId: Int, reportId: Int): Optional<ClassReport>

    fun reportClass(classId: Int, report: ClassReportInputModel): Optional<ClassReport>

    fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateClassFromReport(classId: Int, reportId: Int): Int

    fun deleteAllReportsInClass(classId: Int): Int

    fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int

    /**
     * Class Stage
     */
    fun getAllStagedClasses(): List<ClassStage>

    fun getSpecificStagedClass(stageId: Int): Optional<ClassStage>

    fun createStagingClass(classStage: ClassStage): Optional<ClassStage>

    fun createClassFromStaged(stageId: Int): Optional<Class>

    fun voteOnStagedClass(stageId: Int, vote: VoteInputModel): Int

    fun deleteAllStagedClasses(): Int

    fun deleteSpecificStagedClass(stageId: Int): Int

    /**
     * Class version
     */
    fun getAllVersionsOfClass(classId: Int): List<ClassVersion>

    fun getSpecificVersionOfClass(classId: Int, versionId: Int): Optional<ClassVersion>

    fun deleteAllVersionsOfClass(courseId: Int): Int

    fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int

    /**
     * Course Class
     */
    fun getAllCoursesOfClass(classId: Int): List<Course>

    fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course>

    fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): Optional<CourseClass>

    fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel): Int

    fun deleteAllCoursesInClass(classId: Int): Int

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    /**
     * Course Class Report
     */
    fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport>

    fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport>

    fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): Optional<CourseClassReport>

    fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): Optional<CourseClass>

    fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int

    fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    /**
     * Course Class Stage
     */
    fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage>

    fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage>
    
    fun createStagingCourseInClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): Optional<CourseClassStage>
    
    fun addCourseInClassFromStaged(classId: Int, stageId: Int): Optional<CourseClass>
    
    fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel): Int
    
    fun deleteStageEntriesOfCourseInSpecificClass(classId: Int): Int
    
    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int

    /**
     * Lectures Of Class
     */
    fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture>

    fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): Optional<Lecture>

    fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): Optional<Lecture>

    fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel): Int

    fun deleteAllLecturesOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int

    /**
     * Lecture Report
     */
    fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureReport>

    fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<LectureReport>

    fun deleteAllReportsOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int

    fun deleteSpecificReportOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Int

    fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel): Optional<LectureReport>

    fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<Lecture>

    /**
     * Lecture Stage
     */
    fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): List<LectureStage>

    fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Optional<LectureStage>

    fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): Optional<LectureStage>

    fun deleteAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int

    fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int

    fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int): Optional<Lecture>

    /**
     * Lecture Version
     */
    fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureVersion>

    fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): Optional<LectureVersion>

    fun deleteAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int

    fun deleteSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): Int

    /**
     * Homework
     */
    fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): List<Homework>

    fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Optional<Homework>

    fun createHomeworkOnCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Optional<Homework>

    fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel): Int

    fun deleteAllHomeworksOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int

    /**
     * Homework Stage
     */
    fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): List<HomeworkStage>

    fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Optional<HomeworkStage>

    fun createStagingHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Optional<HomeworkStage>

    fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int): Optional<Homework>

    fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int

    fun deleteAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int

    /**
     * Homework Report
     */
    fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): List<HomeworkReport>

    fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport>

    fun createReportOnHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, homeworkReportInputModel: HomeworkReportInputModel): Optional<HomeworkReport>

    fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Optional<Homework>

    fun voteOnReportOfHomeworkInCourse(classId: Int, courseId: Int, homeWorkId: Int, reportId: Int, vote: VoteInputModel): Optional<HomeworkReport>

    fun deleteAllReportsInHomework(classId: Int, courseId: Int, homeworkId: Int): Int

    fun deleteSpecificReportInHomework(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Int
    
    /**
     * Homework version
     */
    fun getAllVersionsOfHomeworkOnCourseInClass(classId: Int, courseId: Int, homeworkId: Int): List<HomeworkVersion>

    fun getSpecificVersionOfHomework(classId: Int, courseId: Int, homeworkId: Int, versionId: Int): Optional<HomeworkVersion>

    fun deleteAllVersionsOfHomework(classId: Int, courseId: Int, homeworkId: Int): Int

    fun deleteSpecificVersionOfHomework(classId: Int, courseId: Int, homeworkId: Int, versionId: Int): Int


}