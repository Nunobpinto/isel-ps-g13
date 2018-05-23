package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/programmes")
class ProgrammeController {

    @Autowired
    lateinit var programmeService: ProgrammeService

    /**
     * All GET Routes
     */

    @GetMapping
    fun getAllProgrammes() = programmeService.getAllProgrammes()

    @GetMapping("/{programmeId}")
    fun getSpecificProgramme(@PathVariable programmeId: Int) = programmeService.getSpecificProgramme(programmeId)

    @GetMapping("/{programmeId}/reports")
    fun getAllReportsOfProgramme(@PathVariable programmeId: Int) = programmeService.getAllReportsOfProgramme(programmeId)

    @GetMapping("/{programmeId}/reports/{reportId}")
    fun getSpecificReportOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.getSpecificReportOfProgramme(programmeId, reportId)

    @GetMapping("/stage")
    fun getAllProgrammeStageEntries() = programmeService.getStagedProgrammes()

    @GetMapping("/stage/{stageId}")
    fun getProgrammeSpecificStageEntry(@PathVariable stageId: Int) = programmeService.getSpecificStagedProgramme(stageId)

    @GetMapping("/{programmeId}/courses")
    fun getCoursesOnSpecificProgramme(@PathVariable programmeId: Int) = programmeService.getCoursesOnSpecificProgramme(programmeId)

    /**
     * All POST Routes
     */

    @PostMapping()
    fun createProgramme(@RequestBody inputProgramme: ProgrammeInputModel)
            = programmeService.createProgramme(inputProgramme)

    @PostMapping("/{programmeId}/courses")
    fun addCourseToProgramme(@PathVariable programmeId: Int,
                             @RequestBody course: Course
    ) = programmeService.addCourseToProgramme(programmeId, course)

    @PostMapping("/{programmeId}/inputVote")
    fun voteOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnProgramme(programmeId, inputVote)

    @PostMapping("/{programmeId}/reports")
    fun reportProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputProgrammeReport: ProgrammeReportInputModel
    ) = programmeService.reportProgramme(programmeId, inputProgrammeReport)

    @PostMapping("/{programmeId}/reports/{reportId}/inputVote")
    fun voteOnReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnReportedProgramme(reportId, inputVote)

    @PostMapping("/{programmeId}/reports/{reportId}")
    fun updateReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.updateReportedProgramme(programmeId, reportId)

    @PostMapping("/stage")
    fun createStagingProgramme(@RequestBody inputProgramme: ProgrammeInputModel) = programmeService.createStagedProgramme(inputProgramme)

    @PostMapping("/stage/{stageId}")
    fun createProgrammeFromStaged(@PathVariable stageId: Int) = programmeService.createProgrammeFromStaged(stageId)

    @PostMapping("/stage/{stageId}/inputVote")
    fun voteOnStagedProgramme(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnStagedProgramme(stageId, inputVote)

    /**
     * ALL PATCH Routes
     */

    @PatchMapping("/{programmeId}")
    fun partialUpdateOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputProgramme: ProgrammeInputModel
    ) = programmeService.partialUpdateOnProgramme(programmeId, inputProgramme)

    @PatchMapping("/stage/{stageId}")
    fun partialUpdateOnStagedProgramme(
            @PathVariable stageId: Int,
            @RequestBody inputProgramme: ProgrammeInputModel
    ) = programmeService.partialUpdateOnStagedProgramme(stageId, inputProgramme)

    /**
     * ALL DELETE Routes
     */

    @DeleteMapping
    fun deleteAllProgrammes() = programmeService.deleteAllProgrammes()

    @DeleteMapping("/{programmeId}")
    fun deleteSpecificProgramme(@PathVariable programmeId: Int) = programmeService.deleteSpecificProgramme(programmeId)

    @DeleteMapping("/stage")
    fun deleteAllStagedProgrammes() = programmeService.deleteAllStagedProgrammes()

    @DeleteMapping("/stage/{stageId}")
    fun deleteStagedProgramme(@PathVariable stageId: Int) = programmeService.deleteStagedProgramme(stageId)

    @DeleteMapping("/{programmeId}/report")
    fun deleteAllReportsOnProgramme(@PathVariable programmeId: Int) = programmeService.deleteAllReportsOnProgramme(programmeId)

    @DeleteMapping("/{programmeId}/report/{reportId}")
    fun deleteReportOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.deleteReportOnProgramme(programmeId, reportId)

}