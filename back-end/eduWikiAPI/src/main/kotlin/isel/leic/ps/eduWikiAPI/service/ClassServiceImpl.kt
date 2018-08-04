package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.outputModel.ClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.LectureOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.ClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.CourseClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.HomeworkReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.LectureReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.ClassStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.CourseClassStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.HomeworkStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.LectureStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.ClassVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.HomeworkVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.LectureVersionOutputModel
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.exceptions.UnknownDataException
import isel.leic.ps.eduWikiAPI.repository.*
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var jdbi: Jdbi

    @Autowired
    lateinit var storageService: ResourceStorageService

    /**
     * Class Methods
     */

    fun getTerm(termDAOJdbi: TermDAOJdbi, termId: Int): Term = termDAOJdbi.getTerm(termId)
            .orElseThrow {
                UnknownDataException(
                        msg = "Can't find term with id $termId",
                        action = "Try Again Later"
                )
            }

    fun getCourse(courseDAOJdbi: CourseDAOJdbi, courseId: Int): Course = courseDAOJdbi.getSpecificCourse(courseId)
            .orElseThrow {
                UnknownDataException(
                        msg = "Can't find course with id $courseId",
                        action = "Try Again Later"
                )
            }

    override fun getAllClasses(): List<ClassOutputModel> =
            jdbi.inTransaction<List<ClassOutputModel>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val classes = classDAO.getAllClasses()
                classes.map { toClassOutputModel(it, getTerm(termDAOJdbi, it.termId)) }
            }

    override fun getSpecificClass(classId: Int): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No class found with id $classId",
                                    action = "Try other ID"
                            )
                        }
                toClassOutputModel(klass, getTerm(termDAOJdbi, klass.termId))
            }

    override fun createClass(input: ClassInputModel): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.createClass(toClass(input))
                classDAO.createClassVersion(toClassVersion(klass))
                toClassOutputModel(klass, getTerm(termDAOJdbi, klass.termId))
            }

    override fun voteOnClass(classId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getClassVotes(classId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateClassVotes(classId, votes)
            }

    override fun partialUpdateOnClass(classId: Int, input: ClassInputModel): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId).orElseThrow {
                    NotFoundException(
                            msg = "No class found with id $classId",
                            action = "Try other ID"
                    )
                }
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val updatedClass = Class(
                        classId = classId,
                        version = klass.version.inc(),
                        createdBy = input.createdBy,
                        className = if (input.className.isEmpty()) klass.className else input.className
                )
                val res = classDAO.updateClass(updatedClass)
                classDAO.createClassVersion(toClassVersion(updatedClass))
                toClassOutputModel(res, getTerm(termDAOJdbi, res.termId))
            }

    override fun deleteSpecificClass(classId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificClass(classId)
            }

    override fun getAllReportsOfClass(classId: Int): List<ClassReportOutputModel> =
            jdbi.withExtension<List<ClassReportOutputModel>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllReportsFromClass(classId).map { toClassReportOutputModel(it) }
            }

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReportOutputModel =
            jdbi.withExtension<ClassReportOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                toClassReportOutputModel(
                        it.getSpecificReportFromClass(classId, reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report found",
                                            action = "Try other report Id"
                                    )
                                }
                )
            }

    override fun reportClass(classId: Int, report: ClassReportInputModel): ClassReportOutputModel =
            jdbi.withExtension<ClassReportOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                toClassReportOutputModel(it.reportClass(classId, toClassReport(classId, report)))
            }

    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                var votes = classDAO.getReportedClassVotes(classId, reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                classDAO.updateReportedClassVotes(classId, reportId, votes)
            }

    override fun updateClassFromReport(classId: Int, reportId: Int): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
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
                toClassOutputModel(res, termDAOJdbi.getTerm(res.termId).get())
            }

    override fun deleteAllReportsInClass(classId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteAllReportsInClass(classId)
            }

    override fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificReportInClass(classId, reportId)
            }

    override fun getAllStagedClasses(): List<ClassStageOutputModel> =
            jdbi.inTransaction<List<ClassStageOutputModel>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                classDAO.getAllStagedClasses().map {
                    toClassStagedOutputModel(it, getTerm(termDAOJdbi, it.termId))
                }
            }

    override fun getSpecificStagedClass(stageId: Int): ClassStageOutputModel =
            jdbi.inTransaction<ClassStageOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val classStage = classDAO.getSpecificStagedClass(stageId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No class staged found",
                                    action = "Try other id"
                            )
                        }
                toClassStagedOutputModel(classStage, getTerm(termDAOJdbi, classStage.termId))
            }

    override fun createStagingClass(classStage: ClassStage): ClassStageOutputModel =
            jdbi.inTransaction<ClassStageOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val res = classDAO.createStagedClass(classStage)
                toClassStagedOutputModel(res, getTerm(termDAOJdbi, res.termId))
            }

    override fun createClassFromStaged(stageId: Int): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val classStaged = classDAO.getSpecificStagedClass(stageId).get()
                val createdClass = classDAO.createClass(stagedToClass(classStaged))
                classDAO.deleteSpecificStagedClass(stageId)
                classDAO.createClassVersion(toClassVersion(createdClass))
                toClassOutputModel(createdClass, getTerm(termDAOJdbi, createdClass.termId))
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

    override fun getAllVersionsOfClass(classId: Int): List<ClassVersionOutputModel> =
            jdbi.inTransaction<List<ClassVersionOutputModel>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                classDAO.getAllVersionsOfSpecificClass(classId).map {
                    toClassVersionOutputModel(it, getTerm(termDAOJdbi, it.termId))
                }
            }

    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): ClassVersionOutputModel =
            jdbi.inTransaction<ClassVersionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val classVersion = classDAO.getVersionOfSpecificClass(classId, versionId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No version found",
                                    action = "Try with other version number"
                            )
                        }
                toClassVersionOutputModel(classVersion, getTerm(termDAOJdbi, classVersion.termId))
            }

    override fun deleteAllVersionsOfClass(courseId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteAllVersionsOfClass(courseId)
            }

    override fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.deleteSpecificVersionOfClass(courseId, versionId)
            }

    /**
     * Course Class
     */
    override fun getAllCoursesOfClass(classId: Int): List<CourseClassOutputModel> =
            jdbi.inTransaction<List<CourseClassOutputModel>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId).orElseThrow {
                    NotFoundException(
                            msg = "No class found with this id",
                            action = "Try other id"
                    )
                }
                classDAO.getAllCoursesOfClass(classId).map {
                    toCourseClassOutputModel(getCourse(courseDAOJdbi, it.courseId), klass, it, getTerm(termDAOJdbi, it.termId))
                }

            }

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): CourseClassOutputModel =
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId).orElseThrow {
                    NotFoundException(
                            msg = "No class found with this id",
                            action = "Try other id"
                    )
                }
                val courseClass = classDAO.getSpecificCourseOfClass(classId, courseId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No course found with this id for this class",
                                    action = "Try other courseId"
                            )
                        }
                val course = getCourse(courseDAOJdbi, courseId)
                toCourseClassOutputModel(course, klass, courseClass, getTerm(termDAOJdbi, courseClass.termId))
            }

    override fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): CourseClassOutputModel =
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No class found",
                                    action = "Try other id"
                            )
                        }
                val term = getTerm(termDAOJdbi, klass.termId)
                val courseClass = CourseClass(
                        courseId = courseId,
                        classId = classId,
                        termId = term.termId,
                        createdBy = courseClassInputModel.createdBy
                )
                val created = classDAO.addCourseToClass(courseClass)
                val course = getCourse(courseDAOJdbi, courseId)
                toCourseClassOutputModel(course, klass, created, term)
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

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReportOutputModel> =
            jdbi.inTransaction<List<CourseClassReportOutputModel>, Exception> {
                val classDAOJdbi = it.attach(ClassDAOJdbi::class.java)
                val courseClass = classDAOJdbi.getCourseClass(classId, courseId).get()
                classDAOJdbi.getAllReportsOfCourseInClass(courseClass.courseClassId).map { toCourseClassReportOutputModel(it) }
            }

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): CourseClassReportOutputModel =
            jdbi.withExtension<CourseClassReportOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                toCourseClassReportOutputModel(
                        it.getSpecificReportOfCourseInClass(reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report found",
                                            action = "Try with other report ID"
                                    )
                                }
                )
            }

    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): CourseClassReportOutputModel =
            jdbi.inTransaction<CourseClassReportOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClass = classDAO.getCourseClass(classId, courseId).orElseThrow {
                    NotFoundException(
                            msg = "No courseClass found",
                            action = "Try with other courseClass ID"
                    )
                }
                val courseClassReport = CourseClassReport(
                        courseClassId = courseClass.courseClassId,
                        classId = courseClassReportInputModel.classId ?: courseClass.classId,
                        courseId = courseClassReportInputModel.courseId ?: courseClass.courseId,
                        termId = courseClassReportInputModel.termId ?: courseClass.termId,
                        reportedBy = courseClassReportInputModel.reportedBy,
                        deleltePermanently = courseClassReportInputModel.deletePermanently
                )
                val report = classDAO.reportCourseInClass(courseClassReport)
                toCourseClassReportOutputModel(report)
            }

    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): CourseClassOutputModel =
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId).get()
                val courseClass = classDAO.getCourseClass(classId, courseId).get()
                val updatedCourseClass = CourseClass(
                        courseClassId = courseClass.courseClassId,
                        createdBy = courseClassReport.reportedBy,
                        courseId = courseClassReport.courseId ?: courseClass.courseId,
                        classId = courseClassReport.classId ?: courseClass.courseId,
                        termId = courseClassReport.termId ?: courseClass.termId
                )
                classDAO.deleteSpecificReportOnCourseClass(courseClass.courseClassId, reportId)
                classDAO.deleteSpecificCourseInClass(classId, courseId)
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No class found",
                                    action = "Try other id"
                            )
                        }
                val term = getTerm(termDAOJdbi, klass.termId)
                val course = getCourse(courseDAOJdbi, courseId)
                if (!courseClassReport.deleltePermanently) {
                    toCourseClassOutputModel(
                            course,
                            klass,
                            classDAO.addCourseToClass(updatedCourseClass),
                            term
                    )
                } else toCourseClassOutputModel(course, klass, courseClass, term)


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

    override fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStageOutputModel> =
            jdbi.withExtension<List<CourseClassStageOutputModel>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getStageEntriesOfCoursesInClass(classId).map { toCourseClassStageOutputModel(it) }
            }

    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): CourseClassStageOutputModel =
            jdbi.withExtension<CourseClassStageOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                toCourseClassStageOutputModel(it.getSpecificStagedCourseInClass(classId, stageId).orElseThrow {
                    NotFoundException(
                            msg = "No staged course class",
                            action = "Try other staged id"
                    )
                })
            }

    override fun createStagingCourseInClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): CourseClassStageOutputModel =
            jdbi.inTransaction<CourseClassStageOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termId = classDAO.getTermIdFromSpecificClass(classId)
                val courseClassStage = CourseClassStage(
                        courseId = courseId,
                        classId = classId,
                        termId = termId,
                        createdBy = courseClassInputModel.createdBy
                )
                toCourseClassStageOutputModel(classDAO.createStagingCourseInClass(courseClassStage))
            }

    override fun addCourseInClassFromStaged(classId: Int, stageId: Int): CourseClassOutputModel =
    //TODO classID no usage
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val courseClassStage = classDAO.getCourseClassStage(stageId).get()
                val courseClass = stagedToCourseClass(courseClassStage)
                val created = classDAO.addCourseToClass(courseClass)
                val klass = classDAO.getSpecificClass(classId).get()
                val term = getTerm(termDAOJdbi, klass.termId)
                val course = courseDAOJdbi.getSpecificCourse(created.classId).get()
                toCourseClassOutputModel(course, klass, created, term)
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

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<LectureOutputModel> =
            jdbi.inTransaction<List<LectureOutputModel>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.getAllLecturesFromCourseInClass(classDAO.getCourseClassId(classId, courseId)).map { toLectureOutputModel(it) }
            }

    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                toLectureOutputModel(
                        lectureDAO.getSpecificLectureFromCourseInClass(classDAO.getCourseClassId(classId, courseId), lectureId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No Lecture found",
                                            action = "Try another id"
                                    )
                                }
                )
            }

    override fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lecture = lectureDAO.createLectureOnCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        toLecture(lectureInputModel)
                )
                lectureDAO.createLectureVersion(toLectureVersion(lecture))
                toLectureOutputModel(lecture)
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

    override fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureReportOutputModel> =
            jdbi.inTransaction<List<LectureReportOutputModel>, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.getAllReportsOfLectureFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId
                ).map { toLectureReportOutputModel(it) }
            }

    override fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureReportOutputModel =
            jdbi.inTransaction<LectureReportOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toLectureReportOutputModel(lectureDAO.getSpecificReportOfLectureFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId,
                        reportId
                ).orElseThrow { NotFoundException(msg = "No report found", action = "Try other report id") }
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

    override fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
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
                toLectureOutputModel(res)
            }

    override fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): List<LectureStageOutputModel> =
            jdbi.inTransaction<List<LectureStageOutputModel>, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                lectureDAO.getAllStagedLecturesOfCourseInClass(classDAO.getCourseClassId(classId, courseId)).map {
                    toLectureStageOutputModel(it)
                }
            }

    override fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): LectureStageOutputModel =
            jdbi.inTransaction<LectureStageOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toLectureStageOutputModel(lectureDAO.getSpecificStagedLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        stageId
                ).orElseThrow {
                    NotFoundException(
                            msg = "No staged lecture found",
                            action = "Try with other id"
                    )
                })
            }

    override fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): LectureStageOutputModel =
            jdbi.inTransaction<LectureStageOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toLectureStageOutputModel(lectureDAO.createStagingLectureOnCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        toLectureStage(lectureInputModel)
                ))
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

    override fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
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
                toLectureOutputModel(createdLecture)
            }

    override fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureVersionOutputModel> =
            jdbi.inTransaction<List<LectureVersionOutputModel>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                lectureDAO.getAllVersionsOfLectureOfCourseInclass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId
                ).map { toLectureVersionOutputModel(it) }
            }

    override fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): LectureVersionOutputModel =
            jdbi.inTransaction<LectureVersionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                toLectureVersionOutputModel(lectureDAO.getSpecificVersionOfLectureOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        lectureId,
                        version
                ).orElseThrow {
                    NotFoundException(
                            msg = "No version for this lecture",
                            action = "Try with other version number"
                    )
                })
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

    override fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): List<HomeworkOutputModel> =
            jdbi.inTransaction<List<HomeworkOutputModel>, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                homeworkDAO.getAllHomeworksFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId)
                ).map { toHomeworkOutputModel(it) }
            }

    override fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkOutputModel =
            jdbi.inTransaction<HomeworkOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                toHomeworkOutputModel(homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                ).orElseThrow {
                    NotFoundException(
                            msg = "No homework found",
                            action = "Try other homework id"
                    )
                })
            }

    override fun createHomeworkOnCourseInClass(
            sheet: MultipartFile,
            classId: Int,
            courseId: Int,
            homeworkInputModel: HomeworkInputModel
    ): HomeworkOutputModel = jdbi.inTransaction<HomeworkOutputModel, Exception> {
        val classDAO = it.attach(ClassDAOJdbi::class.java)
        val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
        val createdHomework = homeworkDAO.createHomeworkOnCourseInClass(
                classDAO.getCourseClassId(classId, courseId),
                toHomework(homeworkInputModel)
        )
        homeworkDAO.createHomeworkVersion(toHomeworkVersion(createdHomework))
        storageService.storeResource(createdHomework.sheetId, sheet)
        toHomeworkOutputModel(createdHomework)
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
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val homeworks = homeworkDAO.getAllHomeworksFromCourseInClass(courseClassId)
                storageService.batchDeleteResource(homeworks.map(Homework::sheetId))
                homeworkDAO.deleteAllHomeworksOfCourseInClass(courseClassId)
                homeworkDAO.deleteAllHomeworksOfCourseInClass(courseClassId)
            }

    override fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId).get()
                storageService.deleteSpecificResource(homework.sheetId)
                homeworkDAO.deleteSpecificHomeworkOfCourseInClass(
                        courseClassId,
                        homeworkId
                )
                homeworkDAO.deleteSpecificHomeworkOfCourseInClass(courseClassId, homeworkId)
            }

    override fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): List<HomeworkStageOutputModel> =
            jdbi.inTransaction<List<HomeworkStageOutputModel>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getAllStagedHomeworksOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId)
                ).map { toHomeworkStagedOutputModel(it) }
            }

    override fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): HomeworkStageOutputModel =
            jdbi.inTransaction<HomeworkStageOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toHomeworkStagedOutputModel(homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        stageId
                ).orElseThrow {
                    NotFoundException(
                            msg = "No staged homework",
                            action = "Try with other stage id"
                    )
                })
            }

    override fun createStagingHomeworkOnCourseInClass(sheet: MultipartFile, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): HomeworkStageOutputModel =
            jdbi.inTransaction<HomeworkStageOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val stagingHomework = homeworkDAO.createStagingHomeworkOnCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        toHomeworkStage(homeworkInputModel)
                )
                storageService.storeResource(stagingHomework.sheetId, sheet)
                toHomeworkStagedOutputModel(stagingHomework)
            }

    override fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int): HomeworkOutputModel =
            jdbi.inTransaction<HomeworkOutputModel, Exception> {
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
                toHomeworkOutputModel(createdHomework)
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
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val homeworks = homeworkDAO.getAllStagedHomeworksOfCourseInClass(
                       courseClassId
                )
                storageService.batchDeleteResource(homeworks.map(HomeworkStage::sheetId))
                homeworkDAO.deleteAllStagedHomeworksOfCourseInClass(
                        courseClassId
                )
                homeworkDAO.deleteAllStagedHomeworksOfCourseInClass(courseClassId)
            }

    override fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClassId = classDAO.getCourseClassId(classId, courseId)
                val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(
                        courseClassId,
                        stageId
                ).get()
                storageService.deleteSpecificResource(stagedHomework.sheetId)
                homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(
                        courseClassId,
                        stageId
                )
                homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
            }

    override fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): List<HomeworkReportOutputModel> =
            jdbi.inTransaction<List<HomeworkReportOutputModel>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getAllReportsOfHomeworkFromCourseInClass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeWorkId
                ).map { toHomeworkReportOutputModel(it) }
            }

    override fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkReportOutputModel =
            jdbi.inTransaction<HomeworkReportOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toHomeworkReportOutputModel(
                        homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(
                                classDAO.getCourseClassId(classId, courseId),
                                homeworkId,
                                reportId
                        ).orElseThrow {
                            NotFoundException(
                                    msg = "No report found",
                                    action = "Try with other report id"
                            )
                        }
                )
            }

    override fun createReportOnHomeworkFromCourseInClass(//TODO params unused
            classId: Int,
            courseId: Int,
            homeworkId: Int,
            homeworkReportInputModel: HomeworkReportInputModel
    ): HomeworkReportOutputModel =
            jdbi.withExtension<HomeworkReportOutputModel, HomeworkDAOJdbi, Exception>(HomeworkDAOJdbi::class.java) {
                toHomeworkReportOutputModel(
                        it.createReportOnHomework(toHomeworkReport(homeworkReportInputModel))
                )
            }

    override fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkOutputModel =
            jdbi.inTransaction<HomeworkOutputModel, Exception> {
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
                        sheetId = homeworkReport.sheetId ?: homework.sheetId,
                        dueDate = homeworkReport.dueDate ?: homework.dueDate,
                        lateDelivery = homeworkReport.lateDelivery ?: homework.lateDelivery,
                        multipleDeliveries = homeworkReport.multipleDeliveries ?: homework.multipleDeliveries
                )
                val res = homeworkDAO.updateHomeWork(updatedHomework)
                homeworkDAO.createHomeworkVersion(toHomeworkVersion(updatedHomework))
                homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId, homeworkId, reportId)
                toHomeworkOutputModel(res)
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

    override fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): List<HomeworkVersionOutputModel> =
            jdbi.inTransaction<List<HomeworkVersionOutputModel>, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                homeworkDAO.getAllVersionsOfHomeworkOfCourseInclass(
                        classDAO.getCourseClassId(classId, courseId),
                        homeworkId
                ).map { toHomeworkVersionOutputModel(it) }
            }

    override fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): HomeworkVersionOutputModel =
            jdbi.inTransaction<HomeworkVersionOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toHomeworkVersionOutputModel(
                        homeworkDAO.getSpecificVersionOfHomeworkOfCourseInClass(
                                classDAO.getCourseClassId(classId, courseId),
                                homeworkId,
                                version
                        ).orElseThrow {
                            NotFoundException(
                                    msg = "No version found",
                                    action = "Try with other version number"
                            )
                        })
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