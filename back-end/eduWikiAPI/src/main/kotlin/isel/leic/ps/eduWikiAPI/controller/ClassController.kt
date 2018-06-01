package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LessonReportInputModel
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/classes")
class ClassController {

    @Autowired
    lateinit var classService: ClassService

    // ---------- COURSE ----------

    // ----------------------------
    // Class Endpoints
    // ----------------------------

    @GetMapping
    fun getAllClasses() = classService.getAllClasses()

    @GetMapping("/{classId}")
    fun getSpecificClass(@PathVariable classId: Int) = classService.getSpecificClass(classId)

    @PostMapping
    fun createCourse(@RequestBody input: ClassInputModel) = classService.createClass(input)

    @PostMapping("/{classId}/vote")
    fun voteOnClass(
            @PathVariable classId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnClass(classId, vote)

    @PatchMapping("/{classId}")
    fun partialUpdateOnClass(
            @PathVariable classId: Int,
            @RequestBody input: ClassInputModel
    ) = classService.updateClass(classId, input)

    @DeleteMapping("/{classId}")
    fun deleteSpecificClass(@PathVariable classId: Int) = classService.deleteSpecificClass(classId)

    // ----------------------------
    // Course Report Endpoints
    // ----------------------------

    @GetMapping("/{classId}/reports")
    fun getClassReports(@PathVariable classId: Int) = classService.getReportsOfClass(classId)

    @GetMapping("/{classId}/reports/{reportId}")
    fun getSpecificClassReport(
            @PathVariable classId: Int,
            @PathVariable reportId: Int
    ) = classService.getSpecificReportOfClass(classId, reportId)

    @PostMapping("/{classId}/reports")
    fun reportClass(
            @PathVariable classId: Int,
            @RequestBody report: ClassReportInputModel
    ) = classService.reportClass(classId, report)

    @PostMapping("/{classId}/reports/{reportId}/vote")
    fun voteOnReportedClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnReportClass(classId, reportId, vote)

    @PostMapping("/{classId}/reports/{reportId}")
    fun updateReportedClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int
    ) = classService.updateClassFromReport(classId, reportId)

    @DeleteMapping("/{classId}/reports")
    fun deleteAllReportsInClass(@PathVariable classId: Int) = classService.deleteAllReportsInClass(classId)

