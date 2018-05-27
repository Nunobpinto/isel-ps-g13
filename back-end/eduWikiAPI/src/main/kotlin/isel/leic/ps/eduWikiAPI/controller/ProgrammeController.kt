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

    @GetMapping("/{programmeId}/courses/stage")
    fun getStagedCoursesOnSpecificProgramme(@PathVariable programmeId: Int) = programmeService.getStagedCoursesOfProgramme(programmeId)

    @GetMapping("/{programmeId}/courses/stage/{stageId}")
    fun getStagedCoursesOnSpecificProgramme(@PathVariable programmeId: Int, @PathVariable stageId: Int) = programmeService.getSpecificStagedCourseOfProgramme(programmeId, stageId)

    @GetMapping("/{programmeId}/courses/{courseId}")
    fun getCourseOnSpecificProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.getSpecificCourseOnSpecificProgramme(programmeId, courseId)

    @GetMapping("/{programmeId}/courses/{courseId}/versions")
    fun getVersionsOfCourseOnSpecificProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.getAllVersionsOfCourseOnSpecificProgramme(programmeId, courseId)

    @GetMapping("/{programmeId}/courses/{courseId}/versions/{versionId}")
    fun getVersionOfCourseOnSpecificProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable versionId: Int
    ) = programmeService.getSpecificVersionOfCourseOnSpecificProgramme(programmeId, courseId, versionId)

    @GetMapping("/{programmeId}/courses/{courseId}/reports")
    fun getReportsOfCourseOnSpecificProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int
    ) = programmeService.getAllReportsOfCourseOnSpecificProgramme(programmeId, courseId)

    @GetMapping("/{programmeId}/courses/{courseId}/reports/{reportId}")
    fun getReportOfCourseOnSpecificProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = programmeService.getSpecificReportOfCourseOnSpecificProgramme(programmeId, courseId, reportId)


    @GetMapping("/{programmeId}/versions")
    fun getAllVersionsOfProgramme(@PathVariable programmeId: Int) = programmeService.getAllVersions(programmeId)

    @GetMapping("/{programmeId}/versions/{versionId]")
    fun getVersionOfProgramme(@PathVariable programmeId: Int, @PathVariable versionId: Int) = programmeService.getVersion(programmeId, versionId)


    /**
     * All POST Routes
     */

    @PostMapping()
    fun createProgramme(@RequestBody inputProgramme: ProgrammeInputModel) = programmeService.createProgramme(inputProgramme)

    @PostMapping("/{programmeId}/courses")
    fun addCourseToProgramme(@PathVariable programmeId: Int,
                             @RequestBody inputCourseProgramme: CourseProgrammeInputModel
    ) = programmeService.addCourseToProgramme(programmeId, inputCourseProgramme)

    @PostMapping("/{programmeId}/courses/{courseId}/vote")
    fun voteOnCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnCourseProgramme(programmeId, courseId,inputVote)

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
    ) = programmeService.voteOnReportedProgramme(reportId, inputVote)

    @PostMapping("/{programmeId}/reports/{reportId}")
    fun updateReportedProgramme(
            @PathVariable programmeId: Int,
            @PathVariable reportId: Int
    ) = programmeService.updateReportedProgramme(programmeId, reportId)

    @PostMapping("/stage")
    fun createStagingProgramme(@RequestBody inputProgramme: ProgrammeInputModel) = programmeService.createStagingProgramme(inputProgramme)

    @PostMapping("/stage/{stageId}")
    fun createProgrammeFromStaged(@PathVariable stageId: Int) = programmeService.createProgrammeFromStaged(stageId)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedProgramme(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnStagedProgramme(stageId, inputVote)

    @PostMapping("/{programmeId}/courses/stage")
    fun createdStagedCourssOnSpecificProgramme(
            @PathVariable programmeId: Int,
            @RequestBody inputCourseProgramme: CourseProgrammeInputModel
    ) = programmeService.createStagedCourseOfProgramme(programmeId, inputCourseProgramme)

    @PostMapping("/{programmeId}/courses/stage/{stageId}")
    fun createCourseProgrammeFromStaged(@PathVariable programmeId: Int, @PathVariable stageId: Int) = programmeService.createCourseProgrammeFromStaged(programmeId, stageId)

    @PostMapping("/{programmeId}/courses/stage/{stageId}/vote")
    fun voteCourseProgrammeFromStaged(
            @PathVariable programmeId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = programmeService.voteOnCourseProgrammeStaged(programmeId, stageId, inputVote)

    @PostMapping("/{programmeId}/courses/{courseId}/reports")
    fun reportCourseOnSpecificProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputCourseProgrammeReport: CourseProgrammeReportInputModel
    ) = programmeService.reportCourseOnProgramme(programmeId, courseId, inputCourseProgrammeReport)

    @PostMapping("/{programmeId}/courses/{courseId}/reports/{reportId}")
    fun updateReportedCourseProgramme(
            @PathVariable programmeId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = programmeService.updateReportedCourseProgramme(programmeId, courseId, reportId)

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

    @DeleteMapping("/{programmeId}/versions")
    fun deleteAllVersions(@PathVariable programmeId: Int) = programmeService.deleteAllVersions(programmeId)

    @DeleteMapping("/{programmeId}/versions/{versionId}")
    fun deleteSpecificVersion(@PathVariable programmeId: Int, @PathVariable versionId: Int) = programmeService.deleteSpecificVersion(programmeId, versionId)

}