package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class ProgrammeServiceImpl : ProgrammeService {

    @Autowired
    lateinit var programmeRepo: ProgrammeDAO

    override fun createProgramme(input: ProgrammeInputModel) {
        val programme = Programme(
                createdBy = input.createdBy,
                fullName = input.fullName,
                shortName = input.shortName,
                academicDegree = input.academicDegree,
                totalCredits = input.totalCredits,
                duration = input.duration,
                version = 0

        )
        programmeRepo.createProgramme(programme)
    }

    override fun getSpecificProgramme(programmeId: Int): Programme =
         programmeRepo.getProgramme(programmeId)


    override fun createStagedProgramme(programme: ProgrammeInputModel) {
        val stage = ProgrammeStage(
                createdBy = programme.createdBy,
                fullName = programme.fullName,
                shortName = programme.shortName,
                academicDegree = programme.academicDegree,
                totalCredits = programme.totalCredits,
                duration = programme.duration,
                timestamp = Timestamp.valueOf(programme.timestamp)

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
                duration = programmeStage.duration
        )
        programmeRepo.deleteProgrammeStage(stageId)
        programmeRepo.createProgramme(programme)
    }

    override fun getStagedProgrammes(): List<ProgrammeStage> =
            programmeRepo.getAllProgrammeStages()

}