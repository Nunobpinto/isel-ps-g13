package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport

interface ProgrammeService {

    fun createProgramme(input: ProgrammeInputModel)

    fun getSpecificProgramme(programmeId: Int): Programme

    fun createStagedProgramme(programme: ProgrammeInputModel)

    fun getSpecificStagedProgramme(stageId: Int) : ProgrammeStage

    fun createProgrammeFromStaged(stageId: Int)

    fun getStagedProgrammes() : List<ProgrammeStage>

    fun getAllProgrammes(): List<Programme>

    fun getAllReportsOfProgramme(programmeId: Int): List<ProgrammeReport>

    fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReport

    fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course>

}