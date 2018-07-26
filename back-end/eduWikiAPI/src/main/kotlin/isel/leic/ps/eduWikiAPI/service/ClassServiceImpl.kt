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
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.HomeworkDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var classDAO: ClassDAO

    @Autowired
    lateinit var lectureDAO: LectureDAO

    @Autowired
    lateinit var homeworkDAO: HomeworkDAO

    @Autowired
    lateinit var handle: Handle

    /**
     * Class Methods
     */

    override fun getAllClasses(): List<Class> = classDAO.getAllClasses()

    override fun getSpecificClass(classId: Int): Optional<Class> = classDAO.getSpecificClass(classId)

    override fun createClass(input: ClassInputModel): Optional<Class> {
        handle.begin()
        val klass = classDAO.createClass(toClass(input)).get()
        classDAO.createClassVersion(toClassVersion(klass))
        handle.commit()
        return Optional.of(klass)
    }

    override fun voteOnClass(classId: Int, vote: VoteInputModel): Int =
            classDAO.voteOnClass(classId, Vote.valueOf(vote.vote))

    override fun partialUpdateOnClass(classId: Int, input: ClassInputModel): Int {
        handle.begin()
        val klass = classDAO.getSpecificClass(classId).get()
        val updatedClass = Class(
                classId = classId,
                version = klass.version.inc(),
                createdBy = input.createdBy,
                className = if (input.className.isEmpty()) klass.className else input.className
        )
        val res = classDAO.updateClass(updatedClass)
        classDAO.createClassVersion(toClassVersion(updatedClass))
        handle.commit()
        return res
    }

    override fun deleteSpecificClass(classId: Int): Int = classDAO.deleteSpecificClass(classId)

    override fun getAllReportsOfClass(classId: Int): List<ClassReport> =
            classDAO.getAllReportsFromClass(classId)

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): Optional<ClassReport> =
            classDAO.getSpecificReportFromClass(classId, reportId)

    override fun reportClass(classId: Int, report: ClassReportInputModel): Optional<ClassReport> =
            classDAO.reportClass(classId, toClassReport(classId, report))

    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int =
            classDAO.voteOnReportOfClass(classId, reportId, Vote.valueOf(vote.vote))

    override fun updateClassFromReport(classId: Int, reportId: Int): Int {
        handle.begin()
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
        handle.commit()
        return res
    }

    override fun deleteAllReportsInClass(classId: Int): Int = classDAO.deleteAllReportsInClass(classId)

    override fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int =
            classDAO.deleteSpecificReportInClass(classId, reportId)

    override fun getAllStagedClasses(): List<ClassStage> = classDAO.getAllStagedClasses()

    override fun getSpecificStagedClass(stageId: Int): Optional<ClassStage> =
            classDAO.getSpecificStagedClass(stageId)

    override fun createStagingClass(classStage: ClassStage): Optional<ClassStage> =
            classDAO.createStagedClass(classStage)

    override fun createClassFromStaged(stageId: Int): Optional<Class> {
        handle.begin()
        val classStaged = classDAO.getSpecificStagedClass(stageId).get()
        val createdClass = classDAO.createClass(stagedToClass(classStaged)).get()
        classDAO.deleteSpecificStagedClass(stageId)
        classDAO.createClassVersion(toClassVersion(createdClass))
        handle.commit()
        return Optional.of(createdClass)
    }

    override fun voteOnStagedClass(stageId: Int, vote: VoteInputModel): Int =
            classDAO.voteOnStagedClass(stageId, Vote.valueOf(vote.vote))

    override fun deleteAllStagedClasses(): Int = classDAO.deleteAllStagedClasses()

    override fun deleteSpecificStagedClass(stageId: Int): Int =
            classDAO.deleteSpecificStagedClass(stageId)

    override fun getAllVersionsOfClass(classId: Int): List<ClassVersion> =
            classDAO.getAllVersionsOfSpecificClass(classId)

    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): Optional<ClassVersion> =
            classDAO.getVersionOfSpecificClass(classId, versionId)

    override fun deleteAllVersionsOfClass(courseId: Int): Int =
            classDAO.deleteAllVersionsOfClass(courseId)

    override fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int =
            classDAO.deleteSpecificVersionOfClass(courseId, versionId)

    override fun getAllCoursesOfClass(classId: Int): List<Course> =
            classDAO.getAllCoursesOfClass(classId)

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course> =
            classDAO.getSpecificCourseOfClass(classId, courseId)

    override fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): Optional<CourseClass> {
        handle.begin()
        val termId = classDAO.getTermIdFromSpecificClass(classId)
        val courseClass = CourseClass(
                courseId = courseId,
                classId = classId,
                termId = termId,
                createdBy = courseClassInputModel.createdBy
        )
        val res = classDAO.addCourseToClass(courseClass).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel): Int =
            classDAO.voteOnCourseInClass(classId, courseId, Vote.valueOf(vote.vote))

    override fun deleteAllCoursesInClass(classId: Int): Int = classDAO.deleteAllCoursesInClass(classId)

    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int =
            classDAO.deleteSpecificCourseInClass(classId, courseId)

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport> =
            classDAO.getAllReportsOfCourseInClass(classId, courseId)

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport> =
            classDAO.getSpecificReportOfCourseInClass(classId, courseId, reportId)

    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel): Optional<CourseClassReport> {
        handle.begin()
        val courseClass = classDAO.getCourseClass(classId, courseId).get()
        val courseClassReport = CourseClassReport(
                courseClassId = courseClass.courseClassId,
                classId = courseClassReportInputModel.classId ?: courseClass.classId,
                courseId = courseClassReportInputModel.courseId ?: courseClass.courseId,
                termId = courseClassReportInputModel.termId ?: courseClass.termId,
                reportedBy = courseClassReportInputModel.reportedBy
        )
        val res = classDAO.reportCourseInClass(courseClassReport).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int): Optional<CourseClass> {
        handle.begin()
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
        handle.commit()
        return res
    }

    override fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int =
            classDAO.voteOnReportOfCourseClass(classId, courseId, reportId, Vote.valueOf(vote.vote))

    override fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int =
            classDAO.deleteAllCourseReportsInClass(classId, courseId)

    override fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int =
            classDAO.deleteSpecificCourseReportInClass(classId, courseId, reportId)

    override fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage> =
            classDAO.getStageEntriesOfCoursesInClass(classId)

    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage> =
            classDAO.getSpecificStagedCourseInClass(classId, stageId)

    override fun createStagingCourseInClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel): Optional<CourseClassStage> {
        handle.begin()
        val termId = classDAO.getTermIdFromSpecificClass(classId)
        val courseClassStage = CourseClassStage(
                courseId = courseId,
                classId = classId,
                termId = termId,
                createdBy = courseClassInputModel.createdBy
        )
        val res = classDAO.createStagingCourseInClass(courseClassStage).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun addCourseInClassFromStaged(classId: Int, stageId: Int): Optional<CourseClass> {
        //TODO classID no usage
        handle.begin()
        val courseClassStage = classDAO.getCourseClassStage(stageId).get()
        val courseClass = stagedToCourseClass(courseClassStage)
        val res = classDAO.addCourseToClass(courseClass).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel): Int =
            classDAO.voteOnStagedCourseInClass(classId, stageId, Vote.valueOf(vote.vote))

    override fun deleteStageEntriesOfCourseInSpecificClass(classId: Int): Int =
            classDAO.deleteStagedEntriesOfCourseInSpecificClass(classId)

    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int =
            classDAO.deleteSpecificStagedCourseInClass(classId, stageId)

    /**
     * Lectures Methods
     */

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture> =
            lectureDAO.getAllLecturesFromCourseInClass(classDAO.getCourseClassId(classId, courseId))


    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): Optional<Lecture> =
            lectureDAO.getSpecificLectureFromCourseInClass(classDAO.getCourseClassId(classId, courseId), lectureId)


    override fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): Optional<Lecture> {
        handle.begin()
        val classMiscUnit = classDAO.createClassMiscUnit(
                classDAO.getCourseClassId(classId, courseId),
                "Lecture"
        ).get()
        val res = lectureDAO.createLecture(toLecture(classMiscUnit, lectureInputModel)).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel): Int =
            lectureDAO.voteOnLectureOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId,
                    Vote.valueOf(vote.vote)
            )

    override fun deleteAllLecturesOfCourseInClass(classId: Int, courseId: Int): Int =
            lectureDAO.deleteAllLecturesOfCourseInClass(classDAO.getCourseClassId(classId, courseId))


    override fun deleteSpecificLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int =
            lectureDAO.deleteSpecificLectureOfCourseInClass(classDAO.getCourseClassId(classId, courseId), lectureId)

    override fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureReport> =
            lectureDAO.getAllReportsOfLectureFromCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId
            )

    override fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<LectureReport> =
            lectureDAO.getSpecificReportOfLectureFromCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId,
                    reportId
            )

    override fun deleteAllReportsOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int =
            lectureDAO.deleteAllReportsOnLectureOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId
            )

    override fun deleteSpecificReportOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Int =
            lectureDAO.deleteSpecificReportOnLectureOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId,
                    reportId
            )

    override fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel): Optional<LectureReport> =
    //TODO check params
            lectureDAO.createReportOnLecture(toLectureReport(lectureReportInputModel))

    override fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel): Int =
    //TODO check params
            lectureDAO.voteOnReportOfLecture(lectureId, reportId, Vote.valueOf(vote.vote))


    override fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int): Optional<Lecture> {
        handle.begin()
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
        val res = lectureDAO.updateLecture(updatedLecture).get()
        lectureDAO.createLectureVersion(toLectureVersion(updatedLecture))
        lectureDAO.deleteSpecificReportOnLectureOfCourseInClass(courseClassId, lectureId, reportId)
        handle.commit()
        return Optional.of(res)
    }

    override fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): List<LectureStage> =
            lectureDAO.getAllStagedLecturesOfCourseInClass(classDAO.getCourseClassId(classId, courseId))


    override fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Optional<LectureStage> =
            lectureDAO.getSpecificStagedLectureOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    stageId
            )

    override fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel): Optional<LectureStage> {
        handle.begin()
        val classMiscUnitStage = classDAO.createStagingClassMiscUnit(
                classDAO.getCourseClassId(classId, courseId),
                "Lecture"
        ).get()
        val res = lectureDAO.createStagingLecture(toLectureStage(classMiscUnitStage, lectureInputModel)).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun deleteAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): Int =
            classDAO.deleteAllClassMiscUnitsFromTypeOfCourseInClass(classDAO.getCourseClassId(classId, courseId), "Lecture")

    override fun deleteSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int =
            classDAO.deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(classDAO.getCourseClassId(classId, courseId), stageId)

    override fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int =
            lectureDAO.voteOnStagedLectureOfCourseInClass(classDAO.getCourseClassId(classId, courseId), stageId, Vote.valueOf(vote.vote))

    override fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int): Optional<Lecture> {
        handle.begin()
        val courseClassId = classDAO.getCourseClassId(classId, courseId)
        val stagedLecture = lectureDAO.getSpecificStagedLectureOfCourseInClass(
                courseClassId,
                stageId
        ).get()
        val classMiscUnit = classDAO.createClassMiscUnit(courseClassId, "Lecture").get()
        val createdLecture = lectureDAO.createLecture(stagedToLecture(classMiscUnit, stagedLecture)).get()
        classDAO.deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId, stageId)
        lectureDAO.createLectureVersion(toLectureVersion(createdLecture))
        handle.commit()
        return Optional.of(createdLecture)
    }

    override fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): List<LectureVersion> =
            lectureDAO.getAllVersionsOfLectureOfCourseInclass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId
            )

    override fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): Optional<LectureVersion> =
            lectureDAO.getSpecificVersionOfLectureOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId,
                    version
            )

    override fun deleteAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): Int =
            lectureDAO.deleteAllVersionsOfLectureOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId
            )

    override fun deleteSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): Int =
            lectureDAO.deleteSpecificVersionOfLectureOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    lectureId,
                    version
            )

    /**
     * Homeworks Methods
     */

    override fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): List<Homework> =
            homeworkDAO.getAllHomeworksFromCourseInClass(classDAO.getCourseClassId(classId, courseId))

    override fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Optional<Homework> =
            homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId
            )

    override fun createHomeworkOnCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Optional<Homework> {
        handle.begin()
        val classMiscUnit = classDAO.createClassMiscUnit(
                classDAO.getCourseClassId(classId, courseId),
                "Homework"
        ).get()
        val res = homeworkDAO.createHomework(toHomework(classMiscUnit, homeworkInputModel)).get()
        handle.commit()
        return Optional.of(res)
    }


    override fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel): Int =
            homeworkDAO.voteOnHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId,
                    Vote.valueOf(vote.vote)
            )

    override fun deleteAllHomeworksOfCourseInClass(classId: Int, courseId: Int): Int =
            homeworkDAO.deleteAllHomeworksOfCourseInClass(classDAO.getCourseClassId(classId, courseId))

    override fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int =
            homeworkDAO.deleteSpecificHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId
            )

    override fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): List<HomeworkStage> =
            homeworkDAO.getAllStagedHomeworksOfCourseInClass(classDAO.getCourseClassId(classId, courseId))

    override fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Optional<HomeworkStage> =
            homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    stageId
            )

    override fun createStagingHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel): Optional<HomeworkStage> {
        handle.begin()
        val classMiscUnitStage = classDAO.createStagingClassMiscUnit(
                classDAO.getCourseClassId(classId, courseId),
                "Homework"
        ).get()
        val res = homeworkDAO.createStagingHomework(toHomeworkStage(classMiscUnitStage, homeworkInputModel)).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int): Optional<Homework> {
        handle.begin()
        val courseClassId = classDAO.getCourseClassId(classId, courseId)
        val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(
                courseClassId,
                stageId
        ).get()
        val classMiscUnit = classDAO.createClassMiscUnit(courseClassId, "Homework").get()
        val createdHomework = homeworkDAO.createHomework(stagedToHomework(classMiscUnit, stagedHomework)).get()
        homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
        homeworkDAO.createHomeworkVersion(toHomeworkVersion(createdHomework))
        handle.commit()
        return Optional.of(createdHomework)
    }

    override fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel): Int =
            homeworkDAO.voteOnStagedHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    stageId,
                    Vote.valueOf(vote.vote)
            )

    override fun deleteAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): Int =
            classDAO.deleteAllClassMiscUnitsFromTypeOfCourseInClass(classDAO.getCourseClassId(classId, courseId), "Homework")

    override fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): Int =
            classDAO.deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(classDAO.getCourseClassId(classId, courseId), stageId)

    override fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): List<HomeworkReport> =
            homeworkDAO.getAllReportsOfHomeworkFromCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeWorkId
            )

    override fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport> =
            homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId,
                    reportId
            )

    override fun createReportOnHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, homeworkReportInputModel: HomeworkReportInputModel): Optional<HomeworkReport> =
    //TODO params unused
            homeworkDAO.createReportOnHomework(toHomeworkReport(homeworkReportInputModel))

    override fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Optional<Homework> {
        handle.begin()
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
        val res = homeworkDAO.updateHomeWork(updatedHomework).get()
        homeworkDAO.createHomeworkVersion(toHomeworkVersion(updatedHomework))
        homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId, homeworkId, reportId)
        handle.commit()
        return Optional.of(res)
    }

    override fun voteOnReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, vote: VoteInputModel): Int =
        //TODO no usage params
         homeworkDAO.voteOnReportOfHomeworkOfCourseInClass(homeworkId, reportId, Vote.valueOf(vote.vote))


    override fun deleteAllReportsOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int =
            homeworkDAO.deleteAllReportsOnHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId
            )

    override fun deleteSpecificReportOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): Int =
            homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId,
                    reportId
            )

    override fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): List<HomeworkVersion> =
            homeworkDAO.getAllVersionsOfHomeworkOfCourseInclass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId
            )

    override fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): Optional<HomeworkVersion> =
            homeworkDAO.getSpecificVersionOfHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId,
                    version
            )

    override fun deleteAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): Int =
            homeworkDAO.deleteAllVersionsOfHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId
            )

    override fun deleteSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): Int =
            homeworkDAO.deleteSpecificVersionOfHomeworkOfCourseInClass(
                    classDAO.getCourseClassId(classId, courseId),
                    homeworkId,
                    version
            )

}