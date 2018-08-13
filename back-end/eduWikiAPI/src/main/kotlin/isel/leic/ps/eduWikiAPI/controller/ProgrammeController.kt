package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.service.interfaces.ProgrammeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/programmes")
class ProgrammeController {

    @Autowired
    lateinit var programmeService: ProgrammeService

    // ---------- Programme ----------

    // -------------------------------
    // Programme Endpoints
    // -------------------------------

    @GetMapping
    fun getAllProgrammes() = programmeService.getAllProgrammes()

    @GetMapping("/{programmeId}")
    fun getSpecificProgramme(@PathVariable programmeId: Int) = programmeService.getSpecificProgramme(programmeId)

    @PostMapping()
    fun createProgramme(@RequestBody inputProgramme: ProgrammeInputModel, principal: Principal) =
            programmeService.createProgramme(inputProgramme, principal)

    @PostMapping("/{programmeId}/vote")
    fun voteOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = programmeService.voteOnProgramme(programmeId, inputVote, principal)

    @PatchMapping("/{programmeId}")
    fun partialUpdateOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputProgramme: ProgrammeInputModel,
            principal: Principal
    ) = programmeService.partialUpdateOnProgramme(programmeId, inputProgramme, principal)

    @DeleteMapping("/{programmeId}")
    fun deleteSpecificProgramme(@PathVariable programmeId: Int, principal: Principal) =
            programmeService.deleteSpecificProgramme(programmeId, principal)

    // -------------------------------
    // Programme Report Endpoints
    // -------------------------------

    @GetMapping("/{programmeId}/reports")
    fun getAllReportsOfSpecificProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllReportsOfSpecificProgramme(programmeId)

    @GetMapping("/{programmeId}/reports/{reportId}")
    fun getSpecificReportOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.getSpecificReportOfProgramme(programmeId, reportId)

    @PostMapping("/{programmeId}/reports")
    fun reportProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputProgrammeReport: ProgrammeReportInputModel,
            principal: Principal
    ) = programmeService.reportProgramme(programmeId, inputProgrammeReport, principal)

    @PostMapping("/{programmeId}/reports/{reportId}/vote")
    fun voteOnReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = programmeService.voteOnReportedProgramme(programmeId, reportId, inputVote, principal)

    @PostMapping("/{programmeId}/reports/{reportId}")
    fun updateProgrammeFromReport(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = programmeService.updateProgrammeFromReport(programmeId, reportId, principal)

    @DeleteMapping("/{programmeId}/reports/{reportId}")
    fun deleteSpecificReportOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = programmeService.deleteSpecificReportOnProgramme(programmeId, reportId, principal)

    // -------------------------------
    // Programme Stage Endpoints
    // -------------------------------

    @GetMapping("/stage")
    fun getAllProgrammeStageEntries() = programmeService.getAllProgrammeStageEntries()

    @GetMapping("/stage/{stageId}")
    fun getSpecificStageEntryOfProgramme(@PathVariable stageId: Int) =
            programmeService.getSpecificStageEntryOfProgramme(stageId)

    @PostMapping("/stage")
    fun createStagingProgramme(@RequestBody inputProgramme: ProgrammeInputModel, principal: Principal) =
            programmeService.createStagingProgramme(inputProgramme, principal)

    @PostMapping("/stage/{stageId}")
    fun createProgrammeFromStaged(@PathVariable stageId: Int, principal: Principal) =
            programmeService.createProgrammeFromStaged(stageId, principal)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedProgramme(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = programmeService.voteOnStagedProgramme(stageId, inputVote, principal)

    @DeleteMapping("/stage/{stageId}")
    fun deleteSpecificStagedProgramme(@PathVariable stageId: Int, principal: Principal) =
            programmeService.deleteSpecificStagedProgramme(stageId, principal)

    // -------------------------------
    // Programme Version Endpoints
    // -------------------------------

    @GetMapping("/{programmeId}/versions")
    fun getAllVersionsOfProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllVersionsOfProgramme(programmeId)

    @GetMapping("/{programmeId}/versions/{version}")
    fun getSpecificVersionOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable version: Int
    ) = programmeService.getSpecificVersionOfProgramme(programmeId, version)

    // ------ Programme Course -------

    // -------------------------------
    // Programme Course Endpoints
    // -------------------------------

    @GetMapping("/{programmeId}/courses/{courseId}")
    fun getSpecificCourseOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.getSpecificCourseOfProgramme(programmeId, courseId)

    @GetMapping("/{programmeId}/courses")
    fun getAllCoursesOnSpecificProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllCoursesOnSpecificProgramme(programmeId)

    @PostMapping("/{programmeId}/courses")
    fun addCourseToProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputCourseProgramme: CourseProgrammeInputModel,
            principal: Principal
    ) = programmeService.addCourseToProgramme(programmeId, inputCourseProgramme, principal)

    @PostMapping("/{programmeId}/courses/{courseId}/vote")
    fun voteOnCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = programmeService.voteOnCourseProgramme(programmeId, courseId, inputVote, principal)

    @DeleteMapping("/{programmeId}/courses/{courseId}")
    fun deleteSpecificCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            principal: Principal
    ) = programmeService.deleteSpecificCourseProgramme(programmeId, courseId, principal)

    // -------------------------------
    // Programme Course Report Endpoints
    // -------------------------------

    @GetMapping("/{programmeId}/courses/{courseId}/reports")
    fun getAllReportsOfCourseOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.getAllReportsOfCourseOnProgramme(programmeId, courseId)

    @GetMapping("/{programmeId}/courses/{courseId}/reports/{reportId}")
    fun getSpecificReportOfCourseOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = programmeService.getSpecificReportOfCourseOnProgramme(programmeId, courseId, reportId)

    @PostMapping("/{programmeId}/courses/{courseId}/reports")
    fun reportSpecificCourseOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputCourseProgrammeReport: CourseProgrammeReportInputModel,
            principal: Principal
    ) = programmeService.reportSpecificCourseOnProgramme(programmeId, courseId, inputCourseProgrammeReport, principal)

    @PostMapping("/{programmeId}/courses/{courseId}/reports/{reportId}")
    fun updateCourseProgrammeFromReport(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = programmeService.updateCourseProgrammeFromReport(programmeId, courseId, reportId, principal)

    @PostMapping("/{programmeId}/courses/{courseId}/reports/{reportId}/vote")
    fun voteOnReportedCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = programmeService.voteOnReportedCourseProgramme(programmeId, courseId, reportId, inputVote, principal)

    @DeleteMapping("/{programmeId}/courses/{courseId}/reports/{reportId}")
    fun deleteSpecificReportOfCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = programmeService.deleteSpecificReportOfCourseProgramme(programmeId, courseId, reportId, principal)

    // -------------------------------
    // Programme Course Stage Endpoints
    // -------------------------------

    @GetMapping("/{programmeId}/courses/stage")
    fun getAllCourseStageEntriesOfSpecificProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllCourseStageEntriesOfSpecificProgramme(programmeId)

    @GetMapping("/{programmeId}/courses/stage/{stageId}")
    fun getSpecificStagedCourseOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int
    ) = programmeService.getSpecificStagedCourseOfProgramme(programmeId, stageId)

    @PostMapping("/{programmeId}/courses/stage")
    fun createStagingCourseOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputCourseProgramme: CourseProgrammeInputModel,
            principal: Principal
    ) = programmeService.createStagingCourseOnProgramme(programmeId, inputCourseProgramme, principal)

    @PostMapping("/{programmeId}/courses/stage/{stageId}")
    fun createCourseProgrammeFromStaged(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = programmeService.createCourseProgrammeFromStaged(programmeId, stageId, principal)

    @PostMapping("/{programmeId}/courses/stage/{stageId}/vote")
    fun voteOnStagedCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = programmeService.voteOnStagedCourseProgramme(programmeId, stageId, inputVote, principal)

    @DeleteMapping("/{programmeId}/courses/stage/{stageId}")
    fun deleteSpecificStagedCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = programmeService.deleteSpecificStagedCourseProgramme(programmeId, stageId, principal)

    // -------------------------------
    // Programme Course Version Endpoints
    // -------------------------------

    @GetMapping("/{programmeId}/courses/{courseId}/versions")
    fun getAllVersionsOfCourseOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.getAllVersionsOfCourseOnProgramme(programmeId, courseId)

    @GetMapping("/{programmeId}/courses/{courseId}/versions/{version}")
    fun getSpecificVersionOfCourseOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable version: Int
    ) = programmeService.getSpecificVersionOfCourseOnProgramme(programmeId, courseId, version)

}