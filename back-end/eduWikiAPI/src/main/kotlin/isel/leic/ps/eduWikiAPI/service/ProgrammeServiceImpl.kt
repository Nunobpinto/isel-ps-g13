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
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var jdbi: Jdbi

    /**
     * Programme Methods
     */

    override fun getAllProgrammes(): List<Programme> =
            jdbi.withExtension<List<Programme>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getAllProgrammes()
            }

    override fun getSpecificProgramme(programmeId: Int): Optional<Programme> =
            jdbi.withExtension<Optional<Programme>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getSpecificProgramme(programmeId)
            }

    override fun createProgramme(inputProgramme: ProgrammeInputModel): Programme =
            jdbi.inTransaction<Programme, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                val programme = programmeDAO.createProgramme(toProgramme(inputProgramme))
                programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))
                programme
            }

    override fun createStagingProgramme(inputProgramme: ProgrammeInputModel): ProgrammeStage =
            jdbi.withExtension<ProgrammeStage, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.createStagingProgramme(toProgrammeStage(inputProgramme))
            }

    override fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage> =
            jdbi.withExtension<Optional<ProgrammeStage>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getSpecificStageEntryOfProgramme(stageId)
            }

    override fun createProgrammeFromStaged(stageId: Int): Programme =
            jdbi.inTransaction<Programme, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                val programme = stagedToProgramme(programmeDAO.getSpecificStageEntryOfProgramme(stageId).get())
                val createdProgramme = programmeDAO.createProgramme(programme)
                programmeDAO.deleteSpecificStagedProgramme(stageId)
                programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))
                createdProgramme
            }

    override fun getAllProgrammeStageEntries(): List<ProgrammeStage> =
            jdbi.withExtension<List<ProgrammeStage>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getAllProgrammeStageEntries()
            }

    override fun voteOnStagedProgramme(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                var votes = programmeDAO.getVotesOnStagedProgramme(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                programmeDAO.updateVotesOnStagedProgramme(stageId, votes)
            }

    override fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReport> =
            jdbi.withExtension<List<ProgrammeReport>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getAllReportsOfSpecificProgramme(programmeId)
            }

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): Optional<ProgrammeReport> =
            jdbi.withExtension<Optional<ProgrammeReport>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getSpecificReportOfProgramme(programmeId, reportId)
            }

    override fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<Course> =
            jdbi.withExtension<List<Course>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllCoursesOnSpecificProgramme(programmeId)
            }

    override fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): Course =
            jdbi.inTransaction<Course, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val course = toCourseProgramme(inputCourseProgramme)
                val res = courseDAO.addCourseToProgramme(programmeId, course)
                courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(course))
                res
            }

    override fun voteOnProgramme(programmeId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                var votes = programmeDAO.getVotesOnProgramme(programmeId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                programmeDAO.updateVotesOnProgramme(programmeId, votes)
            }

    override fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel): ProgrammeReport =
            jdbi.withExtension<ProgrammeReport, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.reportProgramme(
                        programmeId,
                        toProgrammeReport(programmeId, inputProgrammeReport)
                )
            }

    override fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel): CourseProgrammeReport =
            jdbi.withExtension<CourseProgrammeReport, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.reportSpecificCourseOnProgramme(
                        programmeId,
                        courseId,
                        toCourseProgrammeReport(programmeId, courseId, inputCourseReport)
                )
            }

    override fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                var votes = programmeDAO.getVotesOnReportedProgramme(reportId, programmeId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                programmeDAO.updateVotesOnReportedProgramme(programmeId, reportId, votes)
            }

    override fun updateProgrammeFromReport(programmeId: Int, reportId: Int): Programme =
            jdbi.inTransaction<Programme, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
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
                val res = programmeDAO.updateProgramme(programmeId, updatedProgramme)
                programmeDAO.createProgrammeVersion(toProgrammeVersion(updatedProgramme))
                programmeDAO.deleteSpecificReportOnProgramme(programmeId, reportId)
                res
            }

    override fun deleteAllProgrammes(): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteAllProgrammes()
            }

    override fun deleteSpecificProgramme(programmeId: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteSpecificProgramme(programmeId)
            }

    override fun deleteAllStagedProgrammes(): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteAllStagedProgrammes()
            }

    override fun deleteSpecificStagedProgramme(stageId: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteSpecificStagedProgramme(stageId)
            }

    override fun deleteAllReportsOnProgramme(programmeId: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteAllReportsOnProgramme(programmeId)
            }

    override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteSpecificReportOnProgramme(programmeId, reportId)
            }

    override fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel): Programme =
            jdbi.inTransaction<Programme, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
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
                val res = programmeDAO.updateProgramme(programmeId, updatedProgramme)
                programmeDAO.createProgrammeVersion(toProgrammeVersion(updatedProgramme))
                res
            }

    override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion> =
            jdbi.withExtension<List<ProgrammeVersion>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getAllVersionsOfProgramme(programmeId)
            }

    override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion> =
            jdbi.withExtension<Optional<ProgrammeVersion>, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.getSpecificVersionOfProgramme(programmeId, version)
            }

    override fun deleteAllProgrammeVersions(programmeId: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteAllProgrammeVersions(programmeId)
            }

    override fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteSpecificProgrammeVersion(programmeId, version)
            }

    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<Course> =
            jdbi.withExtension<Optional<Course>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getSpecificCourseOfProgramme(programmeId, courseId)
            }

    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion> =
            jdbi.withExtension<List<CourseProgrammeVersion>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllVersionsOfCourseOnProgramme(programmeId, courseId)
            }

    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion> =
            jdbi.withExtension<Optional<CourseProgrammeVersion>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getSpecificVersionOfCourseOnProgramme(programmeId, courseId, version)
            }

    override fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport> =
            jdbi.withExtension<List<CourseProgrammeReport>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllReportsOfCourseOnProgramme(programmeId, courseId)
            }

    override fun getSpecificReportOfCourseOnProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport> =
            jdbi.withExtension<Optional<CourseProgrammeReport>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
            }

    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnCourseProgramme(programmeId, courseId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnCourseProgramme(programmeId, courseId, votes)
            }

    override fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): CourseProgrammeStage =
            jdbi.withExtension<CourseProgrammeStage, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.createStagingCourseOfProgramme(toCourseProgrammeStage(programmeId, inputCourseProgramme))
            }

    override fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int): Course =
            jdbi.inTransaction<Course, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId).get()
                val courseProgramme = stagedToCourseProgramme(programmeId, courseProgrammeStage)
                val res = courseDAO.addCourseToProgramme(programmeId, courseProgramme)
                courseDAO.deleteStagedCourseProgramme(programmeId)
                courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(courseProgramme))
                res
            }

    override fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnStagedCourseProgramme(programmeId, stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnStagedCourseProgramme(programmeId, stageId, votes)
            }

    override fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int): Course =
            jdbi.inTransaction<Course, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
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
                val res = courseDAO.updateCourseProgramme(programmeId, courseId, updatedCourse)
                courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(updatedCourse))
                courseDAO.deleteReportOnCourseProgramme(programmeId, courseId, reportId)
                res
            }

    override fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnReportedCourseProgramme(programmeId, courseId, reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnReportedCourseProgramme(programmeId, courseId, reportId, votes)
            }

    override fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStage> =
            jdbi.withExtension<List<CourseProgrammeStage>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllCourseStageEntriesOfSpecificProgramme(programmeId)
            }

    override fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage> =
            jdbi.withExtension<Optional<CourseProgrammeStage>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getSpecificStagedCourseProgramme(programmeId, stageId)
            }

    override fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteSpecificCourseProgramme(programmeId, courseId)
            }

    override fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteSpecificVersionOfCourseProgramme(programmeId, courseId, version)
            }

    override fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
            }

    override fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteSpecificStagedCourseProgramme(programmeId, courseId, stageId)
            }

}