package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.HomeworkInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.LectureInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LessonReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.*
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
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var classDAO: ClassDAO

    @Autowired
    lateinit var courseDAO: CourseDAO

    @Autowired
    lateinit var lectureDAO: LectureDAO

    @Autowired
    lateinit var handle: Handle

    /**
     * Class Methods
     */

    override fun getAllClasses(): List<Class> = classDAO.getAllClasses()

    override fun getSpecificClass(classId: Int): Optional<Class> = classDAO.getSpecificClass(classId)

    override fun createClass(input: ClassInputModel): Optional<Class> {
        handle.begin()
        val klass = classDAO.createClass(Class(
                termId = input.term,
                fullName = input.fullName,
                timestamp = Timestamp.valueOf(LocalDateTime.now()),
                createdBy = input.createdBy
        )).get()
        classDAO.createClassVersion(ClassVersion(
                classId = klass.id,
                timestamp = klass.timestamp,
                className = klass.fullName,
                termId = klass.termId,
                createdBy = klass.createdBy
        ))
        handle.commit()
        return Optional.of(klass)
    }

    override fun voteOnClass(classId: Int, vote: VoteInputModel): Int = classDAO.voteOnClass(classId, Vote.valueOf(vote.vote))

    override fun partialUpdateOnClass(classId: Int, input: ClassInputModel): Int {
        handle.begin()
        val klass = classDAO.getSpecificClass(classId).get()
        val updatedClass = Class(
                id = classId,
                version = klass.version.inc(),
                createdBy = input.createdBy,
                fullName = if(input.fullName.isEmpty()) klass.fullName else input.fullName,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val res = classDAO.updateClass(updatedClass)
        classDAO.createClassVersion(ClassVersion(
                classId = updatedClass.id,
                version = updatedClass.version,
                className = updatedClass.fullName,
                createdBy = updatedClass.createdBy,
                timestamp = updatedClass.timestamp
        ))
        handle.commit()
        return res
    }

    override fun deleteSpecificClass(classId: Int): Int = classDAO.deleteSpecificClass(classId)

    override fun getAllReportsOfClass(classId: Int): List<ClassReport> = classDAO.getAllReportsFromClass(classId)

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): Optional<ClassReport> = classDAO.getSpecificReportFromClass(classId, reportId)

    override fun reportClass(classId: Int, report: ClassReportInputModel): Optional<ClassReport> {
        val rep = ClassReport(
                classId= classId,
                className = report.className,
                reportedBy = report.reportedBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return classDAO.reportClass(classId, rep)
    }

    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int = classDAO.voteOnReportOfClass(classId, reportId, Vote.valueOf(vote.vote))

    override fun updateClassFromReport(classId: Int, reportId: Int): Int {
        handle.begin()
        val klass = classDAO.getSpecificClass(classId).get()
        val report = classDAO.getSpecificReportFromClass(classId, reportId).get()
        val updatedClass = Class(
                id = classId,
                termId = report.termId,
                version = klass.version.inc(),
                timestamp = Timestamp.valueOf(LocalDateTime.now()),
                fullName = if(report.className.isEmpty()) klass.fullName else report.className,
                createdBy = report.reportedBy
        )
        val res = classDAO.updateClass(updatedClass)
        classDAO.createClassVersion(ClassVersion(
                classId = updatedClass.id,
                version = updatedClass.version,
                className = updatedClass.fullName,
                createdBy = updatedClass.createdBy,
                timestamp = updatedClass.timestamp
        ))
        classDAO.deleteSpecificReportOfClass(reportId)
        handle.commit()
        return res
    }

    override fun deleteAllReportsInClass(classId: Int): Int = classDAO.deleteAllReportsInClass(classId)

    override fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int = classDAO.deleteSpecificReportOfClass(reportId)

    override fun getAllStagedClasses(): List<ClassStage> = classDAO.getAllStagedClasses()

    override fun getSpecificStagedClass(stageId: Int): Optional<ClassStage> = classDAO.getSpecificStagedClass(stageId)

    override fun createStagingClass(inputClass: ClassInputModel): Optional<ClassStage> = classDAO.createStagingClass(inputClass)

    override fun createClassFromStaged(stageId: Int): Optional<Class> {
        handle.begin()
        val classStaged = classDAO.getSpecificStagedClass(stageId).get()
        val newClass = Class(
                id = classStaged.classId,
                termId = classStaged.termId,
                timestamp = Timestamp.valueOf(LocalDateTime.now()),
                fullName = classStaged.className,
                createdBy = classStaged.createdBy
        )
        val createdClass = classDAO.createClass(newClass).get()
        classDAO.deleteSpecificStagedClass(stageId)
        classDAO.createClassVersion(ClassVersion(
                classId = createdClass.id,
                version = createdClass.version,
                className = createdClass.fullName,
                createdBy = createdClass.createdBy,
                timestamp = createdClass.timestamp
        ))
        handle.commit()
        return Optional.of(createdClass)
    }

    override fun voteOnStagedClass(stageId: Int, vote: VoteInputModel): Int = classDAO.voteOnStagedClass(stageId, Vote.valueOf(vote.vote))

    override fun deleteAllStagedClasses(): Int = classDAO.deleteAllStagedClasses()

    override fun deleteSpecificStagedClass(stageId: Int): Int = classDAO.deleteSpecificStagedClass(stageId)

    override fun getAllVersionsOfClass(classId: Int): List<ClassVersion> = classDAO.getAllVersionsOfSpecificClass(classId)

    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): Optional<ClassVersion> = classDAO.getVersionOfSpecificClass(classId, versionId)

    override fun deleteAllVersionsOfClass(courseId: Int): Int = classDAO.deleteAllVersionsOfClass(courseId)

    override fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int = classDAO.deleteSpecificVersionOfClass(courseId, versionId)

    /**
     * Course Methods
     */

    override fun getAllCoursesOfClass(classId: Int): List<Course> = courseDAO.getCoursesOfClass(classId)

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course> = courseDAO.getSpecificCourseOfClass(classId, courseId)

    override fun addCourseToClass(classId: Int, courseId: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel): Int = courseDAO.voteOnCourseInClass(classId, courseId, Vote.valueOf(vote.vote))

    override fun deleteAllCoursesInClass(classId: Int): Int = courseDAO.deleteAllCoursesInClass(classId)

    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int = courseDAO.deleteSpecificCourseInClass(classId, courseId)

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport> = courseDAO.getAllReportsOfCourseInClass(classId, courseId)

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport>
        = courseDAO.getSpecificReportOfCourseInClass(classId, courseId, reportId)

    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): Optional<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int
            = courseDAO.voteOnReportOfCourseClass(classId, courseId, reportId, Vote.valueOf(vote.vote))

    override fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int = courseDAO.deleteAllCourseReportsInClass(classId, courseId)

    override fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int
            = courseDAO.deleteSpecificCourseReportInClass(classId, courseId, reportId)

    override fun getAllCoursesStagedInClass(classId: Int): List<CourseClassStage> = courseDAO.getAllCoursesStagedInClass(classId)

    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage> = courseDAO.getSpecificStagedCourseClass(classId, stageId)

    override fun createStagingCourseInClass(classId: Int, courseId: Int): Optional<CourseClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCourseToClassFromStaged(classId: Int, stageId: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel): Int
            = courseDAO.voteOnStagedCourseInClass(classId, stageId, Vote.valueOf(vote.vote))

    override fun deleteAllStagedCoursesInClass(classId: Int): Int = courseDAO.deleteAllStagedCoursesInClass(classId)

    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int = courseDAO.deleteSpecificStagedCourseInClass(classId, stageId)

    /**
     * Lectures Methods
     */

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture> = lectureDAO.getAllLecturesFromCourseInClass(classId, courseId)

    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): Optional<Lecture>
        = lectureDAO.getSpecificLectureFromCourseInClass(classId)

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

    /**
     * Homeworks Methods
     */

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