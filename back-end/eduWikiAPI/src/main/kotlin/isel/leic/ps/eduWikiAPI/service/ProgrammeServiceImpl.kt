package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.ProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseProgrammeCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ProgrammeCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseProgrammeReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ProgrammeReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseProgrammeStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ProgrammeStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.CourseProgrammeVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ProgrammeVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.CourseProgrammeVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ProgrammeVersionOutputModel
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var jdbi: Jdbi

    /**
     * Programme Methods
     */

    override fun getAllProgrammes(): ProgrammeCollectionOutputModel =
            jdbi.withExtension<ProgrammeCollectionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val programmes = it.getAllProgrammes().map { toProgrammeOutput(it) }
                toProgrammeCollectionOutputModel(programmes)
            }

    override fun getSpecificProgramme(programmeId: Int): ProgrammeOutputModel =
            jdbi.withExtension<ProgrammeOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeOutput(it.getSpecificProgramme(programmeId).orElseThrow {
                    isel.leic.ps.eduWikiAPI.exceptions.NotFoundException(
                            msg = "No Programme Found",
                            action = "Try with other id"
                    )
                })
            }

    override fun createProgramme(inputProgramme: ProgrammeInputModel): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                val programme = programmeDAO.createProgramme(toProgramme(inputProgramme))
                programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))
                toProgrammeOutput(programme)
            }

    override fun createStagingProgramme(inputProgramme: ProgrammeInputModel): ProgrammeStageOutputModel =
            jdbi.withExtension<ProgrammeStageOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeStageOutputModel(it.createStagingProgramme(toProgrammeStage(inputProgramme)))
            }

    override fun getSpecificStageEntryOfProgramme(stageId: Int): ProgrammeStageOutputModel =
            jdbi.withExtension<ProgrammeStageOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeStageOutputModel(
                        it.getSpecificStageEntryOfProgramme(stageId).orElseThrow {
                            isel.leic.ps.eduWikiAPI.exceptions.NotFoundException(
                                    msg = "No Programme Staged Found",
                                    action = "Try with other id"
                            )
                        }
                )
            }

    override fun createProgrammeFromStaged(stageId: Int): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                val programme = stagedToProgramme(programmeDAO.getSpecificStageEntryOfProgramme(stageId).get())
                val createdProgramme = programmeDAO.createProgramme(programme)
                programmeDAO.deleteSpecificStagedProgramme(stageId)
                programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))
                toProgrammeOutput(createdProgramme)
            }

    override fun getAllProgrammeStageEntries(): ProgrammeStageCollectionOutputModel =
            jdbi.withExtension<ProgrammeStageCollectionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val stagedProgrammes = it.getAllProgrammeStageEntries().map { toProgrammeStageOutputModel(it) }
                toProgrammeStageCollectionOutputModel(stagedProgrammes)
            }

    override fun voteOnStagedProgramme(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                var votes = programmeDAO.getVotesOnStagedProgramme(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                programmeDAO.updateVotesOnStagedProgramme(stageId, votes)
            }

    override fun getAllReportsOfSpecificProgramme(programmeId: Int): ProgrammeReportCollectionOutputModel =
            jdbi.withExtension<ProgrammeReportCollectionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val reports = it.getAllReportsOfSpecificProgramme(programmeId).map { toProgrammeReportOutputModel(it) }
                toProgrammeReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReportOutputModel =
            jdbi.withExtension<ProgrammeReportOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeReportOutputModel(it.getSpecificReportOfProgramme(programmeId, reportId).orElseThrow {
                    NotFoundException(
                            msg = "No report found",
                            action = "Try with other id"
                    )
                })
            }

    override fun getAllCoursesOnSpecificProgramme(programmeId: Int): CourseProgrammeCollectionOutputModel =
            jdbi.withExtension<CourseProgrammeCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammes = it.getAllCoursesOnSpecificProgramme(programmeId).map { toCourseProgrammeOutputModel(it) }
                toCourseProgrammeCollectionOutputModel(courseProgrammes)
            }

    override fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): CourseProgrammeOutputModel =
            jdbi.inTransaction<CourseProgrammeOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val course = toCourseProgramme(inputCourseProgramme)
                val res = courseDAO.addCourseToProgramme(programmeId, course)
                courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(course))
                toCourseProgrammeOutputModel(res)
            }

    override fun voteOnProgramme(programmeId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                var votes = programmeDAO.getVotesOnProgramme(programmeId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                programmeDAO.updateVotesOnProgramme(programmeId, votes)
            }

    override fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel): ProgrammeReportOutputModel =
            jdbi.withExtension<ProgrammeReportOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeReportOutputModel(it.reportProgramme(
                        programmeId,
                        toProgrammeReport(programmeId, inputProgrammeReport)
                ))
            }

    override fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel): CourseProgrammeReportOutputModel =
            jdbi.withExtension<CourseProgrammeReportOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeReportOutputModel(it.reportSpecificCourseOnProgramme(
                        programmeId,
                        courseId,
                        toCourseProgrammeReport(programmeId, courseId, inputCourseReport)
                ))
            }

    override fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                var votes = programmeDAO.getVotesOnReportedProgramme(reportId, programmeId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                programmeDAO.updateVotesOnReportedProgramme(programmeId, reportId, votes)
            }

    override fun updateProgrammeFromReport(programmeId: Int, reportId: Int): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
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
                toProgrammeOutput(res)
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

    override fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
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
                toProgrammeOutput(res)
            }

    override fun getAllVersionsOfProgramme(programmeId: Int): ProgrammeVersionCollectionOutputModel =
            jdbi.withExtension<ProgrammeVersionCollectionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val programmeVersions = it.getAllVersionsOfProgramme(programmeId).map { toProgrammeVersionOutputModel(it) }
                toProgrammeVersionCollectionOutputModel(programmeVersions)
            }

    override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): ProgrammeVersionOutputModel =
            jdbi.withExtension<ProgrammeVersionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeVersionOutputModel(
                        it.getSpecificVersionOfProgramme(programmeId, version)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No version found",
                                            action = "Try other version"
                                    )
                                })
            }

    override fun deleteAllProgrammeVersions(programmeId: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteAllProgrammeVersions(programmeId)
            }

    override fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                it.deleteSpecificProgrammeVersion(programmeId, version)
            }

    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): CourseProgrammeOutputModel =
            jdbi.withExtension<CourseProgrammeOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeOutputModel(
                        it.getSpecificCourseOfProgramme(programmeId, courseId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No course with thid id in this programme",
                                            action = "Add course to this programme"
                                    )
                                }
                )
            }

    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): CourseProgrammeVersionCollectionOutputModel =
            jdbi.withExtension<CourseProgrammeVersionCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammeVersions = it.getAllVersionsOfCourseOnProgramme(programmeId, courseId).map { toCourseProgrammeVersionOutput(it) }
                toCourseProgrammeVersionCollectionOutputModel(courseProgrammeVersions)
            }

    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): CourseProgrammeVersionOutputModel =
            jdbi.withExtension<CourseProgrammeVersionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeVersionOutput(
                        it.getSpecificVersionOfCourseOnProgramme(programmeId, courseId, version)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No version of course with thid id in this programme",
                                            action = "Search other version"
                                    )
                                }
                )
            }

    override fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): CourseProgrammeReportCollectionOutputModel =
            jdbi.withExtension<CourseProgrammeReportCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val reports = it.getAllReportsOfCourseOnProgramme(programmeId, courseId)
                        .map { toCourseProgrammeReportOutputModel(it) }
                toCourseProgrammeReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfCourseOnProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReportOutputModel =
            jdbi.withExtension<CourseProgrammeReportOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeReportOutputModel(
                        it.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report of course with thid id in this programme",
                                            action = "Search other report"
                                    )
                                }
                )
            }

    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnCourseProgramme(programmeId, courseId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnCourseProgramme(programmeId, courseId, votes)
            }

    override fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): CourseProgrammeStageOutputModel =
            jdbi.withExtension<CourseProgrammeStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeStageOutputModel(
                        it.createStagingCourseOfProgramme(toCourseProgrammeStage(programmeId, inputCourseProgramme))
                )
            }

    override fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int): CourseProgrammeOutputModel =
            jdbi.inTransaction<CourseProgrammeOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId).get()
                val courseProgramme = stagedToCourseProgramme(programmeId, courseProgrammeStage)
                val res = courseDAO.addCourseToProgramme(programmeId, courseProgramme)
                courseDAO.deleteStagedCourseProgramme(programmeId)
                courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(courseProgramme))
                toCourseProgrammeOutputModel(res)
            }

    override fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnStagedCourseProgramme(programmeId, stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnStagedCourseProgramme(programmeId, stageId, votes)
            }

    override fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeOutputModel =
            jdbi.inTransaction<CourseProgrammeOutputModel, Exception> {
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

                courseDAO.deleteReportOnCourseProgramme(programmeId, courseId, reportId)
                if (report.deleteFlag) {
                    courseDAO.deleteSpecificCourseProgramme(programmeId, courseId)
                    toCourseProgrammeOutputModel(course)
                } else {
                    val res = courseDAO.updateCourseProgramme(programmeId, courseId, updatedCourse)
                    courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(updatedCourse))
                    toCourseProgrammeOutputModel(res)
                }
            }

    override fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnReportedCourseProgramme(programmeId, courseId, reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnReportedCourseProgramme(programmeId, courseId, reportId, votes)
            }

    override fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): CourseProgrammeStageCollectionOutputModel =
            jdbi.withExtension<CourseProgrammeStageCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammeStageEntries = it.getAllCourseStageEntriesOfSpecificProgramme(programmeId).map { toCourseProgrammeStageOutputModel(it) }
                toCourseProgrammeStageCollectionOutputModel(courseProgrammeStageEntries)
            }

    override fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStageOutputModel =
            jdbi.withExtension<CourseProgrammeStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeStageOutputModel(
                        it.getSpecificStagedCourseProgramme(programmeId, stageId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No staged version of course with thid id in this programme",
                                            action = "Search other staged version"
                                    )
                                }
                )
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