package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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


}