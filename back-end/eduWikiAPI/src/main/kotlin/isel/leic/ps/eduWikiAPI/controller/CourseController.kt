package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
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
    fun getStageEntriesFromWorkItemOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = courseService.getStageEntriesFromWorkItemOnSpecificTermOfCourse(courseId, termId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}")
    fun getStageEntryFromWorkItemOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = courseService.getStageEntryFromWorkItemOnSpecificTermOfCourse(courseId, termId, stageId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports")
    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workAssignmentId: Int
    ) = courseService.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId)

    @GetMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}")
    fun getSpecificReportFromWorkItemOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termInt: Int,
            @PathVariable workItemId: Int,
            @PathVariable reportId: Int
    ) = courseService.getSpecificReportFromWorkItemOnSpecificTermOfCourse(reportId)

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
    fun reportCourse( //TODO
            @PathVariable courseId: Int,
            @RequestBody inputReportCourse: CourseReportInputModel
    ) = courseService.reportCourse(courseId, inputReportCourse)

    @PostMapping("/{courseId}/reports/{reportId}/vote")
    fun voteOnReportedCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = courseService.voteOnReportedCourse(reportId, inputVote)

    @PostMapping("/{courseId}/reports/{reportId}")
    fun updateReportedCourse( //TODO
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

    //exams
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
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}/vote")
    fun voteOnReportToExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}")
    fun updateReportedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/exams/stage")
    fun createStagingExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody exam: ExamInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}")
    fun createExamFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/exams/stage/{stageId}/vote")
    fun voteOnStagedExam(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    //workItems
    @PostMapping("/{courseId}/terms/{termId}/workAssignments")
    fun createWorkItemOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody workItem: WorkItemInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports")
    fun addReportToWorkItemOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}/vote")
    fun voteOnReportToWorkItemOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}")
    fun updateReportedWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/stage")
    fun createStagingWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody workItem: WorkItemInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}")
    fun createWorkItemFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}/vote")
    fun voteOnStagedWorkItem(
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

    //workitems
    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments")
    fun deleteAllWorkItems(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}")
    fun deleteWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/stage")
    fun deleteAllStagedWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/stage/{stageId}")
    fun deleteStagedWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentsId}/reports")
    fun deleteAllReportsInWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workAssignments/{workAssignmentId}/reports/{reportId}")
    fun deleteReportInWorkItem(
            @PathVariable courseId: Int,
            @PathVariable workItemId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()
}