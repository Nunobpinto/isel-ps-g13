package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
    fun createCourse(@RequestBody inputCourse: CourseInputModel) = courseService.createCourse(inputCourse)

    @PostMapping("/{courseId}/vote")
    fun voteOnCourse(
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnCourse(courseId, inputVote)

    @PatchMapping("/{courseId}")
    fun partialUpdateOnCourse(
            @PathVariable courseId: Int,
            @RequestBody inputCourse: CourseInputModel
    ) = courseService.partialUpdateOnCourse(courseId, inputCourse)

    @DeleteMapping("/{courseId}")
    fun deleteSpecificCourse(@PathVariable courseId: Int) = courseService.deleteSpecificCourse(courseId)

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

    @DeleteMapping("/{courseId}/versions")
    fun deleteAllVersionsOfCourse(@PathVariable courseId: Int) = courseService.deleteAllVersionsOfCourse(courseId)

    @DeleteMapping("/{courseId}/versions/{version}")
    fun deleteVersionOfCourse(
            @PathVariable courseId: Int,
            @PathVariable version: Int
    ) = courseService.deleteVersionOfCourse(courseId, version)

    // ----------------------------
    // Course Report Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/reports")
    fun getAllReportsOnCourse(@PathVariable courseId: Int): List<CourseReport> = courseService.getAllReportsOnCourse(courseId)

    @GetMapping("/{courseId}/reports/{reportId}")
    fun getSpecificReportOfCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportOfCourse(courseId, reportId)

    @PostMapping("/{courseId}/reports")
    fun reportCourse(
            @PathVariable courseId: Int,
            @RequestBody inputCourseReport: CourseReportInputModel
    ) = courseService.reportCourse(courseId, inputCourseReport)

    @PostMapping("/{courseId}/reports/{reportId}/vote")
    fun voteOnReportOfCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnReportedCourse(reportId, inputVote)

    @PostMapping("/{courseId}/reports/{reportId}")
    fun updateReportedCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = courseService.updateReportedCourse(courseId, reportId)

    @DeleteMapping("/{courseId}/reports")
    fun deleteAllReportsOnCourse(@PathVariable courseId: Int) = courseService.deleteAllReportsOnCourse(courseId)

    @DeleteMapping("/{courseId}/reports/{reportId}")
    fun deleteReportOnCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = courseService.deleteReportOnCourse(courseId, reportId)

    // ----------------------------
    // Course Stage Endpoints
    // ----------------------------

    @GetMapping("/stage")
    fun getAllCourseStageEntries() = courseService.getAllCourseStageEntries()

    @GetMapping("/stage/{stageId}")
    fun getCourseSpecificStageEntry(@PathVariable stageId: Int) = courseService.getCourseSpecificStageEntry(stageId)

    @PostMapping("/stage")
    fun createStagingCourse(@RequestBody inputCourse: CourseInputModel) = courseService.createStagingCourse(inputCourse)

    @PostMapping("/stage/{stageId}")
    fun createCourseFromStaged(@PathVariable stageId: Int) = courseService.createCourseFromStaged(stageId)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedCourse(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnStagedCourse(stageId, inputVote)

    @DeleteMapping("/stage")
    fun deleteAllStagedCourses() = courseService.deleteAllStagedCourses()

    @DeleteMapping("/stage/{stageId}")
    fun deleteSpecificStagedCourse(@PathVariable stageId: Int) = courseService.deleteSpecificStagedCourse(stageId)

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
            @RequestParam sheet: MultipartFile,
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            examInputModel: ExamInputModel //TODO check if @RequestBody does work too
    ) = courseService.createExamOnCourseInTerm(sheet, courseId, termId, examInputModel)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/vote")
    fun voteOnExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId : Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnExam(examId, inputVote)


    @DeleteMapping("/{courseId}/terms/{termId}/exams")
    fun deleteAllExamsOfCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.deleteAllExamsOfCourseInTerm(courseId, termId)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}")
    fun deleteSpecificExamOfCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.deleteSpecificExamOfCourseInTerm(courseId, termId, examId)

    // ----------------------------
    // Exam Version Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/versions")
    fun getAllVersionsOfSpecificExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.getAllVersionsOfSpecificExam(examId)

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/versions/{versionId}")
    fun getVersionOfSpecificExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable versionId: Int
    ) = courseService.getVersionOfSpecificExam(examId, versionId)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/versions")
    fun deleteAllVersionsOfExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.deleteAllVersionsOfExam(examId)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/versions/{version}")
    fun deleteVersionOfExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable version: Int
    ) = courseService.deleteVersionOfExam(examId, version)

    // ----------------------------
    // Exam Report Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/reports")
    fun getAllReportsOnExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.getAllReportsOnExamOnSpecificTermOfCourse(examId)

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun getSpecificReportOnExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportOnExamOnSpecificTermOfCourse(reportId)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports")
    fun addReportToExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @RequestBody inputExamReport: ExamReportInputModel
    ) = courseService.addReportToExamOnCourseInTerm(examId, inputExamReport)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}/vote")
    fun voteOnReportToExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnReportedExamOnCourseInTerm(reportId, inputVote)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun updateReportedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = courseService.updateReportedExam(examId, reportId, courseId, termId)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/reports")
    fun deleteAllReportsOnExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = courseService.deleteAllReportsOnExam(examId)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun deleteReportOnExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = courseService.deleteReportOnExam(examId, reportId)

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
            @RequestParam sheet: MultipartFile,
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody examInputModel: ExamInputModel
    ) = courseService.createStagingExam(sheet, courseId, termId, examInputModel)

    @PostMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}")
    fun createExamFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.createExamFromStaged(courseId, termId, stageId)

    @PostMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}/vote")
    fun voteOnStagedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnStagedExam(stageId, inputVote)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/stage")
    fun deleteAllStagedExamsOfCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.deleteAllStagedExamsOfCourseInTerm(courseId, termId)

    @DeleteMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}")
    fun deleteStagedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.deleteStagedExam(stageId)

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
            @RequestParam sheet: MultipartFile,
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody workAssignmentInputModel: WorkAssignmentInputModel
    ) = courseService.createWorkAssignmentOnCourseInTerm(sheet, courseId, termId, workAssignmentInputModel)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/vote")
    fun voteOnWorkAssignment(@PathVariable courseId: Int,
                             @PathVariable termId: Int,
                             @PathVariable workAssignmentId: Int,
                             @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnWorkAssignment(workAssignmentId, inputVote)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments")
    fun deleteAllWorkAssignmentsOfCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.deleteAllWorkAssignmentsOfCourseInTerm(courseId, termId)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}")
    fun deleteSpecificWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.deleteSpecificWorkAssignment(workAssignmentId)

    // ----------------------------
    // Work Assignment Version Endpoints
    // ----------------------------

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions")
    fun getAllVersionsOfSpecificWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.getAllVersionsOfSpecificWorkAssignment(workAssignmentId)

    @GetMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions/{version}")
    fun getVersionOfSpecificWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable version: Int
    ) = courseService.getVersionOfSpecificWorkAssignment(workAssignmentId, version)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions")
    fun deleteAllVersionsOfWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.deleteAllVersionsOfWorkAssignment(workAssignmentId)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions/{version}")
    fun deleteVersionOfWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable version: Int
    ) = courseService.deleteVersionOfWorkAssignment(workAssignmentId, version)

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
    ) = courseService.getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId, reportId)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports")
    fun addReportToWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @RequestBody inputWorkAssignmentReport: WorkAssignmentReportInputModel
    ) = courseService.addReportToWorkAssignmentOnCourseInTerm(workAssignmentId, inputWorkAssignmentReport)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}/vote")
    fun voteOnReportToWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnReportedWorkAssignmentOnCourseInTerm(reportId, inputVote)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}")
    fun updateReportedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int
    ) = courseService.updateWorkAssignmentBasedOnReport(workAssignmentId, reportId, courseId, termId)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentsId}/reports")
    fun deleteAllReportsOnWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.deleteAllReportsOnWorkAssignment(workAssignmentId)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}")
    fun deleteReportOnWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int
    ) = courseService.deleteReportOnWorkAssignment(workAssignmentId, reportId)

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
            @RequestBody inputWorkAssignment: WorkAssignmentInputModel
    ) = courseService.createStagingWorkAssignment(courseId, termId, inputWorkAssignment)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/stage/{stageId}")
    fun createWorkAssignmentFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.createWorkAssignmentFromStaged(courseId, termId, stageId)

    @PostMapping("/{courseId}/terms/{termId}/work-assignments/stage/{stageId}/vote")
    fun voteOnStagedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnStagedWorkAssignment(stageId, inputVote)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/stage")
    fun deleteAllStagedWorkAssignmentsOfCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId, termId)

    @DeleteMapping("/{courseId}/terms/{termId}/work-assignments/stage/{stageId}")
    fun deleteSpecificStagedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.deleteSpecificStagedWorkAssignment(stageId)

}