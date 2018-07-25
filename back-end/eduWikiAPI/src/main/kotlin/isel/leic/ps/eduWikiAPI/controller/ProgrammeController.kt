package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
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
    fun getAllReportsOfSpecificProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllReportsOfSpecificProgramme(programmeId)

    @GetMapping("/{programmeId}/reports/{reportId}")
    fun getSpecificReportOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.getSpecificReportOfProgramme(programmeId, reportId)

    @GetMapping("/stage")
    fun getAllProgrammeStageEntries() = programmeService.getAllProgrammeStageEntries()

    @GetMapping("/stage/{stageId}")
    fun getSpecificStageEntryOfProgramme(@PathVariable stageId: Int) =
            programmeService.getSpecificStageEntryOfProgramme(stageId)

    @GetMapping("/{programmeId}/courses")
    fun getAllCoursesOnSpecificProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllCoursesOnSpecificProgramme(programmeId)

    @GetMapping("/{programmeId}/courses/stage")
    fun getAllCourseStageEntriesOfSpecificProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllCourseStageEntriesOfSpecificProgramme(programmeId)

    @GetMapping("/{programmeId}/courses/stage/{stageId}")
    fun getSpecificStagedCourseOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int
    ) = programmeService.getSpecificStagedCourseOfProgramme(programmeId, stageId)

    @GetMapping("/{programmeId}/courses/{courseId}")
    fun getSpecificCourseOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.getSpecificCourseOfProgramme(programmeId, courseId)

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

    @GetMapping("/{programmeId}/versions")
    fun getAllVersionsOfProgramme(@PathVariable programmeId: Int) =
            programmeService.getAllVersionsOfProgramme(programmeId)

    @GetMapping("/{programmeId}/versions/{version}")
    fun getSpecificVersionOfProgramme(
            @PathVariable programmeId: Int,
            @PathVariable version: Int
    ) = programmeService.getSpecificVersionOfProgramme(programmeId, version)

    /**
     * All POST Routes
     */

    @PostMapping()
    fun createProgramme(@RequestBody inputProgramme: ProgrammeInputModel) =
            programmeService.createProgramme(inputProgramme)

    @PostMapping("/{programmeId}/courses")
    fun addCourseToProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputCourseProgramme: CourseProgrammeInputModel
    ) = programmeService.addCourseToProgramme(programmeId, inputCourseProgramme)

    @PostMapping("/{programmeId}/courses/{courseId}/vote")
    fun voteOnCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnCourseProgramme(programmeId, courseId, inputVote)

    @PostMapping("/{programmeId}/vote")
    fun voteOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnProgramme(programmeId, inputVote)

    @PostMapping("/{programmeId}/reports")
    fun reportProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputProgrammeReport: ProgrammeReportInputModel
    ) = programmeService.reportProgramme(programmeId, inputProgrammeReport)

    @PostMapping("/{programmeId}/reports/{reportId}/vote")
    fun voteOnReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnReportedProgramme(programmeId, reportId, inputVote)

    @PostMapping("/{programmeId}/reports/{reportId}")
    fun updateProgrammeFromReport(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.updateProgrammeFromReport(programmeId, reportId)

    @PostMapping("/stage")
    fun createStagingProgramme(@RequestBody inputProgramme: ProgrammeInputModel) =
            programmeService.createStagingProgramme(inputProgramme)

    @PostMapping("/stage/{stageId}")
    fun createProgrammeFromStaged(@PathVariable stageId: Int) =
            programmeService.createProgrammeFromStaged(stageId)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedProgramme(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnStagedProgramme(stageId, inputVote)

    @PostMapping("/{programmeId}/courses/stage")
    fun createStagingCourseOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputCourseProgramme: CourseProgrammeInputModel
    ) = programmeService.createStagingCourseOnProgramme(programmeId, inputCourseProgramme)

    @PostMapping("/{programmeId}/courses/stage/{stageId}")
    fun createCourseProgrammeFromStaged(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int
    ) = programmeService.createCourseProgrammeFromStaged(programmeId, stageId)

    @PostMapping("/{programmeId}/courses/stage/{stageId}/vote")
    fun voteOnStagedCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnStagedCourseProgramme(programmeId, stageId, inputVote)

    @PostMapping("/{programmeId}/courses/{courseId}/reports")
    fun reportSpecificCourseOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputCourseProgrammeReport: CourseProgrammeReportInputModel
    ) = programmeService.reportSpecificCourseOnProgramme(programmeId, courseId, inputCourseProgrammeReport)

    @PostMapping("/{programmeId}/courses/{courseId}/reports/{reportId}")
    fun updateCourseProgrammeFromReport(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = programmeService.updateCourseProgrammeFromReport(programmeId, courseId, reportId)

    @PostMapping("/{programmeId}/courses/{courseId}/reports/{reportId}/vote")
    fun voteOnReportedCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnReportedCourseProgramme(programmeId, courseId, reportId, inputVote)


    /**
     * ALL PATCH Routes
     */

    @PatchMapping("/{programmeId}")
    fun partialUpdateOnProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputProgramme: ProgrammeInputModel
    ) = programmeService.partialUpdateOnProgramme(programmeId, inputProgramme)

    /**
     * ALL DELETE Routes
     */

    @DeleteMapping
    fun deleteAllProgrammes() = programmeService.deleteAllProgrammes()

    @DeleteMapping("/{programmeId}")
    fun deleteSpecificProgramme(@PathVariable programmeId: Int) =
            programmeService.deleteSpecificProgramme(programmeId)

    @DeleteMapping("/stage")
    fun deleteAllStagedProgrammes() = programmeService.deleteAllStagedProgrammes()

    @DeleteMapping("/stage/{stageId}")
    fun deleteSpecificStagedProgramme(@PathVariable stageId: Int) =
            programmeService.deleteSpecificStagedProgramme(stageId)

    @DeleteMapping("/{programmeId}/report")
    fun deleteAllReportsOnProgramme(@PathVariable programmeId: Int) =
            programmeService.deleteAllReportsOnProgramme(programmeId)

    @DeleteMapping("/{programmeId}/report/{reportId}")
    fun deleteSpecificReportOnProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.deleteSpecificReportOnProgramme(programmeId, reportId)

    @DeleteMapping("/{programmeId}/versions")
    fun deleteAllProgrammeVersions(@PathVariable programmeId: Int) =
            programmeService.deleteAllProgrammeVersions(programmeId)

    @DeleteMapping("/{programmeId}/courses/{courseId}")
    fun deleteSpecificCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.deleteSpecificCourseProgramme(programmeId, courseId)

    @DeleteMapping("/{programmeId}/courses/{courseId}/versions/{version}")
    fun deleteSpecificVersionOfCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable version: Int
    ) = programmeService.deleteSpecificVersionOfCourseProgramme(programmeId, courseId, version)

    @DeleteMapping("/{programmeId}/courses/{courseId}/reports/{reportId}")
    fun deleteSpecificReportOfCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = programmeService.deleteSpecificReportOfCourseProgramme(programmeId, courseId, reportId)

    @DeleteMapping("/{programmeId}/courses/stage/{stageId}")
    fun deleteSpecificStagedCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = programmeService.deleteSpecificStagedCourseProgramme(programmeId, courseId, stageId)

    @DeleteMapping("/{programmeId}/versions/{version}")
    fun deleteSpecificProgrammeVersion(
            @PathVariable programmeId: Int,
            @PathVariable version: Int
    ) = programmeService.deleteSpecificProgrammeVersion(programmeId, version)

}