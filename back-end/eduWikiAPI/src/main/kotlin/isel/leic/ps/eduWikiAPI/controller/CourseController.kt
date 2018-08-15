package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/courses")
class CourseController {

    @Autowired
    lateinit var courseService: CourseService


    // ---------- COURSE ----------

    // ----------------------------
    // Course Endpoints
    // ----------------------------

    @GetMapping
    fun getAllCourses() = courseService.getAllCourses()

    @GetMapping("/{courseId}")
    fun getSpecificCourse(@PathVariable courseId: Int) = courseService.getSpecificCourse(courseId)

    @PostMapping
    fun createCourse(@RequestBody inputCourse: CourseInputModel, principal: Principal) = courseService.createCourse(inputCourse, principal)

    @PostMapping("/{courseId}/vote")
    fun voteOnCourse(
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnCourse(courseId, inputVote, principal)

    @PatchMapping("/{courseId}")
    fun partialUpdateOnCourse(
            @PathVariable courseId: Int,
            @RequestBody inputCourse: CourseInputModel,
            principal: Principal
    ) = courseService.partialUpdateOnCourse(courseId, inputCourse, principal)

    @DeleteMapping("/{courseId}")
    fun deleteSpecificCourse(@PathVariable courseId: Int, principal: Principal) = courseService.deleteSpecificCourse(courseId, principal)

    // ----------------------------
    // Course Version Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/versions")
    fun getAllVersionsOfSpecificCourse(@PathVariable courseId: Int) =
            courseService.getAllVersionsOfSpecificCourse(courseId)

    @GetMapping("/{courseId}/versions/{versionId}")
    fun getVersionOfSpecificCourse(
            @PathVariable courseId: Int,
            @PathVariable versionId: Int
    ) = courseService.getVersionOfSpecificCourse(courseId, versionId)

    // ----------------------------
    // Course Report Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/reports")
    fun getAllReportsOnCourse(@PathVariable courseId: Int) = courseService.getAllReportsOnCourse(courseId)

    @GetMapping("/{courseId}/reports/{reportId}")
    fun getSpecificReportOfCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportOfCourse(courseId, reportId)

    @PostMapping("/{courseId}/reports")
    fun reportCourse(
            @PathVariable courseId: Int,
            @RequestBody inputCourseReport: CourseReportInputModel,
            principal: Principal
    ) = courseService.reportCourse(courseId, inputCourseReport, principal)

    @PostMapping("/{courseId}/reports/{reportId}/vote")
    fun voteOnReportOfCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnReportedCourse(courseId, reportId, inputVote, principal)

    @PostMapping("/{courseId}/reports/{reportId}")
    fun updateReportedCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = courseService.updateReportedCourse(courseId, reportId, principal)

    @DeleteMapping("/{courseId}/reports/{reportId}")
    fun deleteReportOnCourse(@PathVariable courseId: Int, reportId: Int, principal: Principal) = courseService.deleteReportOnCourse(courseId, reportId, principal)

    // ----------------------------
    // Course Stage Endpoints
    // ----------------------------

    @GetMapping("/stage")
    fun getAllCourseStageEntries() = courseService.getAllCourseStageEntries()

    @GetMapping("/stage/{stageId}")
    fun getCourseSpecificStageEntry(@PathVariable stageId: Int) = courseService.getCourseSpecificStageEntry(stageId)

    @PostMapping("/stage")
    fun createStagingCourse(@RequestBody inputCourse: CourseInputModel, principal: Principal) = courseService.createStagingCourse(inputCourse, principal)

    @PostMapping("/stage/{stageId}")
    fun createCourseFromStaged(@PathVariable stageId: Int, principal: Principal) = courseService.createCourseFromStaged(stageId, principal)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedCourse(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnStagedCourse(stageId, inputVote, principal)

    @DeleteMapping("/stage/{stageId}")
    fun deleteSpecificStagedCourse(@PathVariable stageId: Int, principal: Principal) = courseService.deleteSpecificStagedCourse(stageId, principal)

    // ----------- TERM -----------

    // ----------------------------
    // Term related Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms")
    fun getTermsOfCourse(@PathVariable courseId: Int) = courseService.getTermsOfCourse(courseId)

    @GetMapping("/{courseId}/terms/{termId}")
    fun getSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getSpecificTermOfCourse(courseId, termId)

    // ----------- EXAM -----------

    // ----------------------------
    // Exam Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/exams")
    fun getAllExamsFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getAllExamsFromSpecificTermOfCourse(courseId, termId)

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}")
    fun getSpecificExamFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)

