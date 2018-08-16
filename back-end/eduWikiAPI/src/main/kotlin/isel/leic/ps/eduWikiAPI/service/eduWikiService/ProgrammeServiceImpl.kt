package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.CourseProgramme
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ProgrammeOutputModel
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
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TABLE
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.ProgrammeService
import isel.leic.ps.eduWikiAPI.utils.resolveApproval
import isel.leic.ps.eduWikiAPI.utils.resolveVote
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var programmeDAO: ProgrammeDAO
    @Autowired
    lateinit var courseDAO: CourseDAO
    @Autowired
    lateinit var reputationDAO: ReputationDAO
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    // ---------- Programme ----------

    // -------------------------------
    // Programme Methods
    // -------------------------------

    @Transactional
    override fun getAllProgrammes(): ProgrammeCollectionOutputModel {
        val programmes = programmeDAO.getAllProgrammes().map { toProgrammeOutput(it) }
        return toProgrammeCollectionOutputModel(programmes)
    }

    @Transactional
    override fun getSpecificProgramme(programmeId: Int): ProgrammeOutputModel {
        return toProgrammeOutput(programmeDAO.getSpecificProgramme(programmeId)
                .orElseThrow { NotFoundException("No Programme Found", "Try with other id") }
        )
    }

    @Transactional
    override fun createProgramme(inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeOutputModel {
        val programme = programmeDAO.createProgramme(toProgramme(inputProgramme, principal.name))
        programmeDAO.createProgrammeVersion(toProgrammeVersion(programme))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                PROGRAMME_TABLE,
                programme.logId
        ))
        return toProgrammeOutput(programme)
    }

    @Transactional
    override fun voteOnProgramme(programmeId: Int, vote: VoteInputModel, principal: Principal): Int {
        val programme = programmeDAO.getSpecificProgramme(programmeId)
                .orElseThrow { NotFoundException("No Programme Found", "Try with other id") }
        resolveVote(principal.name, programme.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, PROGRAMME_TABLE, programme.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) programme.votes.dec() else programme.votes.inc()
        val success = programmeDAO.updateVotesOnProgramme(programmeId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                programme.createdBy,
                PROGRAMME_TABLE,
                programme.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeOutputModel {
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
        return toProgrammeOutput(res)
    }

    @Transactional
    override fun deleteSpecificProgramme(programmeId: Int, principal: Principal): Int {
        val programme = programmeDAO.getSpecificProgramme(programmeId)
                .orElseThrow { NotFoundException("No Programme Found", "Try with other id") }
        val success = programmeDAO.deleteSpecificProgramme(programmeId)

        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                PROGRAMME_TABLE,
                programme.logId
        ))
        return success
    }

    // -------------------------------
    // Programme Stage Methods
    // -------------------------------

    @Transactional
    override fun getSpecificStageEntryOfProgramme(stageId: Int): ProgrammeStageOutputModel {
        return toProgrammeStageOutputModel(
                programmeDAO.getSpecificStageEntryOfProgramme(stageId)
                        .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
        )
    }

    @Transactional
    override fun getAllProgrammeStageEntries(): ProgrammeStageCollectionOutputModel {
        val stagedProgrammes = programmeDAO.getAllProgrammeStageEntries().map { toProgrammeStageOutputModel(it) }
        return toProgrammeStageCollectionOutputModel(stagedProgrammes)
    }

    @Transactional
    override fun voteOnStagedProgramme(stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val programmeStage = programmeDAO.getSpecificStageEntryOfProgramme(stageId)
                .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
        resolveVote(principal.name, programmeStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, PROGRAMME_STAGE_TABLE, programmeStage.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) programmeStage.votes.dec() else programmeStage.votes.inc()
        val success = programmeDAO.updateVotesOnStagedProgramme(stageId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                programmeStage.createdBy,
                PROGRAMME_STAGE_TABLE,
                programmeStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun createStagingProgramme(inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeStageOutputModel {
        val programmeStage = programmeDAO.createStagingProgramme(toProgrammeStage(inputProgramme, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                PROGRAMME_STAGE_TABLE,
                programmeStage.logId
        ))
        return toProgrammeStageOutputModel(programmeStage)
    }

    @Transactional
    override fun createProgrammeFromStaged(stageId: Int, principal: Principal): ProgrammeOutputModel {
        val programmeStage = programmeDAO.getSpecificStageEntryOfProgramme(stageId)
                .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
        resolveApproval(principal.name, programmeStage.createdBy)

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
        return toProgrammeOutput(createdProgramme)
    }

    @Transactional
    override fun deleteSpecificStagedProgramme(stageId: Int, principal: Principal): Int {
        val programme = programmeDAO.getSpecificStageEntryOfProgramme(stageId)
                .orElseThrow { NotFoundException("No Programme Staged Found", "Try with other id") }
        val success = programmeDAO.deleteSpecificStagedProgramme(stageId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                programme.createdBy,
                ActionType.REJECT_STAGE,
                PROGRAMME_STAGE_TABLE,
                programme.logId
        ))
        return success
    }

    // -------------------------------
    // Programme Report Methods
    // -------------------------------

    @Transactional
    override fun getAllReportsOfSpecificProgramme(programmeId: Int): ProgrammeReportCollectionOutputModel {
        val reports = programmeDAO.getAllReportsOfSpecificProgramme(programmeId).map { toProgrammeReportOutputModel(it) }
        return toProgrammeReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReportOutputModel {
        return toProgrammeReportOutputModel(programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try with other id") })
    }

    @Transactional
    override fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel, principal: Principal): ProgrammeReportOutputModel {
        val reportProgramme = programmeDAO.reportProgramme(programmeId, toProgrammeReport(programmeId, inputProgrammeReport, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                PROGRAMME_REPORT_TABLE,
                reportProgramme.logId
        ))
        return toProgrammeReportOutputModel(reportProgramme)
    }

    @Transactional
    override fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val programmeReport = programmeDAO.getSpecificReportOfProgramme(reportId, programmeId)
                .orElseThrow { NotFoundException("No report found", "Try with other id") }
        resolveVote(principal.name, programmeReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, PROGRAMME_REPORT_TABLE, programmeReport.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) programmeReport.votes.dec() else programmeReport.votes.inc()
        val success = programmeDAO.updateVotesOnReportedProgramme(programmeId, reportId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                programmeReport.reportedBy,
                PROGRAMME_REPORT_TABLE,
                programmeReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun updateProgrammeFromReport(programmeId: Int, reportId: Int, principal: Principal): ProgrammeOutputModel {
        val programme = programmeDAO.getSpecificProgramme(programmeId)
                .orElseThrow { NotFoundException("No programme found", "Try with other id") }
        val report = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try with other id") }
        resolveApproval(principal.name, report.reportedBy)

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
        return toProgrammeOutput(res)
    }

    @Transactional
    override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int, principal: Principal): Int {
        val programmeReport = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try with other id") }

        val success = programmeDAO.deleteSpecificReportOnProgramme(programmeId, reportId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                programmeReport.reportedBy,
                ActionType.REJECT_REPORT,
                PROGRAMME_REPORT_TABLE,
                programmeReport.logId
        ))
        return success
    }

    // -------------------------------
    // Programme Version Methods
    // -------------------------------

    @Transactional
    override fun getAllVersionsOfProgramme(programmeId: Int): ProgrammeVersionCollectionOutputModel {
        val programmeVersions = programmeDAO.getAllVersionsOfProgramme(programmeId).map { toProgrammeVersionOutputModel(it) }
        return toProgrammeVersionCollectionOutputModel(programmeVersions)
    }

    @Transactional
    override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): ProgrammeVersionOutputModel {
        return toProgrammeVersionOutputModel(
                programmeDAO.getSpecificVersionOfProgramme(programmeId, version)
                        .orElseThrow {
                            NotFoundException(
                                    title = "No version found",
                                    detail = "Try other version"
                            )
                        })
    }

    // ------ Course Programme -------

    // -------------------------------
    // Course Programme Methods
    // -------------------------------

    @Transactional
    override fun getAllCoursesOnSpecificProgramme(programmeId: Int): CourseProgrammeCollectionOutputModel {
        val courseProgrammes = courseDAO.getAllCoursesOnSpecificProgramme(programmeId)
        return toCourseProgrammeCollectionOutputModel(courseProgrammes.map {
            toCourseProgrammeOutputModel(
                    it,
                    courseDAO.getSpecificCourse(it.courseId)
                            .orElseThrow { NotFoundException("Course with id ${it.courseId} does not exist", "Try again later") }
            )
        })
    }

    @Transactional
    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): CourseProgrammeOutputModel {
        return toCourseProgrammeOutputModel(
                courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)
                        .orElseThrow { NotFoundException("No course with the id in this programme", "Add course to this programme") },
                courseDAO.getSpecificCourse(courseId)
                        .orElseThrow { NotFoundException("No course found", "Try another id") }
        )
    }

    @Transactional
    override fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel, principal: Principal): CourseProgrammeOutputModel {
        val course = courseDAO.getSpecificCourse(inputCourseProgramme.courseId)
                .orElseThrow { NotFoundException("Course does not exist", "Use a valid course") }

        val newCourseProgramme = courseDAO.addCourseToProgramme(programmeId, toCourseProgramme(inputCourseProgramme, principal.name))
        courseDAO.createCourseProgrammeVersion(toCourseProgrammeVersion(newCourseProgramme))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_PROGRAMME_TABLE,
                newCourseProgramme.logId
        ))
        return toCourseProgrammeOutputModel(newCourseProgramme, course)
    }

    @Transactional
    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseProgramme = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)
                .orElseThrow { NotFoundException("This course in this programme does not exist", "Try again") }
        resolveVote(principal.name, courseProgramme.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_PROGRAMME_TABLE, courseProgramme.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseProgramme.votes.dec() else courseProgramme.votes.inc()
        val success = courseDAO.updateVotesOnCourseProgramme(programmeId, courseId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseProgramme.createdBy,
                COURSE_PROGRAMME_TABLE,
                courseProgramme.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int, principal: Principal): Int {
        val courseProgramme = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)
                .orElseThrow { NotFoundException("This course in this programme does not exist", "Try again") }

        val success = courseDAO.deleteSpecificCourseProgramme(programmeId, courseId)

        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                COURSE_PROGRAMME_TABLE,
                courseProgramme.logId
        ))
        return success
    }

    // -------------------------------
    // Course Programme Stage Methods
    // -------------------------------

    @Transactional
    override fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): CourseProgrammeStageCollectionOutputModel {
        val courseProgrammeStageEntries = courseDAO.getAllCourseStageEntriesOfSpecificProgramme(programmeId).map { toCourseProgrammeStageOutputModel(it) }
        return toCourseProgrammeStageCollectionOutputModel(courseProgrammeStageEntries)
    }

    @Transactional
    override fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStageOutputModel {
        return toCourseProgrammeStageOutputModel(
                courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId)
                        .orElseThrow { NotFoundException("No staged version of course with this id in this programme", "Search other staged version") }
        )
    }

    @Transactional
    override fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel, principal: Principal): CourseProgrammeStageOutputModel {
        val courseProgrammeStage = courseDAO.createStagingCourseOfProgramme(toCourseProgrammeStage(programmeId, inputCourseProgramme, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_PROGRAMME_STAGE_TABLE,
                courseProgrammeStage.logId
        ))
        return toCourseProgrammeStageOutputModel(courseProgrammeStage)
    }

    @Transactional
    override fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int, principal: Principal): CourseProgrammeOutputModel {
        val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId)
                .orElseThrow { NotFoundException("No staged version of course with this id in this programme", "Search other staged version") }
        resolveApproval(principal.name, courseProgrammeStage.createdBy)

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
        return toCourseProgrammeOutputModel(courseProgramme, course)
    }

    @Transactional
    override fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId)
                .orElseThrow { NotFoundException("Either the stage or programme id are not valid", "Try other ids") }
        resolveVote(principal.name, courseProgrammeStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_PROGRAMME_STAGE_TABLE, courseProgrammeStage.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseProgrammeStage.votes.dec() else courseProgrammeStage.votes.inc()
        val success = courseDAO.updateVotesOnStagedCourseProgramme(programmeId, stageId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseProgrammeStage.createdBy,
                COURSE_PROGRAMME_STAGE_TABLE,
                courseProgrammeStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificStagedCourseProgramme(programmeId: Int, stageId: Int, principal: Principal): Int {
        val courseProgrammeStage = courseDAO.getSpecificStagedCourseProgramme(programmeId, stageId)
                .orElseThrow { NotFoundException("Either the stage or programme id are not valid", "Try other ids") }

        val success = courseDAO.deleteSpecificStagedCourseProgramme(programmeId, stageId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                courseProgrammeStage.createdBy,
                ActionType.REJECT_STAGE,
                COURSE_PROGRAMME_STAGE_TABLE,
                courseProgrammeStage.logId
        ))
        return success
    }

    // -------------------------------
    // Course Programme Report Methods
    // -------------------------------

    @Transactional
    override fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): CourseProgrammeReportCollectionOutputModel {
        val reports = courseDAO.getAllReportsOfCourseOnProgramme(programmeId, courseId)
                .map { toCourseProgrammeReportOutputModel(it) }
        return toCourseProgrammeReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOfCourseOnProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReportOutputModel {
        return toCourseProgrammeReportOutputModel(
                courseDAO.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
                        .orElseThrow { NotFoundException("No report of course with this id in this programme", "Search other report") }
        )
    }

    @Transactional
    override fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel, principal: Principal): CourseProgrammeReportOutputModel {
        val courseProgrammeReport = courseDAO.reportSpecificCourseOnProgramme(programmeId, courseId, toCourseProgrammeReport(programmeId, courseId, inputCourseReport, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_PROGRAMME_REPORT_TABLE,
                courseProgrammeReport.logId
        ))
        return toCourseProgrammeReportOutputModel(courseProgrammeReport)
    }

    @Transactional
    override fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int, principal: Principal): CourseProgrammeOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("The course is not valid", "Contact admins") }
        val courseProgramme = courseDAO.getSpecificCourseOfProgramme(programmeId, courseId)
                .orElseThrow { NotFoundException("Can't specified course in programme", "Try other ids") }
        val report = courseDAO.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
                .orElseThrow { NotFoundException("Can't find the specified report for this course in programme", "Search other report") }
        resolveApproval(principal.name, report.reportedBy)


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
                ActionType.APPROVE_REPORT,
                COURSE_PROGRAMME_REPORT_TABLE,
                report.logId,
                report.reportedBy,
                action,
                COURSE_PROGRAMME_TABLE,
                courseProgramme.logId
        ))
        return toRet
    }

    @Transactional
    override fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseProgrammeReport = courseDAO.getSpecificReportOfCourseProgramme(programmeId, courseId, reportId)
                .orElseThrow { NotFoundException("Can't find the specified report for this course in programme", "Search other report") }
        resolveVote(principal.name, courseProgrammeReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_PROGRAMME_REPORT_TABLE, courseProgrammeReport.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseProgrammeReport.votes.dec() else courseProgrammeReport.votes.inc()
        val success = courseDAO.updateVotesOnReportedCourseProgramme(programmeId, courseId, reportId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseProgrammeReport.reportedBy,
                PROGRAMME_REPORT_TABLE,
                courseProgrammeReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }


    @Transactional
    override fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, principal: Principal): Int =
            courseDAO.deleteSpecificReportOfCourseProgramme(programmeId, courseId, reportId)


    // -------------------------------
    // Course Programme Version Methods
    // -------------------------------

    @Transactional
    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): CourseProgrammeVersionCollectionOutputModel {
        val courseProgrammeVersions = courseDAO.getAllVersionsOfCourseOnProgramme(programmeId, courseId).map { toCourseProgrammeVersionOutput(it) }
        return toCourseProgrammeVersionCollectionOutputModel(courseProgrammeVersions)
    }

    @Transactional
    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): CourseProgrammeVersionOutputModel {
        return toCourseProgrammeVersionOutput(
                courseDAO.getSpecificVersionOfCourseOnProgramme(programmeId, courseId, version)
                        .orElseThrow { NotFoundException("No version of course with this id in this programme", "Search other version") }
        )
    }


}