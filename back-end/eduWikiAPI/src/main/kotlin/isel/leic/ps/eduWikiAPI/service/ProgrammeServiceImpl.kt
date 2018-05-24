package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
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

    override fun getAllProgrammes(): List<Programme> = programmeDAO.getAllProgrammes()


    override fun getSpecificProgramme(programmeId: Int): Programme = programmeDAO.getSpecificProgramme(programmeId)

    override fun createProgramme(inputProgramme: ProgrammeInputModel) {
        val programme = Programme(
                createdBy = inputProgramme.createdBy,
                fullName = inputProgramme.fullName,
                shortName = inputProgramme.shortName,
                academicDegree = inputProgramme.academicDegree,
                totalCredits = inputProgramme.totalCredits,
                duration = inputProgramme.duration,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        programmeDAO.createProgramme(programme)
    }

    override fun createStagingProgramme(inputProgramme: ProgrammeInputModel) {
        val stage = ProgrammeStage(
                createdBy = inputProgramme.createdBy,
                fullName = inputProgramme.fullName,
                shortName = inputProgramme.shortName,
                academicDegree = inputProgramme.academicDegree,
                totalCredits = inputProgramme.totalCredits,
                duration = inputProgramme.duration,
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
        programmeDAO.deleteStagedProgramme(stageId) //TODO use transaction
        programmeDAO.createProgramme(programme)    //TODO use transaction
    }

    override fun getStagedProgrammes(): List<ProgrammeStage> = programmeDAO.getAllProgrammeStages()

    override fun voteOnStagedProgramme(stageId: Int, inputVote: VoteInputModel) = programmeDAO.voteOnStagedProgramme(stageId, inputVote)

    override fun getAllReportsOfProgramme(programmeId: Int): List<ProgrammeReport> = programmeDAO.getAllReportsOfProgramme(programmeId)

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReport = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)

    override fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course> = programmeDAO.getCoursesOnSpecificProgramme(programmeId)

    override fun addCourseToProgramme(programmeId: Int, course: Course) = programmeDAO.addCourseToProgramme(programmeId, course)

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

    override fun voteOnReportedProgramme(reportId: Int, inputVote: VoteInputModel) = programmeDAO.voteOnReportedProgramme(reportId, inputVote)

    override fun updateReportedProgramme(programmeId: Int, reportId: Int) {
        val programme = programmeDAO.getSpecificProgramme(programmeId)
        val report = programmeDAO.getSpecificReportOfProgramme(programmeId, reportId)
        val updatedProgramme = Programme(
                id = programme.id,
                version = programme.version + 1,
                votes = programme.votes,
                createdBy = programme.createdBy,
                fullName = report.programmeFullName ?: programme.fullName,
                shortName =report.programmeShortName ?: programme.shortName,
                academicDegree = report.programmeAcademicDegree ?: programme.academicDegree,
                totalCredits = if(report.programmeTotalCredits != 0) report.programmeTotalCredits!! else programme.totalCredits,
                duration = if(report.programmeDuration!=0) report.programmeDuration!! else programme.duration,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        programmeDAO.addToProgrammeVersion(programme)
        programmeDAO.updateProgramme(programmeId, updatedProgramme)
        programmeDAO.deleteReportOnProgramme(programmeId,reportId)
    }

    override fun deleteAllProgrammes() = programmeDAO.deleteAllProgrammes()

    override fun deleteSpecificProgramme(programmeId: Int) = programmeDAO.deleteSpecificProgramme(programmeId)


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
                version = inputProgramme.version + 1,
                createdBy = inputProgramme.createdBy,
                fullName = if(!inputProgramme.fullName.isEmpty()) inputProgramme.fullName else programme.fullName,
                shortName = if(!inputProgramme.shortName.isEmpty()) inputProgramme.shortName else programme.shortName,
                academicDegree = if(!inputProgramme.academicDegree.isEmpty()) inputProgramme.academicDegree else programme.academicDegree,
                totalCredits = if(inputProgramme.totalCredits != 0) inputProgramme.totalCredits else programme.totalCredits,
                duration = if(inputProgramme.duration != 0) inputProgramme.duration else programme.duration
        )
        programmeDAO.addToProgrammeVersion(programme)
        programmeDAO.updateProgramme(programmeId, updatedProgramme)
    }

    override fun partialUpdateOnStagedProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel) {
        val programme = programmeDAO.getSpecificProgrammeStage(programmeId)
        val updatedProgramme = ProgrammeStage(
                programmeId = programmeId,
                createdBy = inputProgramme.createdBy,
                fullName = if(!inputProgramme.fullName.isEmpty()) inputProgramme.fullName else programme.fullName,
                shortName = if(!inputProgramme.shortName.isEmpty()) inputProgramme.shortName else programme.shortName,
                academicDegree = if(!inputProgramme.academicDegree.isEmpty()) inputProgramme.academicDegree else programme.academicDegree,
                totalCredits = if(inputProgramme.totalCredits != 0) inputProgramme.totalCredits else programme.totalCredits,
                duration = if(inputProgramme.duration != 0) inputProgramme.duration else programme.duration
        )
        programmeDAO.updateStagedProgramme(programmeId, updatedProgramme)
    }

    override fun getAllVersions(programmeId: Int): List<ProgrammeVersion> = programmeDAO.getAllVersionsOfProgramme(programmeId)

    override fun getVersion(programmeId: Int, versionId: Int): ProgrammeVersion = programmeDAO.getSpecificVersionOfProgramme(programmeId, versionId)

    override fun deleteAllVersions(programmeId: Int): Int = programmeDAO.deleteAllVersionsOfProgramme(programmeId)

    override fun deleteSpecificVersion(programmeId: Int, versionId: Int): Int = programmeDAO.deleteVersionProgramme(programmeId, versionId)


}