    @PostMapping("/{courseId}/terms/{termId}/exams")
    fun createExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestParam sheet: MultipartFile,
            examInputModel: ExamInputModel,  //TODO check if @RequestBody does work too
            principal: Principal
    ) = courseService.createExamOnCourseInTerm(termId, courseId, sheet, examInputModel, principal)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/vote")
    fun voteOnExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnExam(termId, courseId, examId, inputVote, principal)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}")
    fun deleteSpecificExamOfCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            principal: Principal
    ) = courseService.deleteSpecificExamOfCourseInTerm(courseId, termId, examId, principal)

    // ----------------------------
    // Exam Version Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/versions")
    fun getAllVersionsOfSpecificExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.getAllVersionsOfSpecificExam(termId, courseId, examId)

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/versions/{versionId}")
    fun getVersionOfSpecificExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable versionId: Int
    ) = courseService.getVersionOfSpecificExam(termId, courseId, examId, versionId)

    // ----------------------------
    // Exam Report Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/reports")
    fun getAllReportsOnExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.getAllReportsOnExamOnSpecificTermOfCourse(termId, courseId, examId)

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun getSpecificReportOnExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportOnExamOnSpecificTermOfCourse(termId, courseId, examId, reportId)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports")
    fun addReportToExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @RequestBody inputExamReport: ExamReportInputModel,
            principal: Principal
    ) = courseService.addReportToExamOnCourseInTerm(termId, courseId, examId, inputExamReport, principal)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}/vote")
    fun voteOnReportToExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnReportedExamOnCourseInTerm(termId, courseId, examId, reportId, inputVote, principal)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun updateReportedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = courseService.updateReportedExam(examId, reportId, courseId, termId, principal)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun deleteReportOnExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = courseService.deleteReportOnExam(termId, courseId, examId, reportId, principal)

    // ----------------------------
    // Exam Stage Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/exams/stage")
    fun getStageEntriesFromExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId)

    @GetMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}")
    fun getStageEntryFromExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)


    @PostMapping("/{courseId}/terms/{termId}/exams/stage")
    fun createStagingExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestParam sheet: MultipartFile,
            examInputModel: ExamInputModel,
            principal: Principal
    ) = courseService.createStagingExam(courseId, termId, examInputModel, sheet, principal)

    @PostMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}")
    fun createExamFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = courseService.createExamFromStaged(courseId, termId, stageId, principal)

    @PostMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}/vote")
    fun voteOnStagedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnStagedExam(termId, courseId, stageId, inputVote, principal)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}")
    fun deleteStagedExamOfCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = courseService.deleteSpecificStagedExamOfCourseInTerm(courseId, termId, stageId, principal)

    // ----- WORK ASSIGNMENT ------

    // ----------------------------
    // Work Assignment Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/work-assignments")
    fun getAllWorkAssignmentsFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}")
    fun getSpecificWorkAssignmentFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId, courseId, termId)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments")
    fun createWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestParam sheet: MultipartFile,
            workAssignmentInputModel: WorkAssignmentInputModel,
            principal: Principal
    ) = courseService.createWorkAssignmentOnCourseInTerm(courseId, termId, workAssignmentInputModel, sheet, principal)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/vote")
    fun voteOnWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnWorkAssignment(termId, courseId, workAssignmentId, inputVote, principal)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}")
    fun deleteSpecificWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            principal: Principal
    ) = courseService.deleteSpecificWorkAssignmentOfCourseInTerm(courseId, termId, workAssignmentId, principal)

    // ----------------------------
    // Work Assignment Version Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions")
    fun getAllVersionsOfSpecificWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.getAllVersionsOfSpecificWorkAssignment(termId, courseId, workAssignmentId)

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions/{versionId}")
    fun getVersionOfSpecificWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable versionId: Int
    ) = courseService.getVersionOfSpecificWorkAssignment(termId, courseId, workAssignmentId, versionId)

    // ----------------------------
    // Work Assignment Report Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports")
    fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId, termId, workAssignmentId)

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}")
    fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(termId, courseId, workAssignmentId, reportId)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports")
    fun addReportToWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @RequestBody inputWorkAssignmentReport: WorkAssignmentReportInputModel,
            principal: Principal
    ) = courseService.addReportToWorkAssignmentOnCourseInTerm(termId, courseId, workAssignmentId, inputWorkAssignmentReport, principal)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}/vote")
    fun voteOnReportToWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnReportedWorkAssignmentOnCourseInTerm(termId, courseId, workAssignmentId, reportId, inputVote, principal)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}")
    fun updateReportedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = courseService.updateWorkAssignmentBasedOnReport(workAssignmentId, reportId, courseId, termId, principal)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}")
    fun deleteReportOnWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = courseService.deleteReportOnWorkAssignment(termId, courseId, workAssignmentId, reportId, principal)

    // ----------------------------
    // Work Assignment Stage Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/stage")
    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId)

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/stage/{stageId}")
    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/stage")
    fun createStagingWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestParam sheet: MultipartFile,
            workAssignmentInputModel: WorkAssignmentInputModel,
            principal: Principal
    ) = courseService.createStagingWorkAssignment(sheet, courseId, termId, workAssignmentInputModel, principal)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/stage/{stageId}")
    fun createWorkAssignmentFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = courseService.createWorkAssignmentFromStaged(courseId, termId, stageId, principal)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/stage/{stageId}/vote")
    fun voteOnStagedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = courseService.voteOnStagedWorkAssignment(termId, courseId, stageId, inputVote, principal)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/stage/{stageId}")
    fun deleteSpecificStagedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = courseService.deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId, termId, stageId, principal)

}