    @DeleteMapping("/{classId}/reports/{reportId}")
    fun deleteReportInClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int
    ) = classService.deleteSpecificReportInClass(classId, reportId)

    // ----------------------------
    // Course Stage Endpoints
    // ----------------------------

    @GetMapping("/stage")
    fun getAllClassStageEntries() = classService.getAllStagedClasses()

    @GetMapping("/stage/{stageId}")
    fun getClassSpecificStageEntry(@PathVariable stageId: Int) = classService.getSpecificStagedClass(stageId)

    @PostMapping("/stage")
    fun createStagingClass(@RequestBody input: ClassInputModel) = NotImplementedError()

    @PostMapping("/stage/{stageId}")
    fun createClassFromStaged(@PathVariable stageId: Int) = NotImplementedError()

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PatchMapping("/stage/{stageId}")
    fun partialUpdateOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody input: ClassInputModel
    ) = NotImplementedError()

    @DeleteMapping("/stage")
    fun deleteAllStagedClasses() = NotImplementedError()

    @DeleteMapping("/stage/{stageId}")
    fun deleteStagedClass(@PathVariable stageId: Int) = NotImplementedError()

    // ----------------------------
    // Course Version Endpoints
    // ----------------------------

    @GetMapping("/{classId}/versions")
    fun getVersionsOfClass(@PathVariable classId: Int) = NotImplementedError()

    @GetMapping("/{classId}/versions/{versionId}")
    fun getSpecificVersionOfClass(@PathVariable classId: Int, @PathVariable versionId: Int) = NotImplementedError()

    @DeleteMapping("/{courseId}/versions")
    fun deleteAllVersionsOfClass(@PathVariable courseId: Int) = NotImplementedError()

    @DeleteMapping("/{courseId}/versions/{version}")
    fun deleteSpecificVersionOfClass(
            @PathVariable courseId: Int,
            @PathVariable version: Int
    ) = NotImplementedError()

    // ----------------------------
    // Course Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses")
    fun getCoursesOfClass(@PathVariable classId: Int) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}")
    fun getSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/vote")
    fun voteOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}")
    fun deleteCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    // ----------------------------
    // Course Class Report Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/reports")
    fun getReportsOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun getReportOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/reports")
    fun createReportOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody courseClassReportInputModel: CourseClassReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun updateCourseInClassFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/reports/{reportId}/vote")
    fun voteOnReportOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun deleteCourseReportInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()


    // ----------------------------
    // Course Class Stage Endpoints
    // ----------------------------

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

    @PostMapping("/{classId}/courses/{courseId}/stage/{stageId}")
    fun createCourseOnClassFromStage(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/stage/{stageId}/vote")
    fun voteOnStagedCourseOnClassFromStage(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/stage")
    fun deleteStageEntriesOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/stage/{stageId}")
    fun deleteSpecificStageEntryOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    // ----------------------------
    // Course Class Version Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/versions")
    fun getVersionsOfCourseClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/versions/{versionId}")
    fun getSpecificVersionOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable versionId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/versions")
    fun deleteAllVersionsOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/versions/{version}")
    fun deleteSpecificVersionOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable version: Int
    ) = NotImplementedError()


    // ----------------------------
    // Lessons in Class Endpoints
    // ----------------------------

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

    @PostMapping("/{classId}/courses/{courseId}/lessons")
    fun createLessonOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody lesson: LessonInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/vote")
    fun voteOnLessonOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    // ----------------------------
    // Lessons in Report Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/reports")
    fun getReportsFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/reports/{reportId}")
    fun getReportFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/reports")
    fun deleteReportOnAllLessonOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/reports/{reportId}")
    fun deleteReportOnLessonOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/reports")
    fun createReportOnLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @RequestBody report: LessonReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/reports/{reportId}/vote")
    fun voteOnReportFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int,
            @PathVariable vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/reports/{reportId}")
    fun updateLessonFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    // ----------------------------
    // Lessons in Stage Class Endpoints
    // ----------------------------

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

    @PostMapping("/{classId}/courses/{courseId}/lessons/stage")
    fun createStagedLessonOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody lesson: LessonInputModel
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/stage/{stageId}")
    fun deleteStageEntryFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/stage/{stageId}/vote")
    fun voteOnStageEntryFromLessonOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/lessons/stage/{stageId}")
    fun createLessonFromStage(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    // ----------------------------
    // Lessons Versions in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/versions")
    fun getVersionsOfLessonsOfCourseClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/versions/{versionId}")
    fun getSpecificVersionOfLessonCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable versionId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/versions")
    fun deleteAllVersionsOfLessonCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/lessons/{lessonId}/versions/{versionId}")
    fun deleteSpecificVersionOfLessonCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int,
            @PathVariable version: Int
    ) = NotImplementedError()

    // ----------------------------
    // Homeworks in Class Endpoints
    // ----------------------------

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

    @PostMapping("/{classId}/courses/{courseId}/homeworks")
    fun createHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody homeworkInputModel: HomeworkInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/vote")
    fun voteOnHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()


    // ----------------------------
    // Homeworks Staged in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun getAllStagedHomeWorksFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun getSpecificStagedHomeWorkFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/stage")
    fun createStagedHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody homeworkInputModel: HomeworkInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun createHomeWorkFromStaged(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}/vote")
    fun voteOnStagedHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
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

    // ----------------------------
    // Homeworks Reports in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun getAllReportsOfHomeWorksFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun getSpecificReportOfHomeWorkFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun createReportOnHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody homeworkReportInputModel: HomeworkReportInputModel
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun updateHomeworkFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}/vote")
    fun voteOnReportOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun deleteAllReportsOnHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun deleteSpecificReportOnHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int
    ) = NotImplementedError()

    // ----------------------------
    // Homeworks Version in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions")
    fun getVersionsOfHomeworkOfCourseClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lessonId: Int
    ) = NotImplementedError()

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions/{versionId}")
    fun getSpecificVersionOfHomeworkCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable versionId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions")
    fun deleteAllVersionsOfHomeworkCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = NotImplementedError()

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions/{versionId}")
    fun deleteSpecificVersionOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable version: Int
    ) = NotImplementedError()
}