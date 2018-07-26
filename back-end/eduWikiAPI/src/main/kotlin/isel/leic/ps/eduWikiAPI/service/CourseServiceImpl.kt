package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var courseDAO: CourseDAO
    @Autowired
    lateinit var examDAO: ExamDAO
    @Autowired
    lateinit var workAssignmentDAO: WorkAssignmentDAO
    @Autowired
    lateinit var classDAO: ClassDAO

    @Autowired
    lateinit var handle: Handle

    override fun getAllCourses(): List<Course> = courseDAO.getAllCourses()

    override fun getSpecificCourse(courseId: Int) = courseDAO.getSpecificCourse(courseId)

    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport> =
            courseDAO.getAllReportsOnCourse(courseId)

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int) =
            courseDAO.getSpecificReportOfCourse(courseId, reportId)

    override fun getAllCourseStageEntries(): List<CourseStage> = courseDAO.getAllCourseStageEntries()

    override fun getCourseSpecificStageEntry(stageId: Int) = courseDAO.getCourseSpecificStageEntry(stageId)

    override fun getTermsOfCourse(courseId: Int): List<Term> = courseDAO.getTermsOfCourse(courseId)

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int) =
            courseDAO.getSpecificTermOfCourse(courseId, termId)

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam> =
            examDAO.getAllExamsFromSpecificTermOfCourse(courseId, termId)

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam> =
            examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage> =
            examDAO.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage> =
            examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport> =
            examDAO.getAllReportsOnExamOnSpecificTermOfCourse(examId)

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): Optional<ExamReport> =
            examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(reportId)

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment> =
            workAssignmentDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int, courseId: Int, termId: Int): Optional<WorkAssignment> =
            workAssignmentDAO.getSpecificWorkAssignment(workAssignmentId, courseId, termId)

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage> =
            workAssignmentDAO.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage> =
            workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport> =
            workAssignmentDAO.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId)

    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport> =
            workAssignmentDAO.getSpecificReportOfWorkAssignment(workAssignmentId, reportId)

    override fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> =
            classDAO.getAllClassesOnSpecificTermOfCourse(courseId, termId)

    override fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Optional<Class> =
            classDAO.getClassOnSpecificTermOfCourse(courseId, termId, classId)

    override fun createCourse(inputCourse: CourseInputModel): Optional<Course> {
        handle.begin()
        val course = courseDAO.createCourse(toCourse(inputCourse)).get()
        courseDAO.createCourseVersion(toCourseVersion(course))
        handle.commit()
        return Optional.of(course)
    }

    override fun voteOnCourse(courseId: Int, inputVote: VoteInputModel): Int =
            courseDAO.voteOnCourse(courseId, Vote.valueOf(inputVote.vote))

    override fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel): Optional<CourseReport> =
            courseDAO.reportCourse(courseId, toCourseReport(courseId, inputCourseReport))

    override fun voteOnReportOfCourse(reportId: Int, inputVote: VoteInputModel): Int =
            courseDAO.voteOnReportOfCourse(reportId, Vote.valueOf(inputVote.vote))

    override fun updateReportedCourse(courseId: Int, reportId: Int): Optional<Course> {
        handle.begin()
        val course = courseDAO.getSpecificCourse(courseId).get()
        val report = courseDAO.getSpecificReportOfCourse(courseId, reportId).get()
        val updatedCourse = Course(
                courseId = courseId,
                organizationId = course.organizationId,
                version = course.version.inc(),
                createdBy = report.reportedBy,
                fullName = report.fullName ?: course.fullName,
                shortName = report.shortName ?: course.shortName
        )
        val res = courseDAO.updateCourse(updatedCourse).get()
        courseDAO.createCourseVersion(toCourseVersion(updatedCourse))
        courseDAO.deleteReportOnCourse(reportId)
        handle.commit()
        return Optional.of(res)
    }

    override fun createStagingCourse(inputCourse: CourseInputModel): Optional<CourseStage> =
            courseDAO.createStagedCourse(toCourseStage(inputCourse))

    override fun createCourseFromStaged(stageId: Int): Optional<Course> {
        handle.begin()
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId).get()
        val createdCourse = courseDAO.createCourse(stagedToCourse(courseStage)).get()
        courseDAO.deleteStagedCourse(stageId)
        courseDAO.createCourseVersion(toCourseVersion(createdCourse))
        handle.commit()
        return Optional.of(createdCourse)
    }

    override fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel): Optional<Course> {
        handle.begin()
        val course = courseDAO.getSpecificCourse(courseId).get()
        val updatedCourse = Course(
                courseId = courseId,
                version = course.version.inc(),
                organizationId = course.organizationId,
                createdBy = inputCourse.createdBy,
                fullName = if (inputCourse.fullName.isEmpty()) course.fullName else inputCourse.fullName,
                shortName = if (inputCourse.shortName.isEmpty()) course.shortName else inputCourse.shortName
        )
        val res = courseDAO.updateCourse(updatedCourse).get()
        courseDAO.createCourseVersion(toCourseVersion(updatedCourse))
        handle.commit()
        return Optional.of(res)
    }

    override fun deleteAllCourses(): Int = courseDAO.deleteAllCourses()

    override fun deleteSpecificCourse(courseId: Int): Int = courseDAO.deleteSpecificCourse(courseId)

    override fun deleteAllStagedCourses(): Int = courseDAO.deleteAllStagedCourses()

    override fun deleteSpecificStagedCourse(stageId: Int): Int = courseDAO.deleteStagedCourse(stageId)

    override fun deleteAllReportsOnCourse(courseId: Int): Int = courseDAO.deleteAllReportsOnCourse(courseId)

    override fun deleteReportOnCourse(courseId: Int, reportId: Int): Int =
            courseDAO.deleteReportOnCourse(reportId)

    override fun voteOnStagedCourse(stageId: Int, inputVote: VoteInputModel): Int =
            courseDAO.voteOnStagedCourse(stageId, Vote.valueOf(inputVote.vote))

    override fun createExamOnCourseInTerm(courseId: Int, termId: Int, inputExam: ExamInputModel): Optional<Exam> {
        handle.begin()
        val exam = examDAO.createExam(courseId, termId, toExam(inputExam)).get()
        examDAO.createVersionExam(toExamVersion(exam))
        handle.commit()
        return Optional.of(exam)
    }

    override fun addReportToExamOnCourseInTerm(examId: Int, inputExamReport: ExamReportInputModel): Optional<ExamReport> =
            examDAO.reportExam(toExamReport(examId, inputExamReport))

    override fun voteOnReportToExamOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int =
            examDAO.voteOnReportToExamOnCourseInTerm(reportId, Vote.valueOf(inputVote.vote))

    override fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int): Optional<Exam> {
        handle.begin()
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId).get()
        val report = examDAO.getSpecificReportOfExam(examId, reportId).get()
        val updatedExam = Exam(
                examId = exam.examId,
                version = exam.version.inc(),
                createdBy = report.reportedBy,
                sheet = report.sheet ?: exam.sheet,
                dueDate = report.dueDate ?: exam.dueDate,
                type = report.type ?: exam.type,
                phase = report.phase ?: exam.phase,
                location = report.location ?: exam.location
        )
        val res = examDAO.updateExam(examId, updatedExam).get()
        examDAO.createVersionExam(toExamVersion(updatedExam))
        examDAO.deleteReportOnExam(examId, reportId)
        handle.commit()
        return Optional.of(res)
    }

    override fun createStagingExam(courseId: Int, termId: Int, inputExam: ExamInputModel): Optional<ExamStage> =
            examDAO.createStagingExam(courseId, termId, toStageExam(inputExam))

    override fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int): Optional<Exam> {
        handle.begin()
        val examStage = examDAO.getExamSpecificStageEntry(stageId).get()
        val newExam = examDAO.createExam(courseId, termId, stagedToExam(examStage)).get()
        examDAO.createVersionExam(toExamVersion(newExam))
        examDAO.deleteStagedExam(stageId)
        handle.commit()
        return Optional.of(newExam)
    }

    override fun voteOnStagedExam(stageId: Int, inputVote: VoteInputModel): Int =
            examDAO.voteOnStagedExam(stageId, Vote.valueOf(inputVote.vote))

    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): Optional<WorkAssignment> {
        handle.begin()
        val workAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(
                courseId,
                termId,
                toWorkAssignment(inputWorkAssignment)
        ).get()
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(workAssignment))
        handle.commit()
        return Optional.of(workAssignment)
    }

    override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel): Optional<WorkAssignmentReport> =
            workAssignmentDAO.addReportToWorkAssignmentOnCourseInTerm(
                    workAssignmentId,
                    toWorkAssignmentReport(workAssignmentId, inputWorkAssignmentReport)
            )

    override fun voteOnReportToWorkAssignmentOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int =
            workAssignmentDAO.voteOnReportToWorkAssignmentOnCourseInTerm(reportId, Vote.valueOf(inputVote.vote))

    override fun voteOnExam(examId: Int, inputVote: VoteInputModel): Int =
            examDAO.voteOnExam(examId, Vote.valueOf(inputVote.vote))

    override fun voteOnWorkAssignment(workAssignmentId: Int, inputVote: VoteInputModel): Int =
            workAssignmentDAO.voteOnWorkAssignment(workAssignmentId, Vote.valueOf(inputVote.vote))

    override fun updateWorkAssignmentBasedOnReport(workAssignmentId: Int, reportId: Int, courseId: Int, termId: Int): Optional<WorkAssignment> {
        handle.begin()
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignment(workAssignmentId, courseId, termId).get()
        val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(workAssignmentId, reportId).get()
        val updatedWorkAssignment = WorkAssignment(
                workAssignmentId = workAssignmentId,
                version = workAssignment.version.inc(),
                createdBy = report.reportedBy,
                sheet = report.sheet ?: workAssignment.sheet,
                supplement = report.supplement ?: workAssignment.supplement,
                dueDate = report.dueDate ?: workAssignment.dueDate,
                individual = report.individual ?: workAssignment.individual,
                lateDelivery = report.lateDelivery ?: workAssignment.lateDelivery,
                multipleDeliveries = report.multipleDeliveries ?: workAssignment.multipleDeliveries,
                requiresReport = report.requiresReport ?: workAssignment.requiresReport
        )
        val res = workAssignmentDAO.updateWorkAssignment(workAssignmentId, updatedWorkAssignment).get()
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(updatedWorkAssignment))
        workAssignmentDAO.deleteReportOnWorkAssignment(reportId)
        handle.commit()
        return Optional.of(res)
    }

    override fun createStagingWorkAssignment(courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): Optional<WorkAssignmentStage> {
        handle.begin()
        val res = workAssignmentDAO.createStagingWorkAssingment(
                courseId,
                termId,
                toStageWorkAssignment(inputWorkAssignment)
        ).get()
        handle.commit()
        return Optional.of(res)
    }

    override fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignment> {
        handle.begin()
        val workAssignmentStage = workAssignmentDAO.getWorkAssignmentSpecificStageEntry(stageId).get()
        val workAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(
                courseId,
                termId,
                stagedToWorkAssignment(workAssignmentStage)
        ).get()
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(workAssignment))
        workAssignmentDAO.deleteSpecificStagedWorkAssignment(stageId)
        handle.commit()
        return Optional.of(workAssignment)
    }

    override fun voteOnStagedWorkAssignment(stageId: Int, inputVote: VoteInputModel): Int =
            workAssignmentDAO.voteOnStagedWorkAssignment(stageId, Vote.valueOf(inputVote.vote))

    override fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion> =
            courseDAO.getAllVersionsOfSpecificCourse(courseId)

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): Optional<CourseVersion> =
            courseDAO.getVersionOfSpecificCourse(courseId, versionId)

    override fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion> =
            examDAO.getAllVersionsOfSpecificExam(examId)

    override fun getVersionOfSpecificExam(examId: Int, versionId: Int): Optional<ExamVersion> =
            examDAO.getVersionOfSpecificExam(examId, versionId)

    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion> =
            workAssignmentDAO.getAllVersionsOfSpecificWorkAssignment(workAssignmentId)

    override fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, versionId: Int): Optional<WorkAssignmentVersion> =
            workAssignmentDAO.getVersionOfSpecificWorkAssignment(workAssignmentId, versionId)

    override fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion> =
            classDAO.getAllVersionsOfSpecificClass(classId)

    override fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion> =
            classDAO.getVersionOfSpecificClass(classId, versionId)

    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            examDAO.deleteAllExamsOfCourseInTerm(courseId, termId)

    override fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int =
            examDAO.deleteSpecificExamOfCourseInTerm(examId)

    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            examDAO.deleteAllStagedExamsOfCourseInTerm(courseId, termId)

    override fun deleteStagedExam(stageId: Int): Int = examDAO.deleteStagedExam(stageId)

    override fun deleteAllReportsOnExam(examId: Int): Int = examDAO.deleteAllReportsOnExam(examId)

    override fun deleteReportOnExam(examId: Int, reportId: Int): Int =
            examDAO.deleteReportOnExam(examId, reportId)

    override fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int =
            workAssignmentDAO.deleteAllWorkAssignmentsOfCourseInTerm(courseId, termId)

    override fun deleteSpecificWorkAssignment(workAssignmentId: Int): Int =
            workAssignmentDAO.deleteSpecificWorkAssignment(workAssignmentId)

    override fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int =
            workAssignmentDAO.deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId, termId)

    override fun deleteSpecificStagedWorkAssignment(stageId: Int): Int =
            workAssignmentDAO.deleteSpecificStagedWorkAssignment(stageId)

    override fun deleteAllReportsOnWorkAssignment(workAssignmentId: Int): Int =
            workAssignmentDAO.deleteAllReportsOnWorkAssignment(workAssignmentId)

    override fun deleteReportOnWorkAssignment(workAssignmentId: Int, reportId: Int): Int =
            workAssignmentDAO.deleteReportOnWorkAssignment(reportId)

    override fun deleteAllVersionsOfCourse(courseId: Int): Int =
            courseDAO.deleteAllVersionsOfCourse(courseId)

    override fun deleteVersionOfCourse(courseId: Int, version: Int): Int =
            courseDAO.deleteVersionOfCourse(courseId, version)

    override fun deleteAllVersionsOfWorkAssignment(workAssignmentId: Int): Int =
            workAssignmentDAO.deleteAllVersionOfWorkAssignments(workAssignmentId)

    override fun deleteVersionOfWorkAssignment(workAssignmentId: Int, version: Int): Int =
            workAssignmentDAO.deleteVersionWorkAssignment(workAssignmentId, version)

    override fun deleteAllVersionsOfExam(examId: Int): Int = examDAO.deleteAllVersionOfExam(examId)

    override fun deleteVersionOfExam(examId: Int, version: Int): Int =
            examDAO.deleteVersionOfExam(examId, version)

}