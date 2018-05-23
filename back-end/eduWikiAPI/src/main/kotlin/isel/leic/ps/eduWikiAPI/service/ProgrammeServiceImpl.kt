package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var programmeRepo: ProgrammeDAO

    override fun getAllProgrammes(): List<Programme> {
        return programmeRepo.getAllProgrammes()
    }

    override fun getSpecificProgramme(programmeId: Int): Programme {
        return programmeRepo.getSpecificProgramme(programmeId)
    }

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
        programmeRepo.createProgramme(programme)
    }

    override fun createStagedProgramme(inputProgramme: ProgrammeInputModel) {
        val stage = ProgrammeStage(
                createdBy = inputProgramme.createdBy,
                fullName = inputProgramme.fullName,
                shortName = inputProgramme.shortName,
                academicDegree = inputProgramme.academicDegree,
                totalCredits = inputProgramme.totalCredits,
                duration = inputProgramme.duration,
                timestamp = Timestamp.valueOf(LocalDateTime.now())

        )
        programmeRepo.createProgrammeStage(stage)
    }

    override fun getSpecificStagedProgramme(stageId: Int) : ProgrammeStage =
            programmeRepo.getProgrammeStage(stageId)

    override fun createProgrammeFromStaged(stageId: Int) {
        val programmeStage = programmeRepo.getProgrammeStage(stageId)
        val programme = Programme(
                createdBy = programmeStage.createdBy,
                fullName = programmeStage.fullName,
                shortName = programmeStage.shortName,
                academicDegree = programmeStage.academicDegree,
                totalCredits = programmeStage.totalCredits,
                duration = programmeStage.duration,
                timestamp = Timestamp.valueOf(LocalDateTime.now())

        )
        programmeRepo.deleteStagedProgramme(stageId) //TODO use transaction
        programmeRepo.createProgramme(programme)    //TODO use transaction
    }

    override fun getStagedProgrammes(): List<ProgrammeStage> = programmeRepo.getAllProgrammeStages()

    override fun voteOnStagedProgramme(stageId: Int, inputVote: VoteInputModel) {
        return programmeRepo.voteOnStagedProgramme(stageId, inputVote)
    }

    override fun getAllReportsOfProgramme(programmeId: Int): List<ProgrammeReport>{
        return programmeRepo.getAllReportsOfProgramme(programmeId)
    }

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReport {
        return programmeRepo.getSpecificReportOfProgramme(programmeId, reportId)
    }

    override fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course> {
       return programmeRepo.getCoursesOnSpecificProgramme(programmeId)
    }

    override fun addCourseToProgramme(programmeId: Int, course: Course) {
        return programmeRepo.addCourseToProgramme(programmeId, course)
    }

    override fun voteOnProgramme(programmeId: Int, inputVote: VoteInputModel) {
        return programmeRepo.voteOnProgramme(programmeId, inputVote)
    }

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
        return programmeRepo.reportProgramme(programmeId, programmeReport)
    }

    override fun voteOnReportedProgramme(reportId: Int, inputVote: VoteInputModel) {
        return programmeRepo.voteOnReportedProgramme(reportId, inputVote)
    }

    override fun updateReportedProgramme(programmeId: Int, reportId: Int) {
        val programme = programmeRepo.getSpecificProgramme(programmeId)
        val report = programmeRepo.getSpecificReportOfProgramme(programmeId, reportId)
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
        programmeRepo.addToProgrammeVersion(programme)
        programmeRepo.updateProgramme(programmeId, updatedProgramme)
        programmeRepo.deleteReportOnProgramme(programmeId,reportId)
    }

    override fun deleteAllProgrammes() {
        return programmeRepo.deleteAllProgrammes()
    }

    override fun deleteSpecificProgramme(programmeId: Int) {
        return programmeRepo.deleteSpecificProgramme(programmeId)
    }

    override fun deleteAllStagedProgrammes() {
        return programmeRepo.deleteAllStagedProgrammes()
    }

    override fun deleteStagedProgramme(stageId: Int) {
        return programmeRepo.deleteStagedProgramme(stageId)
    }

    override fun deleteAllReportsOnProgramme(programmeId: Int) {
        return programmeRepo.deleteAllReportsOnProgramme(programmeId)
    }

    override fun deleteReportOnProgramme(programmeId: Int, reportId: Int) {
        return programmeRepo.deleteReportOnProgramme(programmeId, reportId)
    }

    override fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel) {
        val programme = programmeRepo.getSpecificProgramme(programmeId)
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
        programmeRepo.addToProgrammeVersion(programme)
        programmeRepo.updateProgramme(programmeId, updatedProgramme)
    }

    override fun partialUpdateOnStagedProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel) {
        val programme = programmeRepo.getProgrammeStage(programmeId)
        val updatedProgramme = ProgrammeStage(
                programmeId = programmeId,
                createdBy = inputProgramme.createdBy,
                fullName = if(!inputProgramme.fullName.isEmpty()) inputProgramme.fullName else programme.fullName,
                shortName = if(!inputProgramme.shortName.isEmpty()) inputProgramme.shortName else programme.shortName,
                academicDegree = if(!inputProgramme.academicDegree.isEmpty()) inputProgramme.academicDegree else programme.academicDegree,
                totalCredits = if(inputProgramme.totalCredits != 0) inputProgramme.totalCredits else programme.totalCredits,
                duration = if(inputProgramme.duration != 0) inputProgramme.duration else programme.duration
        )
        programmeRepo.updateStagedProgramme(programmeId, updatedProgramme)
    }


}