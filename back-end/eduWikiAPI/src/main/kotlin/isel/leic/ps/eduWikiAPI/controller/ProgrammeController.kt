package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.inputModel.*
import isel.leic.ps.eduWikiAPI.service.ProgrammeService
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
    fun getAllProgrammes() = NotImplementedError()

    @GetMapping("/{programmeId}")
    fun getSpecificProgramme(@PathVariable programmeId: Int) = NotImplementedError()

    @GetMapping("/{programmeId}/report")
    fun getProgrammeReports(@PathVariable programmeId: Int) = NotImplementedError()

    @GetMapping("/{programmeId}/report/{reportId}")
    fun getSpecificProgrammeReport(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @GetMapping("/stage")
    fun getAllProgrammeStageEntries() = NotImplementedError()

    @GetMapping("/stage/{stageId}")
    fun getProgrammeSpecificStageEntry() = NotImplementedError()

    @GetMapping("/{programmeId}/courses")
    fun getCoursesOnSpecificProgramme(@PathVariable programmeId: Int) = NotImplementedError()

    /**
     * All POST Routes
     */
    @PostMapping()
    fun createProgramme(@RequestBody input: ProgrammeInputModel) = NotImplementedError()

    @PostMapping("/{programmeId}/vote")
    fun voteOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{programmeId}/report")
    fun reportProgramme(
            @PathVariable programmeId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{programmeId}/report/{reportId}/vote")
    fun voteOnReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{programmeId}/report/{reportId}")
    fun updateReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/stage")
    fun createStagingProgramme(@RequestBody programme: ProgrammeInputModel) = NotImplementedError()

    @PostMapping("/stage/{stageId}")
    fun createProgrammeFromStaged(@PathVariable stageId: Int) = NotImplementedError()

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