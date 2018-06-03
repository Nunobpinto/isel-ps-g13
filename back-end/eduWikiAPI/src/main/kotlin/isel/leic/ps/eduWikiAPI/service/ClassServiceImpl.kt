package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.HomeworkInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.LectureInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
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
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var classDAO: ClassDAO

    override fun getAllClasses(): List<Class> {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificClass(classId: Int): Optional<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createClass(input: ClassInputModel): Optional<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnClass(classId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClass(classId: Int, input: ClassInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfClass(classId: Int): List<ClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): Optional<ClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportClass(classId: Int, report: Any): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClassFromReport(classId: Int, reportId: Int): Class {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReportsInClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllStagedClasses(): List<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificStagedClass(stageId: Int): Optional<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStagingClass(inputClass: ClassInputModel): Optional<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createClassFromStaged(stageId: Int): Optional<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnStagedClass(stageId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun partialUpdateOnStagedClass(stageId: Int, input: ClassInputModel): ClassStage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllStagedClasses(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificStagedClass(stageId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllVersionsOfClass(classId: Int): List<ClassVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): Optional<ClassVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllVersionsOfClass(courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllCoursesOfClass(classId: Int): List<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificCourseOfClass(classId: Int, classId1: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCourseToClass(classId: Int, courseId: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllCoursesInClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): Optional<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllCoursesStagedInClass(classId: Int): List<CourseClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStagingCourseInClass(classId: Int, courseId: Int): Optional<CourseClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCourseToClassFromStaged(classId: Int, stageId: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllStagedCoursesInClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): Optional<Lecture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createLectureOnCourseInClass(classId: Int, courseId: Int, lecture: LectureInputModel): Optional<Lecture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnLecture(classId: Int, courseId: Int, lectureId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllLecturesOfCourse(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificLectureOfCourse(classId: Int, courseId: Int, lectureId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfLectureFromCourse(classId: Int, courseId: Int, lectureId: Int): List<LectureReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificReportOfLectureFromCourse(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<LectureReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReportsInLecture(classId: Int, courseId: Int, lectureId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificReportInLecture(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportLectureOnCourse(classId: Int, courseId: Int, lectureId: Int, report: LessonReportInputModel): Optional<LectureReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnReportOfLessonInCourse(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateLessonFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<Lecture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): List<LectureStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificStagedLectureOfCourse(classId: Int, courseId: Int, stageId: Int): Optional<LectureStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lecture: LectureInputModel): Optional<LectureStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificStagedLectureOfCourse(classId: Int, courseId: Int, stageId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createLectureFromStage(classId: Int, courseId: Int, stageId: Int): Optional<Lecture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, versionId: Int): Optional<LectureVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllVersionsOfLectureOfCourseInTerm(classId: Int, courseId: Int, lectureId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificVersionOfLecture(classId: Int, courseId: Int, lectureId: Int, versionId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Optional<Homework> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllHomeworksOfCourseInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): List<HomeworkStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Optional<HomeworkStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStagingHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Optional<HomeworkStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int): Optional<Homework> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): List<HomeworkReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<HomeworkReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportHomeworkOnCourse(classId: Int, courseId: Int, homeworkId: Int): Optional<HomeworkReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateHomeworkFromReport(classId: Int, courseId: Int, homeWorkId: Int, reportId: Int): Optional<Homework> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnReportOfHomeworkInCourse(classId: Int, courseId: Int, homeWorkId: Int, reportId: Int, vote: VoteInputModel): Optional<HomeworkReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReportsInHomework(classId: Int, courseId: Int, homeworkId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificReportInHomework(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllVersionsOfHomeworkOnCourseInClass(classId: Int, courseId: Int, homeworkId: Int): List<HomeworkVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificVersionOfHomework(classId: Int, courseId: Int, homeworkId: Int, versionId: Int): Optional<HomeworkVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllVersionsOfHomework(classId: Int, courseId: Int, homeworkId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificVersionOfHomework(classId: Int, courseId: Int, homeworkId: Int, versionId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}