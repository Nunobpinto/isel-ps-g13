package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.inputModel.*
import isel.leic.ps.eduWikiAPI.service.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/classes")
class ClassController {

    //TODO implementation of Class Controller methods
    //TODO should report have a relational to specific course or all courses
    //TODO presentations
    @Autowired
    lateinit var classService: ClassService

    /**
     * All GET Routes
     */
    @GetMapping
    fun getAllClasses() = NotImplementedError()

    @GetMapping("/{classId}")
    fun getSpecificClass(@PathVariable classId: Int) = NotImplementedError()

    @GetMapping("/{classId}/report")
    fun getClassReports(@PathVariable classId: Int) = NotImplementedError()

    @GetMapping("/{classId}/report/{reportId}")
    fun getSpecificClassReport(
            @PathVariable classId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @GetMapping("/stage")
    fun getAllClassStageEntries() = NotImplementedError()

    @GetMapping("/stage/{stageId}")
    fun getClassSpecificStageEntry() = NotImplementedError()

    @GetMapping("/{classId}/courses")
    fun getCoursesOfClass(@PathVariable classId: Int) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}")
    fun getSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/report")
    fun getReportsOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/report/{reportId}")
    fun getReportOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/stage")
    fun getStageEntriesOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/stage/{stageId}")
    fun getStageEntriesOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    //lessons
    @GetMapping("/{classId}/courses/{courseId}/lessons")
    fun getLessonsFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/lessons/{lessonId}")
    fun getSpecificLessonFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/lessons/stage")
    fun getStageEntriesFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/lessons/stage/{stageId}")
    fun getStageEntryFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/report")
    fun getReportsFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/report/{reportId}")
    fun getReportFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    //homeworks
    @GetMapping("/{classId}/courses/{courseId}/homeworks")
    fun getHomeWorksFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeWorkId}")
    fun getSpecificHomeWorkFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun getStageEntriesFromHomeWorkOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun getStageEntryFromHomeWorkOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeWorkId}/report")
    fun getReportsFromHomeWorkOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeWorkId}/report/{reportId}")
    fun getReportFromHomeWorkOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    /**
     * All POST Routes
     */
    @PostMapping()
    fun createCourse(@RequestBody input: ClassInputModel) = NotImplementedError()

    @PostMapping("/{classId}/vote")
    fun voteOnClass(
            @PathVariable classId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/report")
    fun reportClass(
            @PathVariable classId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/report/{reportId}/vote")
    fun voteOnReportedClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/report/{reportId}")
    fun updateReportedClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/stage")
    fun createStagingClass(@RequestBody input: ClassInputModel) = NotImplementedError()

    @PostMapping("/stage/{stageId}")
    fun createClassFromStaged(@PathVariable stageId: Int) = NotImplementedError()

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    //lessons
    @PostMapping("/{classId}/courses/{courseId}/lessons")
    fun createLessonOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody lesson: LessonInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/report")
    fun addReportToLessonOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/report/{reportId}/vote")
    fun voteOnReportToLessonOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/report/{reportId}")
    fun updateReportedLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/stage")
    fun createStagingLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody exam: ExamInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/stage/{stageId}")
    fun createLessonFromStaged(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/stage/{stageId}/vote")
    fun voteOnStagedLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    //workItems
    @PostMapping("/{classId}/courses/{courseId}/homeworks")
    fun createHomeWorknOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody homeWork: HomeWorkInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeWorkId}/report")
    fun addReportToHomeWorkOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @RequestBody report: ReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeWorkId}/report/{reportId}/vote")
    fun voteOnReportToHomeWorkOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeWorkId}/report/{reportId}")
    fun updateReportedHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun createStagingHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody homeWork: HomeWorkInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun createHomeWorkFromStaged(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}/vote")
    fun voteOnStagedHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    /**
     * ALL PATCH Routes
     */
    @PatchMapping("/{classId}")
    fun partialUpdateOnClass(
            @PathVariable classId: Int,
            @RequestBody input: ClassInputModel
    ) = NotImplementedError()

    @PatchMapping("/stage/{stageId}")
    fun partialUpdateOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody input: ClassInputModel
    ) = NotImplementedError()

    /**
     * ALL DELETE Routes
     */
    @DeleteMapping
    fun deleteAllClasses() = NotImplementedError()

    @DeleteMapping("/{classId}")
    fun deleteSpecificClass(@PathVariable classId: Int) = NotImplementedError()

    @DeleteMapping("/stage")
    fun deleteAllStagedClasses() = NotImplementedError()

    @DeleteMapping("/stage/{stageId}")
    fun deleteStagedClass(@PathVariable stageId: Int) = NotImplementedError()

    @DeleteMapping("/{classId}/report")
    fun deleteAllReportsInClass(@PathVariable courseId: Int) = NotImplementedError()

    @DeleteMapping("/{classId}/report/{reportId}")
    fun deleteReportInClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    //lessons
    @DeleteMapping("/{classId}/courses/{courseId}/lessons")
    fun deleteAllLessons(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/{lessonId}")
    fun deleteLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/stage")
    fun deleteAllStagedLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/stage/{stageId}")
    fun deleteStagedLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/report")
    fun deleteAllReportsInLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/report/{reportId}")
    fun deleteReportInLesson(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    //homeWorks
    @DeleteMapping("/{classId}/courses/{courseId}/homeworks")
    fun deleteAllHomeWorks(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}")
    fun deleteHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun deleteAllStagedHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun deleteStagedHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/report")
    fun deleteAllReportsInHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/report/{reportId}")
    fun deleteReportInHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()
}