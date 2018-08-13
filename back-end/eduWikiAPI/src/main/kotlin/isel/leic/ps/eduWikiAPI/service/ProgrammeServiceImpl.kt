package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.CourseProgramme
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
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_TABLE
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var jdbi: Jdbi
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    // ---------- Programme ----------

    // -------------------------------
    // Programme Methods
    // -------------------------------

    override fun getAllProgrammes(): ProgrammeCollectionOutputModel =
            jdbi.withExtension<ProgrammeCollectionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val programmes = it.getAllProgrammes().map { toProgrammeOutput(it) }
                toProgrammeCollectionOutputModel(programmes)
            }

    override fun getSpecificProgramme(programmeId: Int): ProgrammeOutputModel =
            jdbi.withExtension<ProgrammeOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeOutput(it.getSpecificProgramme(programmeId)
                        .orElseThrow { NotFoundException("No Programme Found", "Try with other id") }
                )
            }

    override fun createProgramme(inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)

                val programme = programmeDAO.createProgramme(toProgramme(inputProgramme, principal.name))
                programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        PROGRAMME_TABLE,
                        programme.logId
                ))
                toProgrammeOutput(programme)
            }

    override fun voteOnProgramme(programmeId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)

                val programme = programmeDAO.getSpecificProgramme(programmeId)
                        .orElseThrow { NotFoundException("No Programme Found", "Try with other id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) programme.votes.dec() else programme.votes.inc()
                val success = programmeDAO.updateVotesOnProgramme(programmeId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        programme.createdBy,
                        PROGRAMME_TABLE,
                        programme.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)

                val programme = programmeDAO.getSpecificProgramme(programmeId)
                        .orElseThrow { NotFoundException("No Programme Found", "Try with other id") }

                val updatedProgramme = Programme(
                        programmeId = programmeId,
                        version = programme.version.inc(),
                        createdBy = principal.name,
                        fullName = if(inputProgramme.fullName.isEmpty()) programme.fullName else inputProgramme.fullName,
                        shortName = if(inputProgramme.shortName.isEmpty()) programme.shortName else inputProgramme.shortName,
                        academicDegree = if(inputProgramme.academicDegree.isEmpty()) programme.academicDegree else inputProgramme.academicDegree,
                        totalCredits = if(inputProgramme.totalCredits == 0) programme.totalCredits else inputProgramme.totalCredits,
                        duration = if(inputProgramme.duration == 0) programme.duration else inputProgramme.duration
                )
                val res = programmeDAO.updateProgramme(programmeId, updatedProgramme)
                programmeDAO.createProgrammeVersion(toProgrammeVersion(updatedProgramme))

                publisher.publishEvent(ResourceUpdatedEvent(
                        principal.name,
                        PROGRAMME_TABLE,
                        programme.logId
                ))
                toProgrammeOutput(res)
            }

    override fun deleteSpecificProgramme(programmeId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val programme = it.getSpecificProgramme(programmeId)
                        .orElseThrow { NotFoundException("No Programme Found", "Try with other id") }
                val success = it.deleteSpecificProgramme(programmeId)

                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        PROGRAMME_TABLE,
                        programme.logId
                ))
                success
            }

    // -------------------------------
    // Programme Stage Methods
    // -------------------------------

    override fun getSpecificStageEntryOfProgramme(stageId: Int): ProgrammeStageOutputModel =
            jdbi.withExtension<ProgrammeStageOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeStageOutputModel(
                        it.getSpecificStageEntryOfProgramme(stageId)
                                .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
                )
            }

    override fun getAllProgrammeStageEntries(): ProgrammeStageCollectionOutputModel =
            jdbi.withExtension<ProgrammeStageCollectionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val stagedProgrammes = it.getAllProgrammeStageEntries().map { toProgrammeStageOutputModel(it) }
                toProgrammeStageCollectionOutputModel(stagedProgrammes)
            }

    override fun voteOnStagedProgramme(stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)

                val programmeStage = programmeDAO.getSpecificStageEntryOfProgramme(stageId)
                        .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) programmeStage.votes.dec() else programmeStage.votes.inc()
                val success = programmeDAO.updateVotesOnStagedProgramme(stageId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        programmeStage.createdBy,
                        PROGRAMME_STAGE_TABLE,
                        programmeStage.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun createStagingProgramme(inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeStageOutputModel =
            jdbi.withExtension<ProgrammeStageOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val programmeStage = it.createStagingProgramme(toProgrammeStage(inputProgramme, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        PROGRAMME_STAGE_TABLE,
                        programmeStage.logId
                ))
                toProgrammeStageOutputModel(programmeStage)
            }

    override fun createProgrammeFromStaged(stageId: Int, principal: Principal): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)

                val programmeStage = programmeDAO.getSpecificStageEntryOfProgramme(stageId)
                        .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
                val createdProgramme = programmeDAO.createProgramme(stagedToProgramme(programmeStage))
                programmeDAO.deleteSpecificStagedProgramme(stageId)
                programmeDAO.createProgrammeVersion(toProgrammeVersion(createdProgramme))

                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        PROGRAMME_STAGE_TABLE,
                        programmeStage.logId,
                        programmeStage.createdBy,
                        ActionType.CREATE,
                        PROGRAMME_TABLE,
                        createdProgramme.logId
                ))
                toProgrammeOutput(createdProgramme)
            }

    override fun deleteSpecificStagedProgramme(stageId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val programme = it.getSpecificStageEntryOfProgramme(stageId)
                        .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
                val success = it.deleteSpecificStagedProgramme(stageId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        programme.createdBy,
                        ActionType.REJECT_STAGE,
                        PROGRAMME_STAGE_TABLE,
                        programme.logId
                ))
                success
            }

    // -------------------------------
    // Programme Report Methods
    // -------------------------------

    override fun getAllReportsOfSpecificProgramme(programmeId: Int): ProgrammeReportCollectionOutputModel =
            jdbi.withExtension<ProgrammeReportCollectionOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val reports = it.getAllReportsOfSpecificProgramme(programmeId).map { toProgrammeReportOutputModel(it) }
                toProgrammeReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReportOutputModel =
            jdbi.withExtension<ProgrammeReportOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                toProgrammeReportOutputModel(it.getSpecificReportOfProgramme(programmeId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try with other id") })
            }

    override fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel, principal: Principal): ProgrammeReportOutputModel =
            jdbi.withExtension<ProgrammeReportOutputModel, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val reportProgramme = it.reportProgramme(programmeId, toProgrammeReport(programmeId, inputProgrammeReport, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        PROGRAMME_REPORT_TABLE,
                        reportProgramme.logId
                ))
                toProgrammeReportOutputModel(reportProgramme)
            }

    override fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                val programmeReport = programmeDAO.getSpecificReportOfProgramme(reportId, programmeId)
                        .orElseThrow { NotFoundException("No report found", "Try with other id") }

                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) programmeReport.votes.dec() else programmeReport.votes.inc()
                val success = programmeDAO.updateVotesOnReportedProgramme(programmeId, reportId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        programmeReport.reportedBy,
                        PROGRAMME_REPORT_TABLE,
                        programmeReport.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun updateProgrammeFromReport(programmeId: Int, reportId: Int, principal: Principal): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)

                val programme = programmeDAO.getSpecificProgramme(programmeId)
                        .orElseThrow { NotFoundException("No programme found", "Try with other id") }
                val report = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try with other id") }

                val res = programmeDAO.updateProgramme(programmeId, Programme(
                        version = programme.version.inc(),
                        createdBy = report.reportedBy,
                        fullName = report.fullName ?: programme.fullName,
                        shortName = report.shortName ?: programme.shortName,
                        academicDegree = report.academicDegree ?: programme.academicDegree,
                        totalCredits = report.totalCredits ?: programme.totalCredits,
                        duration = report.duration ?: programme.duration
                ))
                programmeDAO.createProgrammeVersion(toProgrammeVersion(res))
                programmeDAO.deleteSpecificReportOnProgramme(programmeId, reportId)

                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_REPORT,
                        PROGRAMME_REPORT_TABLE,
                        report.logId,
                        report.reportedBy,
                        ActionType.ALTER,
                        PROGRAMME_TABLE,
                        res.logId
                ))
                toProgrammeOutput(res)
            }

    override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ProgrammeDAOJdbi, Exception>(ProgrammeDAOJdbi::class.java) {
                val programmeReport = it.getSpecificReportOfProgramme(programmeId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try with other id") }

                val success = it.deleteSpecificReportOnProgramme(programmeId, reportId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        programmeReport.reportedBy,
                        ActionType.REJECT_REPORT,
                        PROGRAMME_REPORT_TABLE,
                        programmeReport.logId
                ))
                success
            }

    // -------------------------------
    // Programme Version Methods
    // -------------------------------

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

    // ------ Course Programme -------

    // -------------------------------
    // Course Programme Methods
    // -------------------------------

    override fun getAllCoursesOnSpecificProgramme(programmeId: Int): CourseProgrammeCollectionOutputModel =
            jdbi.withExtension<CourseProgrammeCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) { courseDAO ->
                val courseProgrammes = courseDAO.getAllCoursesOnSpecificProgramme(programmeId)
                toCourseProgrammeCollectionOutputModel(courseProgrammes.map {
                    toCourseProgrammeOutputModel(
                            it,
                            courseDAO.getSpecificCourse(it.courseId)
                                    .orElseThrow { NotFoundException("Course with id ${it.courseId} does not exist", "Try again later") }
                    )
                })
            }

    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): CourseProgrammeOutputModel =
            jdbi.withExtension<CourseProgrammeOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeOutputModel(
                        it.getSpecificCourseOfProgramme(programmeId, courseId)
                                .orElseThrow { NotFoundException("No course with the id in this programme", "Add course to this programme") },
                        it.getSpecificCourse(courseId)
                                .orElseThrow { NotFoundException("No course found", "Try another id") }
                        )
            }

    override fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel, principal: Principal): CourseProgrammeOutputModel =
            jdbi.inTransaction<CourseProgrammeOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val course = courseDAO.getSpecificCourse(inputCourseProgramme.courseId)
                        .orElseThrow { NotFoundException("Course does not exist", "Use a valid course") }

                val newCourseProgramme = courseDAO.addCourseToProgramme(programmeId, toCourseProgramme(inputCourseProgramme, principal.name))
                courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(newCourseProgramme))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_PROGRAMME_TABLE,
                        newCourseProgramme.logId
                ))
                toCourseProgrammeOutputModel(newCourseProgramme, course)
            }

    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val courseProgramme = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)
                        .orElseThrow { NotFoundException("This course in this programme does not exist", "Try again") }

                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseProgramme.votes.dec() else courseProgramme.votes.inc()
                val success = courseDAO.updateVotesOnCourseProgramme(programmeId, courseId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        courseProgramme.createdBy,
                        COURSE_PROGRAMME_TABLE,
                        courseProgramme.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgramme = it.getSpecificCourseOfProgramme(programmeId, courseId)
                        .orElseThrow { NotFoundException("This course in this programme does not exist", "Try again") }

                val success = it.deleteSpecificCourseProgramme(programmeId, courseId)

                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        COURSE_PROGRAMME_TABLE,
                        courseProgramme.logId
                ))
                success
            }

    // -------------------------------
    // Course Programme Stage Methods
    // -------------------------------

    override fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): CourseProgrammeStageCollectionOutputModel =
            jdbi.withExtension<CourseProgrammeStageCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammeStageEntries = it.getAllCourseStageEntriesOfSpecificProgramme(programmeId).map { toCourseProgrammeStageOutputModel(it) }
                toCourseProgrammeStageCollectionOutputModel(courseProgrammeStageEntries)
            }

    override fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStageOutputModel =
            jdbi.withExtension<CourseProgrammeStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeStageOutputModel(
                        it.getSpecificStagedCourseProgramme(programmeId, stageId)
                                .orElseThrow { NotFoundException("No staged version of course with this id in this programme", "Search other staged version") }
                )
            }

    override fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel, principal: Principal): CourseProgrammeStageOutputModel =
            jdbi.withExtension<CourseProgrammeStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammeStage = it.createStagingCourseOfProgramme(toCourseProgrammeStage(programmeId, inputCourseProgramme, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_PROGRAMME_STAGE_TABLE,
                        courseProgrammeStage.logId
                ))
                toCourseProgrammeStageOutputModel(courseProgrammeStage)
            }

    override fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int, principal: Principal): CourseProgrammeOutputModel =
            jdbi.inTransaction<CourseProgrammeOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId)
                        .orElseThrow { NotFoundException("No staged version of course with this id in this programme", "Search other staged version") }

                val courseProgramme = courseDAO.addCourseToProgramme(programmeId, stagedToCourseProgramme(programmeId, courseProgrammeStage))
                courseDAO.deleteStagedCourseProgramme(stageId)
                courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(courseProgramme))
                val course = courseDAO.getSpecificCourse(courseProgramme.courseId)
                        .orElseThrow { NotFoundException("The course id in this stage is not valid", "Contact admins") }

                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        COURSE_PROGRAMME_STAGE_TABLE,
                        courseProgrammeStage.logId,
                        courseProgrammeStage.createdBy,
                        ActionType.CREATE,
                        COURSE_PROGRAMME_TABLE,
                        courseProgramme.logId
                ))
                toCourseProgrammeOutputModel(courseProgramme, course)
            }

    override fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId)
                        .orElseThrow { NotFoundException("Either the stage or programme id are not valid", "Try other ids") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseProgrammeStage.votes.dec() else courseProgrammeStage.votes.inc()
                val success = courseDAO.updateVotesOnStagedCourseProgramme(programmeId, stageId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        courseProgrammeStage.createdBy,
                        COURSE_PROGRAMME_STAGE_TABLE,
                        courseProgrammeStage.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificStagedCourseProgramme(programmeId: Int, stageId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammeStage = it.getSpecificStagedCourseProgramme(programmeId, stageId)
                        .orElseThrow { NotFoundException("Either the stage or programme id are not valid", "Try other ids") }

                val success = it.deleteSpecificStagedCourseProgramme(programmeId, stageId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        courseProgrammeStage.createdBy,
                        ActionType.REJECT_STAGE,
                        COURSE_PROGRAMME_STAGE_TABLE,
                        courseProgrammeStage.logId
                ))
                success
            }

    // -------------------------------
    // Course Programme Report Methods
    // -------------------------------

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
                                .orElseThrow { NotFoundException("No report of course with this id in this programme", "Search other report") }
                )
            }

    override fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel, principal: Principal): CourseProgrammeReportOutputModel =
            jdbi.withExtension<CourseProgrammeReportOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammeReport = it.reportSpecificCourseOnProgramme(programmeId, courseId, toCourseProgrammeReport(programmeId, courseId, inputCourseReport, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_PROGRAMME_REPORT_TABLE,
                        courseProgrammeReport.logId
                ))
                toCourseProgrammeReportOutputModel(courseProgrammeReport)
            }

    override fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int, principal: Principal): CourseProgrammeOutputModel =
            jdbi.inTransaction<CourseProgrammeOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val course = courseDAO.getSpecificCourse(courseId)
                        .orElseThrow { NotFoundException("The course is not valid", "Contact admins") }
                val courseProgramme = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)
                        .orElseThrow { NotFoundException("Can't specified course in programme", "Try other ids") }
                val report = courseDAO.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
                        .orElseThrow { NotFoundException("Can't find the specified report for this course in programme", "Search other report") }


                val updatedCourseProgramme = CourseProgramme(
                        programmeId = programmeId,
                        courseId = courseProgramme.courseId,
                        version = courseProgramme.version.inc(),
                        createdBy = report.reportedBy,
                        lecturedTerm = report.lecturedTerm ?: courseProgramme.lecturedTerm,
                        optional = report.optional ?: courseProgramme.optional,
                        credits = report.credits ?: courseProgramme.credits
                )

                courseDAO.deleteReportOnCourseProgramme(programmeId, courseId, reportId)
                val action: ActionType
                val toRet = if(report.deleteFlag) {
                    courseDAO.deleteSpecificCourseProgramme(programmeId, courseId)
                    action = ActionType.DELETE
                    toCourseProgrammeOutputModel(courseProgramme, course)
                } else {
                    val res = courseDAO.updateCourseProgramme(programmeId, courseId, updatedCourseProgramme)
                    courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(updatedCourseProgramme))
                    action = ActionType.ALTER
                    toCourseProgrammeOutputModel(res, course)
                }
                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        COURSE_PROGRAMME_REPORT_TABLE,
                        report.logId,
                        report.reportedBy,
                        action,
                        COURSE_PROGRAMME_TABLE,
                        courseProgramme.logId
                ))
                toRet
            }

    override fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val courseProgrammeReport = courseDAO.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
                        .orElseThrow { NotFoundException("Can't find the specified report for this course in programme", "Search other report") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseProgrammeReport.votes.dec() else courseProgrammeReport.votes.inc()
                val success = courseDAO.updateVotesOnReportedCourseProgramme(programmeId, courseId, reportId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        courseProgrammeReport.reportedBy,
                        PROGRAMME_REPORT_TABLE,
                        courseProgrammeReport.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }


    override fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
            }

    // -------------------------------
    // Course Programme Version Methods
    // -------------------------------

    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): CourseProgrammeVersionCollectionOutputModel =
            jdbi.withExtension<CourseProgrammeVersionCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseProgrammeVersions = it.getAllVersionsOfCourseOnProgramme(programmeId, courseId).map { toCourseProgrammeVersionOutput(it) }
                toCourseProgrammeVersionCollectionOutputModel(courseProgrammeVersions)
            }

    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): CourseProgrammeVersionOutputModel =
            jdbi.withExtension<CourseProgrammeVersionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseProgrammeVersionOutput(
                        it.getSpecificVersionOfCourseOnProgramme(programmeId, courseId, version)
                                .orElseThrow { NotFoundException("No version of course with this id in this programme", "Search other version") }
                )
            }


}