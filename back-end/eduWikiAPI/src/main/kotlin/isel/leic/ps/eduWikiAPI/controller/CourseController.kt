package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController {

    @Autowired
    lateinit var courseService: CourseService

    /**
     * All GET Routes
     */

    @GetMapping
    fun getAllCourses(): List<Course> {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    fun getSpecificCourse(@PathVariable courseId: Int) : Course {
        return courseService.getSpecificCourse(courseId)
    }

    @GetMapping("/{courseId}/report")
    fun getCourseReports(@PathVariable courseId: Int) : List<CourseReport>  {
        return courseService.getAllReportsOnCourse(courseId)
    }

    @GetMapping("/{courseId}/report/{reportId}")
    fun getSpecificReportOfCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportOfCourse(courseId, reportId)

    @GetMapping("/stage")
    fun getAllCourseStageEntries() = courseService.getAllCourseStageEntries()

    @GetMapping("/stage/{stageId}")
    fun getCourseSpecificStageEntry(@PathVariable stageId: Int) = courseService.getCourseSpecificStageEntry(stageId)

    @GetMapping("/{courseId}/terms")
    fun getTermsOfCourse(@PathVariable courseId: Int) = courseService.getTermsOfCourse(courseId)

    @GetMapping("/{courseId}/terms/{termId}")
    fun getSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getSpecificTermOfCourse(courseId, termId)

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

    @GetMapping("/{courseId}/terms/{termId}/workAssignments")
    fun getAllWorkAssignmentsFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}")
    fun getSpecificWorkAssignmentFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/stage")
    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}")
    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports")
    fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId, termId, workAssignmentId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}")
    fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termInt: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId, reportId)

    @GetMapping("/{courseId}/terms/{termId}/classes")
    fun getClassesOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getClassesOnSpecificTermOfCourse(courseId, termId)

    /**
     * All POST Routes
     */

    @PostMapping()
    fun createCourse(@RequestBody inputCourse: CourseInputModel) = courseService.createCourse(inputCourse)

    @PostMapping("/{courseId}/vote")
    fun voteOnCourse(
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnCourse(courseId, inputVote)

    @PostMapping("/{courseId}/reports")
    fun reportCourse( //TODO fazer report do que? todos os campos?
            @PathVariable courseId: Int,
            @RequestBody inputCourseReport: CourseReportInputModel
    ) = courseService.reportCourse(courseId, inputCourseReport)

    @PostMapping("/{courseId}/reports/{reportId}/vote")
    fun voteOnReportedCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnReportedCourse(reportId, inputVote)

    @PostMapping("/{courseId}/reports/{reportId}")
    fun updateReportedCourse( //TODO fazer report de que campos?
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = courseService.updateReportedCourse(courseId, reportId)

    @PostMapping("/stage")
    fun createStagingCourse(@RequestBody inputCourse: CourseInputModel) = courseService.createStagingCourse(inputCourse)

    @PostMapping("/stage/{stageId}")
    fun createCourseFromStaged(@PathVariable stageId: Int) = courseService.createCourseFromStaged(stageId)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedCourse(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnStagedCourse(stageId, inputVote)

    @PostMapping("/{courseId}/terms/{termId}/exams")
    fun createExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody exam: ExamInputModel
    ) = courseService.createExamOnCourseInTerm(courseId, termId, exam)

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
    ) = courseService.voteOnReportToExamOnCourseInTerm(reportId, inputVote)

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun updateReportedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = courseService.updateReportedExam(examId, reportId)

    @PostMapping("/{courseId}/terms/{termId}/exams/stage")
    fun createStagingExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody inputExam: ExamInputModel
    ) = courseService.createStagingExam(courseId, termId, inputExam)

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

    @PostMapping("/{courseId}/terms/{termId}/workAssignments")
    fun createWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody inputWorkAssignment: WorkAssignmentInputModel
    ) = courseService.createWorkAssignmentOnCourseInTerm(courseId, termId, inputWorkAssignment)

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports")
    fun addReportToWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @RequestBody inputWorkAssignmentReport: WorkAssignmentReportInputModel
    ) = courseService.addReportToWorkAssignmentOnCourseInTerm(workAssignmentId, inputWorkAssignmentReport)

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}/vote")
    fun voteOnReportToWorkAssignmentOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnReportToWorkAssignmentOnCourseInTerm(reportId, inputVote)

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}")
    fun updateReportedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int
    ) = courseService.updateReportedWorkAssignment(workAssignmentId, reportId)

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/stage")
    fun createStagingWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody workAssignment: WorkAssignmentInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}")
    fun createWorkAssignmentFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}/vote")
    fun voteOnStagedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    /**
     * ALL PATCH Routes
     */
    @PatchMapping("/{courseId}")
    fun partialUpdateOnCourse(
            @PathVariable courseId: Int,
            @RequestBody course: CourseInputModel
    ) = NotImplementedError()

    @PatchMapping("/stage/{stageId}")
    fun partialUpdateOnStagedCourse(
            @PathVariable stageId: Int,
            @RequestBody course: CourseInputModel
    ) = NotImplementedError()

    /**
     * ALL DELETE Routes
     */
    @DeleteMapping
    fun deleteAllCourses() = NotImplementedError()

    @DeleteMapping("/{courseId}")
    fun deleteSpecificCourse(@PathVariable courseId: Int) = NotImplementedError()

    @DeleteMapping("/stage")
    fun deleteAllStagedCourses() = NotImplementedError()

    @DeleteMapping("/stage/{stageId}")
    fun deleteStagedCourse(@PathVariable stageId: Int) = NotImplementedError()

    @DeleteMapping("/{courseId}/reports")
    fun deleteAllReportsInCourse(@PathVariable courseId: Int) = NotImplementedError()

    @DeleteMapping("/{courseId}/reports/{reportId}")
    fun deleteReportInCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    //exams
    @DeleteMapping("/{courseId}/terms/{termId}/exams")
    fun deleteAllExams(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}")
    fun deleteExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/exams/stage")
    fun deleteAllStagedExams(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}")
    fun deleteStagedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/reports")
    fun deleteAllReportsInExams(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun deleteReportInExam(
            @PathVariable courseId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments")
    fun deleteAllWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}")
    fun deleteWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/stage")
    fun deleteAllStagedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}")
    fun deleteStagedWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentsId}/reports")
    fun deleteAllReportsInWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}")
    fun deleteReportInWorkAssignment(
            @PathVariable courseId: Int,
            @PathVariable workAssignmentId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()
}