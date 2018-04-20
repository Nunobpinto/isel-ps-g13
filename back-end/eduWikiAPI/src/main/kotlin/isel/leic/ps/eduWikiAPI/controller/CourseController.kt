package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.inputModel.*
import isel.leic.ps.eduWikiAPI.service.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController {

    //TODO implementation of Course Controller methods
    @Autowired
    lateinit var courseService: CourseService

    /**
     * All GET Routes
     */
    @GetMapping
    fun getAllCourses() = NotImplementedError()

    @GetMapping("/{courseId}")
    fun getSpecificCourse(@PathVariable courseId: Int) = NotImplementedError()

    @GetMapping("/{courseId}/report")
    fun getCourseReports(@PathVariable courseId: Int) = NotImplementedError()

    @GetMapping("/{courseId}/report/{reportId}")
    fun getSpecificCourseReport(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @GetMapping("/stage")
    fun getAllCourseStageEntries() = NotImplementedError()

    @GetMapping("/stage/{stageId}")
    fun getCourseSpecificStageEntry() = NotImplementedError()

    @GetMapping("/{courseId}/terms")
    fun getTermsOfCourse(@PathVariable courseId: Int) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}")
    fun getSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/exams")
    fun getExamsFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}")
    fun getSpecificExamFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/stage")
    fun getStageEntriesFromExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/stage/{stageId}")
    fun getStageEntryFromExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/report")
    fun getReportsFromExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/exams/{examId}/report/{reportId}")
    fun getReportFromExamOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/workitems")
    fun getWorkItemsFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/workitems/{workItemId}")
    fun getSpecificWorkItemFromSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/exams/{workItemId}/stage")
    fun getStageEntriesFromWorkItemOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termInt: Int,
            @PathVariable workItemId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/workitem/{workItemId}/stage/{stageId}")
    fun getStageEntryFromWorkItemOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/workitem/{workItemId}/report")
    fun getReportsFromWorkUnitOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termInt: Int,
            @PathVariable examInt: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/workitem/{workItemId}/report/{reportId}")
    fun getReportFromWorkItemOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termInt: Int,
            @PathVariable workItemId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @GetMapping("/{courseId}/terms/{termId}/classes")
    fun getClassesOnSpecificTermOfCourse(
            @PathVariable courseId: Int,
            @PathVariable termInt: Int
    ) = NotImplementedError()

    /**
     * All POST Routes
     */
    @PostMapping()
    fun createCourse(@RequestBody input: CourseInputModel) = NotImplementedError()

    @PostMapping("/{courseId}/vote")
    fun voteOnCourse(
            @PathVariable courseId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/report")
    fun reportCourse(
            @PathVariable courseId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/report/{reportId}/vote")
    fun voteOnReportedCourse(
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/stage")
    fun createStagingCourse(@RequestBody course: CourseInputModel) = NotImplementedError()

    @PostMapping("/stage/{stageId}")
    fun createCourseFromStaged(@PathVariable stageId: Int) = NotImplementedError()

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedCourse(
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    //exams
    @PostMapping("/{courseId}/terms/{termId}/exams")
    fun createExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody exam: ExamInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/report")
    fun addReportToExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/exams/{examId}/report/{reportId}/vote")
    fun voteOnReportToExamOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
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
    @PostMapping("/{courseId}/terms/{termId}/workitems")
    fun createWorkItemOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody workItem: WorkItemInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workitems/{workItemId}/report")
    fun addReportToWorkItemOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workitems/{workItemId}/report/{reportId}/vote")
    fun voteOnReportToWorkItemOnCourseInTerm(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workitems/stage")
    fun createStagingWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @RequestBody workItem: WorkItemInputModel
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workitems/stage/{stageId}")
    fun createWorkItemFromStaged(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{courseId}/terms/{termId}/workitems/stage/{stageId}/vote")
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

    @DeleteMapping("/{courseId}/report")
    fun deleteAllReportsInCourse(@PathVariable courseId: Int) = NotImplementedError()

    @DeleteMapping("/{courseId}/report/{reportId}")
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

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/report")
    fun deleteAllReportsInExams(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable examId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/exams/{examId}/report/{reportId}")
    fun deleteReportInExam(
            @PathVariable courseId: Int,
            @PathVariable examId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    //workitems
    @DeleteMapping("/{courseId}/terms/{termId}/workitems")
    fun deleteAllWorkItems(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workitems/{workItemId}")
    fun deleteWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workitems/stage")
    fun deleteAllStagedWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workitems/stage/{stageId}")
    fun deleteStagedWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workitems/{workItemId}/report")
    fun deleteAllReportsInWorkItem(
            @PathVariable courseId: Int,
            @PathVariable termId: Int,
            @PathVariable workItemId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{courseId}/terms/{termId}/workitems/{workItemId}/report/{reportId}")
    fun deleteReportInWorkItem(
            @PathVariable courseId: Int,
            @PathVariable workItemId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()
}