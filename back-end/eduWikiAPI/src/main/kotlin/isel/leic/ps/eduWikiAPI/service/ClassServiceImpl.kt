package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
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
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var jdbi: Jdbi

    /**
     * Class Methods
     */

    override fun getAllClasses(): List<Class> =
            jdbi.withExtension<List<Class>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllClasses()
            }

    override fun getSpecificClass(classId: Int): Optional<Class> =
            jdbi.withExtension<Optional<Class>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getSpecificClass(classId)
            }

    override fun createClass(input: ClassInputModel): Class =
            jdbi.inTransaction<Class, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val klass = classDAO.createClass(toClass(input))
                classDAO.createClassVersion(toClassVersion(klass))
                klass
            }

    override fun voteOnClass(classId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getClassVotes(classId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateClassVotes(classId, votes)
            }

    override fun partialUpdateOnClass(classId: Int, input: ClassInputModel): Class =
            jdbi.inTransaction<Class, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId).get()
                val updatedClass = Class(
                        classId = classId,
                        version = klass.version.inc(),
                        createdBy = input.createdBy,
                        className = if (input.className.isEmpty()) klass.className else input.className
                )
                val res = classDAO.updateClass(updatedClass)
                classDAO.createClassVersion(toClassVersion(updatedClass))
                res
            }

    override fun deleteSpecificClass(classId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificClass(classId)
            }

    override fun getAllReportsOfClass(classId: Int): List<ClassReport> =
            jdbi.withExtension<List<ClassReport>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllReportsFromClass(classId)
            }

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): Optional<ClassReport> =
            jdbi.withExtension<Optional<ClassReport>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getSpecificReportFromClass(classId, reportId)
            }

    override fun reportClass(classId: Int, report: ClassReportInputModel): ClassReport =
            jdbi.withExtension<ClassReport, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.reportClass(classId, toClassReport(classId, report))
            }

    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getReportedClassVotes(classId, reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateReportedClassVotes(classId, reportId, votes)
            }

    override fun updateClassFromReport(classId: Int, reportId: Int): Class =
            jdbi.inTransaction<Class, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId).get()
                val report = classDAO.getSpecificReportFromClass(classId, reportId).get()
                val updatedClass = Class(
                        classId = classId,
                        termId = report.termId,
                        version = klass.version.inc(),
                        className = report.className ?: klass.className,
                        createdBy = report.reportedBy
                )
                val res = classDAO.updateClass(updatedClass)
                classDAO.createClassVersion(toClassVersion(updatedClass))
                classDAO.deleteSpecificReportInClass(classId, reportId)
                res
            }

    override fun deleteAllReportsInClass(classId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteAllReportsInClass(classId)
            }

    override fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificReportInClass(classId, reportId)
            }

    override fun getAllStagedClasses(): List<ClassStage> =
            jdbi.withExtension<List<ClassStage>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllStagedClasses()
            }

    override fun getSpecificStagedClass(stageId: Int): Optional<ClassStage> =
            jdbi.withExtension<Optional<ClassStage>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getSpecificStagedClass(stageId)
            }

    override fun createStagingClass(classStage: ClassStage): ClassStage =
            jdbi.withExtension<ClassStage, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.createStagedClass(classStage)
            }

    override fun createClassFromStaged(stageId: Int): Class =
            jdbi.inTransaction<Class, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val classStaged = classDAO.getSpecificStagedClass(stageId).get()
                val createdClass = classDAO.createClass(stagedToClass(classStaged))
                classDAO.deleteSpecificStagedClass(stageId)
                classDAO.createClassVersion(toClassVersion(createdClass))
                createdClass
            }

    override fun voteOnStagedClass(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getStagedClassVotes(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateStagedClassVotes(stageId, votes)
            }

    override fun deleteAllStagedClasses(): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteAllStagedClasses()
            }

    override fun deleteSpecificStagedClass(stageId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificStagedClass(stageId)
            }

    override fun getAllVersionsOfClass(classId: Int): List<ClassVersion> =
            jdbi.withExtension<List<ClassVersion>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllVersionsOfSpecificClass(classId)
            }

    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): Optional<ClassVersion> =
            jdbi.withExtension<Optional<ClassVersion>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getVersionOfSpecificClass(classId, versionId)
            }

    override fun deleteAllVersionsOfClass(courseId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteAllVersionsOfClass(courseId)
            }

    override fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificVersionOfClass(courseId, versionId)
            }

    override fun getAllCoursesOfClass(classId: Int): List<Course> =
            jdbi.withExtension<List<Course>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllCoursesOfClass(classId)
            }

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course> =
            jdbi.withExtension<Optional<Course>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getSpecificCourseOfClass(classId, courseId)
            }

    override fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): CourseClass =
            jdbi.inTransaction<CourseClass, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termId = classDAO.getTermIdFromSpecificClass(classId)
                val courseClass = CourseClass(
                        courseId = courseId,
                        classId = classId,
                        termId = termId,
                        createdBy = courseClassInputModel.createdBy
                )
                classDAO.addCourseToClass(courseClass)
            }

    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getCourseClassVotes(classId, courseId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateCourseClassVotes(classId, courseId, votes)
            }

    override fun deleteAllCoursesInClass(classId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteAllCoursesInClass(classId)
            }

    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificCourseInClass(classId, courseId)
            }

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport> =
            jdbi.withExtension<List<CourseClassReport>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllReportsOfCourseInClass(classId, courseId)
            }

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport> =
            jdbi.withExtension<Optional<CourseClassReport>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getSpecificReportOfCourseInClass(classId, courseId, reportId)
            }

    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): CourseClassReport =
            jdbi.inTransaction<CourseClassReport, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClass = classDAO.getCourseClass(classId, courseId).get()
                val courseClassReport = CourseClassReport(
                        courseClassId = courseClass.courseClassId,
                        classId = courseClassReportInputModel.classId ?: courseClass.classId,
                        courseId = courseClassReportInputModel.courseId ?: courseClass.courseId,
                        termId = courseClassReportInputModel.termId ?: courseClass.termId,
                        reportedBy = courseClassReportInputModel.reportedBy
                )
                classDAO.reportCourseInClass(courseClassReport)
            }

    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): CourseClass =
            jdbi.inTransaction<CourseClass, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClassReport = classDAO.getSpecificReportOfCourseInClass(classId, courseId, reportId).get()
                val courseClass = classDAO.getCourseClass(classId, courseId).get()
                val updatedCourseClass = CourseClass(
                        courseClassId = courseClass.courseClassId,
                        createdBy = courseClassReport.reportedBy,
                        courseId = courseClassReport.courseId ?: courseClass.courseId,
                        classId = courseClassReport.classId ?: courseClass.courseId,
                        termId = courseClassReport.termId ?: courseClass.termId
                )
                val res = classDAO.updateCourseClass(updatedCourseClass)
                classDAO.deleteSpecificReportOnCourseClass(courseClass.courseClassId, reportId)
                res
            }

    override fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getReportedCourseClassVotes(classId, courseId, reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateReportedCourseClassVotes(classId, courseId, reportId, votes)
            }

    override fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteAllCourseReportsInClass(classId, courseId)
            }

    override fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificCourseReportInClass(classId, courseId, reportId)
            }

    override fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage> =
            jdbi.withExtension<List<CourseClassStage>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getStageEntriesOfCoursesInClass(classId)
            }

    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage> =
            jdbi.withExtension<Optional<CourseClassStage>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getSpecificStagedCourseInClass(classId, stageId)
            }

    override fun createStagingCourseInClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): CourseClassStage =
            jdbi.inTransaction<CourseClassStage, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termId = classDAO.getTermIdFromSpecificClass(classId)
                val courseClassStage = CourseClassStage(
                        courseId = courseId,
                        classId = classId,
                        termId = termId,
                        createdBy = courseClassInputModel.createdBy
                )
                classDAO.createStagingCourseInClass(courseClassStage)
            }

    override fun addCourseInClassFromStaged(classId: Int, stageId: Int): CourseClass =
    //TODO classID no usage
            jdbi.inTransaction<CourseClass, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClassStage = classDAO.getCourseClassStage(stageId).get()
                val courseClass = stagedToCourseClass(courseClassStage)
                classDAO.addCourseToClass(courseClass)
            }

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getStagedCourseClassVotes(classId, stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateStagedCourseClassVotes(classId, stageId, votes)
            }

    override fun deleteStageEntriesOfCourseInSpecificClass(classId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteStagedEntriesOfCourseInSpecificClass(classId)
            }

    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificStagedCourseInClass(classId, stageId)
            }

    /**
     * Lectures Methods
     */

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture> =
            jdbi.inTransaction<List<Lecture>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.getAllLecturesFromCourseInClass(classDAO.getCourseClassId(classId, courseId))
            }

    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): Optional<Lecture> =
            jdbi.inTransaction<Optional<Lecture>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.getSpecificLectureFromCourseInClass(classDAO.getCourseClassId(classId, courseId), lectureId)
            }

    override fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): Lecture =
            jdbi.inTransaction<Lecture, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.createLectureOnCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        toLecture(lectureInputModel)
                )
            }

    override fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = lectureDAO.getVotesOnLecture(classDAO.getCourseClassId(classId, courseId), lectureId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                lectureDAO.updateVotesOnLecture(lectureId, votes)
            }


    override fun deleteAllLecturesOfCourseInClass(classId: Int, courseId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.deleteAllLecturesOfCourseInClass(classDAO.getCourseClassId(classId, courseId))
            }

    override fun deleteSpecificLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.deleteSpecificLectureOfCourseInClass(classDAO.getCourseClassId(classId, courseId), lectureId)
            }

    override fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureReport> =
            jdbi.inTransaction<List<LectureReport>, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.getAllReportsOfLectureFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId
                )
            }

    override fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<LectureReport> =
            jdbi.inTransaction<Optional<LectureReport>, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.getSpecificReportOfLectureFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId,
                        reportId
                )
            }

    override fun deleteAllReportsOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.deleteAllReportsOnLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId
                )
            }

    override fun deleteSpecificReportOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.deleteSpecificReportOnLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId,
                        reportId
                )
            }

    override fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel): LectureReport =
    //TODO check params
            jdbi.withExtension<LectureReport, LectureDAOJdbi, Exception>(LectureDAOJdbi::class.java) {
                it.createReportOnLecture(toLectureReport(lectureReportInputModel))
            }

    override fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel): Int =
    //TODO check params
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                var votes = lectureDAO.getVotesOnReportedLecture(lectureId, reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                lectureDAO.updateVotesOnReportedLecture(lectureId, reportId, votes)
            }

    override fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Lecture =
            jdbi.inTransaction<Lecture, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val lecture = lectureDAO.getSpecificLectureFromCourseInClass(courseClassId, lectureId).get()
                val lectureReport = lectureDAO.getSpecificReportOfLectureFromCourseInClass(
                        courseClassId,
                        lectureId,
                        reportId
                ).get()
                val updatedLecture = Lecture(
                        lectureId = lectureId,
                        createdBy = lectureReport.reportedBy,
                        version = lecture.version.inc(),
                        weekDay = lectureReport.weekDay ?: lecture.weekDay,
                        begins = lectureReport.begins ?: lecture.begins,
                        duration = lectureReport.duration ?: lecture.duration,
                        location = lectureReport.location ?: lecture.location
                )
                val res = lectureDAO.updateLecture(updatedLecture)
                lectureDAO.createLectureVersion(toLectureVersion(updatedLecture))
                lectureDAO.deleteSpecificReportOnLectureOfCourseInClass(courseClassId, lectureId, reportId)
                res
            }

    override fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): List<LectureStage> =
            jdbi.inTransaction<List<LectureStage>, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.getAllStagedLecturesOfCourseInClass(classDAO.getCourseClassId(classId, courseId))
            }

    override fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Optional<LectureStage> =
            jdbi.inTransaction<Optional<LectureStage>, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.getSpecificStagedLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        stageId
                )
            }

    override fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): LectureStage =
            jdbi.inTransaction<LectureStage, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.createStagingLectureOnCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        toLectureStage(lectureInputModel)
                )
            }

    override fun deleteAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.deleteAllStagedLecturesOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId)
                )
            }

    override fun deleteSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.deleteSpecificStagedLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        stageId
                )
            }

    override fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = lectureDAO.getVotesOnStagedLecture(classDAO.getCourseClassId(classId, courseId), stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                lectureDAO.updateVotesOnStagedLecture(stageId, votes)
            }

    override fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int): Lecture =
            jdbi.inTransaction<Lecture, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val stagedLecture = lectureDAO.getSpecificStagedLectureOfCourseInClass(
                        courseClassId,
                        stageId
                ).get()
                val createdLecture = lectureDAO.createLectureOnCourseInClass(courseClassId, stagedToLecture(stagedLecture))
                lectureDAO.deleteSpecificStagedLectureOfCourseInClass(courseClassId, stageId)
                lectureDAO.createLectureVersion(toLectureVersion(createdLecture))
                createdLecture
            }

    override fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureVersion> =
            jdbi.inTransaction<List<LectureVersion>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.getAllVersionsOfLectureOfCourseInclass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId
                )
            }

    override fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): Optional<LectureVersion> =
            jdbi.inTransaction<Optional<LectureVersion>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.getSpecificVersionOfLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId,
                        version
                )
            }

    override fun deleteAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.deleteAllVersionsOfLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId
                )
            }

    override fun deleteSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.deleteSpecificVersionOfLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId,
                        version
                )
            }

    /**
     * Homeworks Methods
     */

    override fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): List<Homework> =
            jdbi.inTransaction<List<Homework>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                homeworkDAO.getAllHomeworksFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId)
                )
            }

    override fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Optional<Homework> =
            jdbi.inTransaction<Optional<Homework>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                )
            }

    override fun createHomeworkOnCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Homework =
            jdbi.inTransaction<Homework, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                homeworkDAO.createHomeworkOnCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        toHomework(homeworkInputModel)
                )
            }

    override fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = homeworkDAO.getVotesOnHomework(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                )
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                homeworkDAO.updateVotesOnHomework(homeworkId, votes)
            }

    override fun deleteAllHomeworksOfCourseInClass(classId: Int, courseId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteAllHomeworksOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId)
                )
            }

    override fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteSpecificHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                )
            }

    override fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): List<HomeworkStage> =
            jdbi.inTransaction<List<HomeworkStage>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getAllStagedHomeworksOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId)
                )
            }

    override fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Optional<HomeworkStage> =
            jdbi.inTransaction<Optional<HomeworkStage>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        stageId
                )
            }

    override fun createStagingHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): HomeworkStage =
            jdbi.inTransaction<HomeworkStage, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.createStagingHomeworkOnCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        toHomeworkStage(homeworkInputModel))
            }

    override fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int): Homework =
            jdbi.inTransaction<Homework, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(
                        courseClassId,
                        stageId
                ).get()
                val createdHomework = homeworkDAO.createHomeworkOnCourseInClass(courseClassId, stagedToHomework(stagedHomework))
                homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
                homeworkDAO.createHomeworkVersion(toHomeworkVersion(createdHomework))
                createdHomework
            }

    override fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                var votes = homeworkDAO.getVotesOnStagedHomework(
                        classDAO.getCourseClassId(classId, courseId),
                        stageId
                )
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                homeworkDAO.updateVotesOnStagedHomework(stageId, votes)
            }

    override fun deleteAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteAllStagedHomeworksOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId)
                )
            }

    override fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        stageId
                )
            }

    override fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): List<HomeworkReport> =
            jdbi.inTransaction<List<HomeworkReport>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getAllReportsOfHomeworkFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeWorkId
                )
            }

    override fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport> =
            jdbi.inTransaction<Optional<HomeworkReport>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId,
                        reportId
                )
            }

    override fun createReportOnHomeworkFromCourseInClass(//TODO params unused
            classId: Int,
            courseId: Int,
            homeworkId: Int,
            homeworkReportInputModel: HomeworkReportInputModel
    ): HomeworkReport =
            jdbi.withExtension<HomeworkReport, HomeworkDAOJdbi, Exception>(HomeworkDAOJdbi::class.java) {
                it.createReportOnHomework(toHomeworkReport(homeworkReportInputModel))
            }

    override fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Homework =
            jdbi.inTransaction<Homework, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId).get()
                val homeworkReport = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(
                        courseClassId,
                        homeworkId,
                        reportId
                ).get()
                val updatedHomework = Homework(
                        homeworkId = homeworkId,
                        createdBy = homeworkReport.reportedBy,
                        version = homework.version.inc(),
                        sheet = homeworkReport.sheet ?: homework.sheet,
                        dueDate = homeworkReport.dueDate ?: homework.dueDate,
                        lateDelivery = homeworkReport.lateDelivery ?: homework.lateDelivery,
                        multipleDeliveries = homeworkReport.multipleDeliveries ?: homework.multipleDeliveries
                )
                val res = homeworkDAO.updateHomeWork(updatedHomework)
                homeworkDAO.createHomeworkVersion(toHomeworkVersion(updatedHomework))
                homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId, homeworkId, reportId)
                res
            }

    override fun voteOnReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                //TODO no usage params
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                var votes = homeworkDAO.getVotesOnReportedHomework(homeworkId, reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                homeworkDAO.updateVotesOnReportedLecture(homeworkId, reportId, votes)
            }

    override fun deleteAllReportsOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteAllReportsOnHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                )
            }

    override fun deleteSpecificReportOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId,
                        reportId
                )
            }

    override fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): List<HomeworkVersion> =
            jdbi.inTransaction<List<HomeworkVersion>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getAllVersionsOfHomeworkOfCourseInclass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                )
            }

    override fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): Optional<HomeworkVersion> =
            jdbi.inTransaction<Optional<HomeworkVersion>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getSpecificVersionOfHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId,
                        version
                )
            }

    override fun deleteAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteAllVersionsOfHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                )
            }

    override fun deleteSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.deleteSpecificVersionOfHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId,
                        version
                )
            }

}