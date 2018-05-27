package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var programmeDAO: ProgrammeDAO

    @Autowired
    lateinit var courseDAO: CourseDAO

    override fun getAllProgrammes(): List<Programme> = programmeDAO.getAllProgrammes()


    override fun getSpecificProgramme(programmeId: Int): Programme = programmeDAO.getSpecificProgramme(programmeId)

    override fun createProgramme(inputProgramme: ProgrammeInputModel) {
        val programme = Programme(
                createdBy = inputProgramme.createdBy!!,
                fullName = inputProgramme.fullName!!,
                shortName = inputProgramme.shortName!!,
                academicDegree = inputProgramme.academicDegree!!,
                totalCredits = inputProgramme.totalCredits!!,
                duration = inputProgramme.duration!!,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        programmeDAO.createProgramme(programme)
        programmeDAO.addToProgrammeVersion(programme)
    }

    override fun createStagingProgramme(inputProgramme: ProgrammeInputModel) {
        val stage = ProgrammeStage(
                createdBy = inputProgramme.createdBy!!,
                fullName = inputProgramme.fullName!!,
                shortName = inputProgramme.shortName!!,
                academicDegree = inputProgramme.academicDegree!!,
                totalCredits = inputProgramme.totalCredits!!,
                duration = inputProgramme.duration!!,
                timestamp = Timestamp.valueOf(LocalDateTime.now())

        )
        programmeDAO.createStagingProgramme(stage)
    }

    override fun getSpecificStagedProgramme(stageId: Int) : ProgrammeStage = programmeDAO.getSpecificProgrammeStage(stageId)

    override fun createProgrammeFromStaged(stageId: Int) {
        val programmeStage = programmeDAO.getSpecificProgrammeStage(stageId)
        val programme = Programme(
                createdBy = programmeStage.createdBy,
                fullName = programmeStage.fullName,
                shortName = programmeStage.shortName,
                academicDegree = programmeStage.academicDegree,
                totalCredits = programmeStage.totalCredits,
                duration = programmeStage.duration,
                timestamp = Timestamp.valueOf(LocalDateTime.now())

        )
        programmeDAO.deleteStagedProgramme(stageId)
        programmeDAO.createProgramme(programme)
        programmeDAO.addToProgrammeVersion(programme)
    }

    override fun getStagedProgrammes(): List<ProgrammeStage> = programmeDAO.getAllProgrammeStages()

    override fun voteOnStagedProgramme(stageId: Int, inputVote: VoteInputModel) = programmeDAO.voteOnStagedProgramme(stageId, inputVote)

    override fun getAllReportsOfProgramme(programmeId: Int): List<ProgrammeReport> = programmeDAO.getAllReportsOfProgramme(programmeId)

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReport = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)

    override fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course> = courseDAO.getCoursesOnSpecificProgramme(programmeId)

    override fun addCourseToProgramme(programmeId: Int, input: CourseProgrammeInputModel) : Int{
        val course = Course (
                id = input.courseId,
                lecturedTerm = input.lecturedTerm,
                programmeId = input.programmeId,
                credits = input.credits,
                optional = input.optional,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        courseDAO.addCourseToProgramme(programmeId, course)
        return courseDAO.addCourseProgrammeToVersion(course)
    }

    override fun voteOnProgramme(programmeId: Int, inputVote: VoteInputModel) = programmeDAO.voteOnProgramme(programmeId, inputVote)

    override fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel) {
        val programmeReport = ProgrammeReport(
                programmeId = programmeId,
                programmeFullName = inputProgrammeReport.fullName,
                programmeShortName = inputProgrammeReport.shortName,
                programmeAcademicDegree = inputProgrammeReport.academicDegree,
                programmeDuration = inputProgrammeReport.duration,
                programmeTotalCredits = inputProgrammeReport.totalCredits,
                reportedBy = inputProgrammeReport.reportedBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        programmeDAO.reportProgramme(programmeId, programmeReport)
    }

    override fun reportCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel): Int {
        val courseProgrammeReport = CourseProgrammeReport(
                optional = inputCourseReport.optional,
                credits = inputCourseReport.credits,
                programmeId = programmeId,
                id = courseId,
                reportedBy = inputCourseReport.reportedBy
        )
        return courseDAO.reportCourseOnProgramme(programmeId, courseId, courseProgrammeReport)
    }

    override fun voteOnReportedProgramme(reportId: Int, inputVote: VoteInputModel) = programmeDAO.voteOnReportedProgramme(reportId, inputVote)

    override fun updateReportedProgramme(programmeId: Int, reportId: Int) {
        val programme = programmeDAO.getSpecificProgramme(programmeId)
        val report = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)
        val updatedProgramme = Programme(
                id = programme.id,
                version = programme.version.inc(),
                votes = programme.votes,
                createdBy = programme.createdBy,
                fullName = report.programmeFullName ?: programme.fullName,
                shortName =report.programmeShortName ?: programme.shortName,
                academicDegree = report.programmeAcademicDegree ?: programme.academicDegree,
                totalCredits = if(report.programmeTotalCredits != 0) report.programmeTotalCredits!! else programme.totalCredits,
                duration = if(report.programmeDuration!=0) report.programmeDuration!! else programme.duration,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        programmeDAO.updateProgramme(programmeId, updatedProgramme)
        programmeDAO.addToProgrammeVersion(updatedProgramme)
        programmeDAO.deleteReportOnProgramme(programmeId,reportId)
    }

    override fun deleteAllProgrammes() = programmeDAO.deleteAllProgrammes()

    override fun deleteSpecificProgramme(programmeId: Int): Int = programmeDAO.deleteSpecificProgramme(programmeId)

    override fun deleteAllStagedProgrammes() {
        return programmeDAO.deleteAllStagedProgrammes()
    }

    override fun deleteStagedProgramme(stageId: Int) = programmeDAO.deleteStagedProgramme(stageId)

    override fun deleteAllReportsOnProgramme(programmeId: Int) = programmeDAO.deleteAllReportsOnProgramme(programmeId)

    override fun deleteReportOnProgramme(programmeId: Int, reportId: Int) = programmeDAO.deleteReportOnProgramme(programmeId, reportId)

    override fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel) {
        val programme = programmeDAO.getSpecificProgramme(programmeId)
        val updatedProgramme = Programme(
                id = programmeId,
                version = inputProgramme.version.inc(),
                createdBy = inputProgramme.createdBy!!,
                fullName = inputProgramme.fullName ?: programme.fullName,
                shortName = inputProgramme.shortName ?: programme.shortName,
                academicDegree = inputProgramme.academicDegree ?: programme.academicDegree,
                totalCredits = inputProgramme.totalCredits ?: programme.totalCredits,
                duration = inputProgramme.duration ?: programme.duration
        )
        programmeDAO.updateProgramme(programmeId, updatedProgramme)
        programmeDAO.addToProgrammeVersion(updatedProgramme)
    }

    override fun getAllVersions(programmeId: Int): List<ProgrammeVersion> = programmeDAO.getAllVersionsOfProgramme(programmeId)

    override fun getVersion(programmeId: Int, versionId: Int): ProgrammeVersion = programmeDAO.getSpecificVersionOfProgramme(programmeId, versionId)

    override fun deleteAllVersions(programmeId: Int): Int = programmeDAO.deleteAllVersionsOfProgramme(programmeId)

    override fun deleteSpecificVersion(programmeId: Int, versionId: Int): Int = programmeDAO.deleteVersionProgramme(programmeId, versionId)

    override fun getSpecificCourseOnSpecificProgramme(programmeId: Int, courseId: Int): Course
            = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)

    override fun getAllVersionsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseVersion>
            = courseDAO.getAllVersionsOfCourseOnSpecificProgramme(programmeId, courseId)


    override fun getSpecificVersionOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, versionId: Int): CourseVersion
            = courseDAO.getSpecificVersionOfCourseOnSpecificProgramme (programmeId, courseId, versionId)

    override fun getAllReportsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport> = courseDAO.getAllReportsOfCourseOnSpecificProgramme(programmeId, courseId)

    override fun getSpecificReportOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReport = courseDAO.getSpecificReportOfCourseOnSpecificProgramme(programmeId, courseId, reportId)

    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int,inputVote: VoteInputModel): Int
            = courseDAO.voteOnCourseOfProgramme(programmeId, Vote.valueOf(inputVote.vote), courseId)

    override fun createStagedCourseOfProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): Int {
        val courseProgrammeStage = CourseProgrammeStage(
                courseId = inputCourseProgramme.courseId,
                programmeId = programmeId,
                credits = inputCourseProgramme.credits,
                optional = inputCourseProgramme.optional,
                lecturedTerm = inputCourseProgramme.lecturedTerm,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return courseDAO.createStagingCourseOfProgramme(courseProgrammeStage)
    }

    override fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int): Int {
        val courseProgrammeStage = courseDAO.getSpecificStagedCourseOfProgramme(programmeId, stageId)
        val courseProgramme = Course(
                createdBy = courseProgrammeStage.createdBy,
                programmeId = programmeId,
                lecturedTerm = courseProgrammeStage.lecturedTerm,
                optional = courseProgrammeStage.optional,
                credits = courseProgrammeStage.credits,
                timestamp = Timestamp.valueOf(LocalDateTime.now())

        )
        courseDAO.deleteStagedCourseOfProgramme(programmeId,stageId)
        courseDAO.addCourseToProgramme(programmeId,courseProgramme)
        return courseDAO.addCourseProgrammeToVersion(courseProgramme)
    }

    override fun voteOnCourseProgrammeStaged(programmeId: Int, stageId: Int, inputVote: VoteInputModel): Int = courseDAO.voteOnCourseProgrammeStaged(programmeId, stageId, Vote.valueOf(inputVote.vote))

    override fun updateReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int {
        val course = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)
        val report = courseDAO.getSpecificReportOfCourseOnSpecificProgramme(programmeId,courseId, reportId)
        val updatedCourse = Course(
                id = course.id,
                version = course.version.inc(),
                votes = course.votes,
                createdBy = course.createdBy,
                lecturedTerm = report.lecturedTerm ?: course.lecturedTerm,
                optional = report.optional ?: course.optional,
                credits = report.credits ?: course.credits,
                programmeId = programmeId,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        courseDAO.updateCourseProgramme(programmeId, courseId, updatedCourse)
        courseDAO.addCourseProgrammeToVersion(updatedCourse)
        return courseDAO.deleteReportOnCourseProgramme(programmeId, courseId, reportId)
    }

    override fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, inputVote: VoteInputModel): Int = courseDAO.voteOnReportOfCourseProgramme(programmeId, reportId, Vote.valueOf(inputVote.vote))

    override fun getStagedCoursesOfProgramme(programmeId: Int): List<CourseProgrammeStage> = courseDAO.getStagedCoursesOfProgramme(programmeId)

    override fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStage = courseDAO.getSpecificStagedCourseOfProgramme(programmeId, stageId)



}