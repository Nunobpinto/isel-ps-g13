package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.HomeworkInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.LectureInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LessonReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.Lecture
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

    fun addCourseToClass(classId: Int, courseId: Int): Int

    fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel): Int

    fun deleteAllCoursesInClass(classId: Int): Int

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    /**
     * Course Class Report
     */
    fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport>

    fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport>

    fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): Optional<CourseClassReport>

    fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): Optional<Course>

    fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int

    fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    /**
     * Course Class Stage
     */
    fun getAllCoursesStagedInClass(classId: Int): List<CourseClassStage>

    fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage>
    
    fun createStagingCourseInClass(classId: Int, courseId: Int): Optional<CourseClassStage>
    
    fun addCourseToClassFromStaged(classId: Int, stageId: Int): Optional<Course>
    
    fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel): Int
    
    fun deleteAllStagedCoursesInClass(classId: Int): Int
    
    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int

    /**
     * Lectures Of Class
     */
    fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture>

    fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): Optional<Lecture>

    fun createLectureOnCourseInClass(classId: Int, courseId: Int, lecture: LectureInputModel): Optional<Lecture>

    fun voteOnLecture(classId: Int, courseId: Int, lectureId: Int): Int

    fun deleteAllLecturesOfCourse(classId: Int, courseId: Int): Int

    fun deleteSpecificLectureOfCourse(classId: Int, courseId: Int, lectureId: Int): Int

    /**
     * Lecture Report
     */
    fun getAllReportsOfLectureFromCourse(classId: Int, courseId: Int, lectureId: Int): List<LectureReport>

    fun getSpecificReportOfLectureFromCourse(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<LectureReport>

    fun deleteAllReportsInLecture(classId: Int, courseId: Int, lectureId: Int): Int

    fun deleteSpecificReportInLecture(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Int

    fun reportLectureOnCourse(classId: Int, courseId: Int, lectureId: Int, report: LessonReportInputModel): Optional<LectureReport>

    fun voteOnReportOfLessonInCourse(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateLessonFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<Lecture>

    /**
     * Lecture Stage
     */
    fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): List<LectureStage>

    fun getSpecificStagedLectureOfCourse(classId: Int, courseId: Int, stageId: Int): Optional<LectureStage>

    fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lecture: LectureInputModel): Optional<LectureStage>

    fun deleteAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificStagedLectureOfCourse(classId: Int, courseId: Int, stageId: Int): Int

    fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int

    fun createLectureFromStage(classId: Int, courseId: Int, stageId: Int): Optional<Lecture>

    /**
     * Lecture Version
     */
    fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureVersion>

    fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, versionId: Int): Optional<LectureVersion>

    fun deleteAllVersionsOfLectureOfCourseInTerm(classId: Int, courseId: Int, lectureId: Int): Int

    fun deleteSpecificVersionOfLecture(classId: Int, courseId: Int, lectureId: Int, versionId: Int): Int

    /**
     * Homework
     */
    fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): Int

    fun getSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int

    fun createHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Optional<Homework>

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

    fun deleteStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int

    /**
     * Homework Report
     */
    fun getAllReportsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): List<HomeworkReport>

    fun getSpecificReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<HomeworkReport>

    fun reportHomeworkOnCourse(classId: Int, courseId: Int, homeworkId: Int): Optional<HomeworkReport>

    fun updateHomeworkFromReport(classId: Int, courseId: Int, homeWorkId: Int, reportId: Int): Optional<Homework>

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