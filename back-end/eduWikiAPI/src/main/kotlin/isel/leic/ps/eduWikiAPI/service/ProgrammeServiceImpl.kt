package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var programmeDAO: ProgrammeDAO
    @Autowired
    lateinit var courseDAO: CourseDAO
    @Autowired
    lateinit var handle: Handle

    override fun getAllProgrammes(): List<Programme> = programmeDAO.getAllProgrammes()

    override fun getSpecificProgramme(programmeId: Int): Optional<Programme> =
            programmeDAO.getSpecificProgramme(programmeId)

    override fun createProgramme(inputProgramme: ProgrammeInputModel): Optional<Programme> {
        val programme = programmeDAO.createProgramme(toProgramme(inputProgramme)).get()
        programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))
        return Optional.of(programme)
    }

    override fun createStagingProgramme(inputProgramme: ProgrammeInputModel): Optional<ProgrammeStage> =
            programmeDAO.createStagingProgramme(toProgrammeStage(inputProgramme))

    override fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage> =
            programmeDAO.getSpecificStageEntryOfProgramme(stageId)

    override fun createProgrammeFromStaged(stageId: Int): Optional<Programme> {
        val programme = stagedToProgramme(programmeDAO.getSpecificStageEntryOfProgramme(stageId).get())
        val createdProgramme = programmeDAO.createProgramme(programme).get()
        programmeDAO.deleteSpecificStagedProgramme(stageId)
        programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))
        return Optional.of(createdProgramme)
    }

    override fun getAllProgrammeStageEntries(): List<ProgrammeStage> =
            programmeDAO.getAllProgrammeStageEntries()

    override fun voteOnStagedProgramme(stageId: Int, inputVote: VoteInputModel): Int =
            programmeDAO.voteOnStagedProgramme(stageId, Vote.valueOf(inputVote.vote))

    override fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReport> =
            programmeDAO.getAllReportsOfSpecificProgramme(programmeId)

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): Optional<ProgrammeReport> =
            programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)

    override fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<Course> =
            courseDAO.getAllCoursesOnSpecificProgramme(programmeId)

    override fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): Optional<Course> {
        handle.begin() //TODO
        val course = toCourseProgramme(inputCourseProgramme)
        val res = courseDAO.addCourseToProgramme(programmeId, course).get()
        courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(course))
        handle.commit()
        return Optional.of(res)
    }

    override fun voteOnProgramme(programmeId: Int, inputVote: VoteInputModel): Int =
            programmeDAO.voteOnProgramme(programmeId, Vote.valueOf(inputVote.vote))

    override fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel): Optional<ProgrammeReport> =
            programmeDAO.reportProgramme(
                    programmeId,
                    toProgrammeReport(programmeId, inputProgrammeReport)
            )

    override fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel): Optional<CourseProgrammeReport> =
            courseDAO.reportSpecificCourseOnProgramme(
                    programmeId,
                    courseId,
                    toCourseProgrammeReport(programmeId, courseId, inputCourseReport)
            )

    override fun voteOnReportedProgramme(programmeId: Int, reportId: Int, inputVote: VoteInputModel): Int =
            programmeDAO.voteOnReportedProgramme(programmeId, reportId, Vote.valueOf(inputVote.vote))

    override fun updateProgrammeFromReport(programmeId: Int, reportId: Int): Optional<Programme> {
        val programme = programmeDAO.getSpecificProgramme(programmeId).get()
        val report = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId).get()
        val updatedProgramme = Programme(
                programmeId = programme.programmeId,
                version = programme.version.inc(),
                createdBy = report.reportedBy,
                fullName = report.fullName ?: programme.fullName,
                shortName = report.shortName ?: programme.shortName,
                academicDegree = report.academicDegree ?: programme.academicDegree,
                totalCredits = report.totalCredits ?: programme.totalCredits,
                duration = report.duration ?: programme.duration
        )
        val res = programmeDAO.updateProgramme(programmeId, updatedProgramme).get()
        programmeDAO.createProgrammeVersion(toProgrammeVersion(updatedProgramme))
        programmeDAO.deleteSpecificReportOnProgramme(programmeId, reportId)
        return Optional.of(res)
    }

    override fun deleteAllProgrammes(): Int = programmeDAO.deleteAllProgrammes()

    override fun deleteSpecificProgramme(programmeId: Int): Int =
            programmeDAO.deleteSpecificProgramme(programmeId)

    override fun deleteAllStagedProgrammes(): Int =
            programmeDAO.deleteAllStagedProgrammes()

    override fun deleteSpecificStagedProgramme(stageId: Int) = programmeDAO.deleteSpecificStagedProgramme(stageId)

    override fun deleteAllReportsOnProgramme(programmeId: Int): Int =
            programmeDAO.deleteAllReportsOnProgramme(programmeId)

    override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int =
            programmeDAO.deleteSpecificReportOnProgramme(programmeId, reportId)

    override fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel): Optional<Programme> {
        val programme = programmeDAO.getSpecificProgramme(programmeId).get()
        val updatedProgramme = Programme(
                programmeId = programmeId,
                version = programme.version.inc(),
                createdBy = inputProgramme.createdBy,
                fullName = inputProgramme.fullName,
                shortName = inputProgramme.shortName,
                academicDegree = inputProgramme.academicDegree,
                totalCredits = inputProgramme.totalCredits,
                duration = inputProgramme.duration
        )
        val res = programmeDAO.updateProgramme(programmeId, updatedProgramme).get()
        programmeDAO.createProgrammeVersion(toProgrammeVersion(updatedProgramme))
        return Optional.of(res)
    }

    override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion> =
            programmeDAO.getAllVersionsOfProgramme(programmeId)

    override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion> =
            programmeDAO.getSpecificVersionOfProgramme(programmeId, version)

    override fun deleteAllProgrammeVersions(programmeId: Int): Int =
            programmeDAO.deleteAllProgrammeVersions(programmeId)

    override fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int =
            programmeDAO.deleteSpecificProgrammeVersion(programmeId, version)

    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<Course> =
            courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)

    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion> =
            courseDAO.getAllVersionsOfCourseOnProgramme(programmeId, courseId)

    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion> =
            courseDAO.getSpecificVersionOfCourseOnProgramme(programmeId, courseId, version)

    override fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport> =
            courseDAO.getAllReportsOfCourseOnProgramme(programmeId, courseId)

    override fun getSpecificReportOfCourseOnProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport> =
            courseDAO.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)

    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int, inputVote: VoteInputModel): Int =
            courseDAO.voteOnCourseProgramme(programmeId, courseId, Vote.valueOf(inputVote.vote))

    override fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): Optional<CourseProgrammeStage> =
            courseDAO.createStagingCourseOfProgramme(toCourseProgrammeStage(programmeId, inputCourseProgramme))

    override fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int): Optional<Course> {
        val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId).get()
        val courseProgramme = stagedToCourseProgramme(programmeId, courseProgrammeStage)
        val res = courseDAO.addCourseToProgramme(programmeId, courseProgramme).get()
        courseDAO.deleteStagedCourseProgramme(programmeId)
        courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(courseProgramme))
        return Optional.of(res)
    }

    override fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, inputVote: VoteInputModel): Int =
            courseDAO.voteOnStagedCourseProgramme(programmeId, stageId, Vote.valueOf(inputVote.vote))

    override fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int): Optional<Course> {
        val course = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId).get()
        val report = courseDAO.getSpecificReportOfCourseProgramme(
                programmeId,
                courseId,
                reportId
        ).get()
        val updatedCourse = Course(
                courseId = course.courseId,
                programmeId = programmeId,
                version = course.version.inc(),
                createdBy = report.reportedBy,
                lecturedTerm = report.lecturedTerm ?: course.lecturedTerm,
                optional = report.optional ?: course.optional,
                credits = report.credits ?: course.credits
        )
        val res = courseDAO.updateCourseProgramme(programmeId, courseId, updatedCourse).get()
        courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(updatedCourse))
        courseDAO.deleteReportOnCourseProgramme(programmeId, courseId, reportId)
        return Optional.of(res)
    }

    override fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, inputVote: VoteInputModel): Int =
            courseDAO.voteOnReportOfCourseProgramme(programmeId, courseId, reportId, Vote.valueOf(inputVote.vote))

    override fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStage> =
            courseDAO.getAllCourseStageEntriesOfSpecificProgramme(programmeId)

    override fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage> =
            courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId)

    override fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int =
            courseDAO.deleteSpecificCourseProgramme(programmeId, courseId)

    override fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int): Int =
            courseDAO.deleteSpecificVersionOfCourseProgramme(programmeId, courseId, version)

    override fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int =
            courseDAO.deleteSpecificReportOfCourseProgramme(programmeId, courseId, reportId)

    override fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int): Int =
            courseDAO.deleteSpecificStagedCourseProgramme(programmeId, courseId, stageId)

}