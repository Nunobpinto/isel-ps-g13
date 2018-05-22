package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/programmes")
class ProgrammeController {

    //TODO implementation of Programme Controller methods
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
    fun createProgramme(@RequestBody input: ProgrammeInputModel)
            = programmeService.createProgramme(input)

    @PostMapping("/{programmeId}/courses")
    fun addCourseToProgramme(@PathVariable programmeId: Int,
                             @RequestBody course: Course
    ) = programmeService.addCourseToProgramme(programmeId, course)

    @PostMapping("/{programmeId}/vote")
    fun voteOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody vote: VoteInputModel
    ) = programmeService.voteOnProgramme(programmeId, vote)

    @PostMapping("/{programmeId}/reports")
    fun reportProgramme(
            @PathVariable programmeId: Int,
            @RequestBody report: ProgrammeReportInputModel
    ) = programmeService.reportProgramme(programmeId, report)

    @PostMapping("/{programmeId}/reports/{reportId}/vote")
    fun voteOnReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = programmeService.voteOnReportedProgramme(reportId, vote)

    @PostMapping("/{programmeId}/reports/{reportId}")
    fun updateReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.updateReportedProgramme(programmeId, reportId)

    @PostMapping("/stage")
    fun createStagingProgramme(@RequestBody programme: ProgrammeInputModel) = programmeService.createStagedProgramme(programme)

    @PostMapping("/stage/{stageId}")
    fun createProgrammeFromStaged(@PathVariable stageId: Int) = programmeService.createProgrammeFromStaged(stageId)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedProgramme(
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    /**
     * ALL PATCH Routes
     */
    @PatchMapping("/{programmeId}")
    fun partialUpdateOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody programme: ProgrammeInputModel
    ) = NotImplementedError()

    @PatchMapping("/stage/{stageId}")
    fun partialUpdateOnStagedProgramme(
            @PathVariable stageId: Int,
            @RequestBody programme: ProgrammeInputModel
    ) = NotImplementedError()

    /**
     * ALL DELETE Routes
     */
    @DeleteMapping
    fun deleteAllProgrammes() = NotImplementedError()

    @DeleteMapping("/{programmeId}")
    fun deleteSpecificProgramme(@PathVariable programmeId: Int) = NotImplementedError()

    @DeleteMapping("/stage")
    fun deleteAllStagedProgrammes() = NotImplementedError()

    @DeleteMapping("/stage/{stageId}")
    fun deleteStagedProgramme(@PathVariable stageId: Int) = NotImplementedError()

    @DeleteMapping("/{programmeId}/report")
    fun deleteAllReportsInProgramme(@PathVariable programmeId: Int) = NotImplementedError()

    @DeleteMapping("/{programmeId}/report/{reportId}")
    fun deleteReportInProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

}