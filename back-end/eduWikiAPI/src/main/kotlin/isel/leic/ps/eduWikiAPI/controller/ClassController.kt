package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
    fun createClass(@RequestBody input: ClassInputModel) = classService.createClass(input)

    @PostMapping("/{classId}/vote")
    fun voteOnClass(
            @PathVariable classId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnClass(classId, inputVote)

    @PatchMapping("/{classId}")
    fun partialUpdateOnClass(
            @PathVariable classId: Int,
            @RequestBody input: ClassInputModel
    ) = classService.partialUpdateOnClass(classId, input)

    @DeleteMapping("/{classId}")
    fun deleteSpecificClass(@PathVariable classId: Int) = classService.deleteSpecificClass(classId)

    // ----------------------------
    // Class Report Endpoints
    // ----------------------------

    @GetMapping("/{classId}/reports")
    fun getClassReports(@PathVariable classId: Int) = classService.getAllReportsOfClass(classId)

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
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnReportClass(classId, reportId, inputVote)

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
    // Class Stage Endpoints
    // ----------------------------

    @GetMapping("/stage")
    fun getAllClassStageEntries() = classService.getAllStagedClasses()

    @GetMapping("/stage/{stageId}")
    fun getClassSpecificStageEntry(@PathVariable stageId: Int) = classService.getSpecificStagedClass(stageId)

    @PostMapping("/stage")
    fun createStagingClass(@RequestBody classStage: ClassStage) = classService.createStagingClass(classStage)

    @PostMapping("/stage/{stageId}")
    fun createClassFromStaged(@PathVariable stageId: Int) = classService.createClassFromStaged(stageId)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnStagedClass(stageId, inputVote)

    @DeleteMapping("/stage")
    fun deleteAllStagedClasses() = classService.deleteAllStagedClasses()

    @DeleteMapping("/stage/{stageId}")
    fun deleteSpecificStagedClass(@PathVariable stageId: Int) = classService.deleteSpecificStagedClass(stageId)

    // ----------------------------
    // Class Version Endpoints
    // ----------------------------

    @GetMapping("/{classId}/versions")
    fun getVersionsOfClass(@PathVariable classId: Int) = classService.getAllVersionsOfClass(classId)

    @GetMapping("/{classId}/versions/{versionId}")
    fun getSpecificVersionOfClass(@PathVariable classId: Int, @PathVariable versionId: Int) = classService.getSpecificVersionOfClass(classId, versionId)

    @DeleteMapping("/{courseId}/versions")
    fun deleteAllVersionsOfClass(@PathVariable courseId: Int) = classService.deleteAllVersionsOfClass(courseId)

    @DeleteMapping("/{courseId}/versions/{versionId}")
    fun deleteSpecificVersionOfClass(
            @PathVariable courseId: Int,
            @PathVariable versionId: Int
    ) = classService.deleteSpecificVersionOfClass(courseId, versionId)

    // ----------------------------
    // Course Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses")
    fun getCoursesOfClass(@PathVariable classId: Int) = classService.getAllCoursesOfClass(classId)

    @GetMapping("/{classId}/courses/{courseId}")
    fun getSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getSpecificCourseOfClass(classId, courseId)

    @PostMapping("/{classId}/courses/{courseId}")
    fun addCourseToClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody classCourseInputModel: CourseClassInputModel
    ) = classService.addCourseToClass(classId, courseId, classCourseInputModel)

    @PostMapping("/{classId}/courses/{courseId}/vote")
    fun voteOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnCourseInClass(classId, courseId, inputVote)

    @DeleteMapping("/{classId}/courses")
    fun deleteAllCoursesInClass(@PathVariable classId: Int) = classService.deleteAllCoursesInClass(classId)

    @DeleteMapping("/{classId}/courses/{courseId}")
    fun deleteSpecificCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteSpecificCourseInClass(classId, courseId)

    // ----------------------------
    // Course Class Report Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/reports")
    fun getReportsOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getAllReportsOfCourseInClass(classId, courseId)

    @GetMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun getReportOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = classService.getSpecificReportOfCourseInClass(classId, courseId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/reports")
    fun createReportOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody courseClassReportInputModel: CourseClassReportInputModel
    ) = classService.reportCourseInClass(classId, courseId, courseClassReportInputModel)

    @PostMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun updateCourseInClassFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = classService.updateCourseInClassFromReport(classId, courseId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/reports/{reportId}/vote")
    fun voteOnReportOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnReportOfCourseInClass(classId, courseId, reportId, inputVote)

    @DeleteMapping("/{classId}/courses/{courseId}/reports")
    fun deleteAllCourseReportsInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllCourseReportsInClass(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun deleteCourseReportInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = classService.deleteSpecificCourseReportInClass(classId, courseId, reportId)


    // ----------------------------
    // Course Class Stage Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/stage")
    fun getStageEntriesOfCoursesInClass(
            @PathVariable classId: Int
    ) = classService.getStageEntriesOfCoursesInClass(classId)

    @GetMapping("/{classId}/courses/stage/{stageId}")
    fun getSpecificStagedCourseInClass(
            @PathVariable classId: Int,
            @PathVariable stageId: Int
    ) = classService.getSpecificStagedCourseInClass(classId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/stage")
    fun createStagingCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody courseClassInputModel: CourseClassInputModel
    ) = classService.createStagingCourseInClass(classId, courseId, courseClassInputModel)

    @PostMapping("/{classId}/courses/stage/{stageId}")
    fun createCourseClassFromStage(
            @PathVariable classId: Int,
            @PathVariable stageId: Int
    ) = classService.addCourseInClassFromStaged(classId, stageId)

    @PostMapping("/{classId}/courses/stage/{stageId}/vote")
    fun voteOnStagedCourseInClassFromStage(
            @PathVariable classId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnStagedCourseInClass(classId, stageId, inputVote)

    @DeleteMapping("/{classId}/courses/stage")
    fun deleteStageEntriesOfCourseInSpecificClass(
            @PathVariable classId: Int
    ) = classService.deleteStageEntriesOfCourseInSpecificClass(classId)

    @DeleteMapping("/{classId}/courses/stage/{stageId}")
    fun deleteSpecificStageEntryOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable stageId: Int
    ) = classService.deleteSpecificStagedCourseInClass(classId, stageId)


    // ----------------------------
    // Lectures in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lectures")
    fun getAllLecturesFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getAllLecturesFromCourseInClass(classId, courseId)

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}")
    fun getSpecificLectureFromSpecificCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.getSpecificLectureFromCourseInClass(classId, courseId, lectureId)

    @PostMapping("/{classId}/courses/{courseId}/lectures")
    fun createLectureOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody lectureInputModel: LectureInputModel
    ) = classService.createLectureOnCourseInClass(classId, courseId, lectureInputModel)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/vote")
    fun voteOnLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnLectureOfCourseInClass(classId, courseId, lectureId, inputVote)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures")
    fun deleteAllLecturesOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllLecturesOfCourseInClass(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}")
    fun deleteSpecificLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.deleteSpecificLectureOfCourseInClass(classId, courseId, lectureId)

    // ----------------------------
    // Report Lectures Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports")
    fun getAllReportsFromLectureOnSpecificCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.getAllReportsOfLectureFromCourseInClass(classId, courseId, lectureId)

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun getSpecificReportOfLectureFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int
    ) = classService.getSpecificReportOfLectureFromCourseInClass(classId, courseId, lectureId, reportId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports")
    fun deleteAllReportsOnLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.deleteAllReportsOnLectureOfCourseInClass(classId, courseId, lectureId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun deleteSpecificReportOnLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int
    ) = classService.deleteSpecificReportOnLectureOfCourseInClass(classId, courseId, lectureId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports")
    fun createReportOnLectureFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @RequestBody lectureReportInputModel: LectureReportInputModel
    ) = classService.createReportOnLectureFromCourseInClass(classId, courseId, lectureId, lectureReportInputModel)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}/vote")
    fun voteOnReportOfLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int,
            @PathVariable inputVote: VoteInputModel
    ) = classService.voteOnReportOfLectureOfCourseInClass(classId, courseId, lectureId, reportId, inputVote)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun updateLectureFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int
    ) = classService.updateLectureFromReport(classId, courseId, lectureId, reportId)

    // ----------------------------
    // Staged Lectures Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lectures/stage")
    fun getAllStagedLecturesOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getAllStagedLecturesOfCourseInClass(classId, courseId)

    @GetMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}")
    fun getSpecificStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.getSpecificStagedLectureOfCourseInClass(classId, courseId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage")
    fun createStagingLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody lectureInputModel: LectureInputModel
    ) = classService.createStagingLectureOfCourseInClass(classId, courseId, lectureInputModel)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/stage")
    fun deleteAllStagedLecturesOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllStagedLecturesOfCourseInClass(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}")
    fun deleteSpecificStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.deleteSpecificStagedLectureOfCourseInClass(classId, courseId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}/vote")
    fun voteOnStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnStagedLectureOfCourseInClass(classId, courseId, stageId, inputVote) //TODO here

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}")
    fun createLectureFromStaged(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.createLectureFromStaged(classId, courseId, stageId)

    // ----------------------------
    // Lectures Versions in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/versions")
    fun getAllVersionsOfLectureOfCourseClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.getAllVersionsOfLectureOfCourseInClass(classId, courseId, lectureId)

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/versions/{versionId}")
    fun getSpecificVersionOfLectureCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable version: Int
    ) = classService.getSpecificVersionOfLectureOfCourseInClass(classId, courseId, lectureId, version)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/versions")
    fun deleteAllVersionsOfLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.deleteAllVersionsOfLectureOfCourseInClass(classId, courseId, lectureId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/versions/{versionId}")
    fun deleteSpecificVersionOfLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable version: Int
    ) = classService.deleteSpecificVersionOfLectureOfCourseInClass(classId, courseId, lectureId, version)

    // ----------------------------
    // Homeworks in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks")
    fun getAllHomeworksOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getAllHomeworksOfCourseInClass(classId, courseId)

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}")
    fun getSpecificHomeworkFromSpecificCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.getSpecificHomeworkFromSpecificCourseInClass(classId, courseId, homeworkId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks")
    fun createHomeworkOnCourseInClass(
            @RequestParam sheet: MultipartFile,
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            homeworkInputModel: HomeworkInputModel
    ) = classService.createHomeworkOnCourseInClass(sheet, classId, courseId, homeworkInputModel)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/vote")
    fun voteOnHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnHomeworkOfCourseInClass(classId, courseId, homeworkId, inputVote)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks")
    fun deleteAllHomeworksOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllHomeworksOfCourseInClass(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}")
    fun deleteSpecificHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.deleteSpecificHomeworkOfCourseInClass(classId, courseId, homeworkId)


    // ----------------------------
    // Staged Homeworks in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun getAllStagedHomeworksOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getAllStagedHomeworksOfCourseInClass(classId, courseId)

    @GetMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun getSpecificStagedHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.getSpecificStagedHomeworkOfCourseInClass(classId, courseId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun createStagingHomeworkOnCourseInClass(
            @PathVariable sheet: MultipartFile,
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            homeworkInputModel: HomeworkInputModel
    ) = classService.createStagingHomeworkOnCourseInClass(sheet, classId, courseId, homeworkInputModel)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun createHomeworkFromStaged(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.createHomeworkFromStaged(classId, courseId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}/vote")
    fun voteOnStagedHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnStagedHomeworkOfCourseInClass(classId, courseId, stageId, inputVote)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun deleteAllStagedHomeworksOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllStagedHomeworksOfCourseInClass(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun deleteSpecificStagedHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.deleteSpecificStagedHomeworkOfCourseInClass(classId, courseId, stageId)

    // ----------------------------
    // Homeworks Reports in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun getAllReportsOfHomeworkFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int
    ) = classService.getAllReportsOfHomeworkFromCourseInClass(classId, courseId, homeWorkId)

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun getSpecificReportOfHomeworkFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int
    ) = classService.getSpecificReportOfHomeworkFromCourseInClass(classId, courseId, homeworkId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun createReportOnHomeworkFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @RequestBody homeworkReportInputModel: HomeworkReportInputModel
    ) = classService.createReportOnHomeworkFromCourseInClass(classId, courseId, homeworkId, homeworkReportInputModel)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun updateHomeworkFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int
    ) = classService.updateHomeworkFromReport(classId, courseId, homeworkId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}/vote")
    fun voteOnReportOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel
    ) = classService.voteOnReportOfHomeworkOfCourseInClass(classId, courseId, homeworkId, reportId, inputVote)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun deleteAllReportsOnHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.deleteAllReportsOnHomeworkOfCourseInClass(classId, courseId, homeworkId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun deleteSpecificReportOnHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int
    ) = classService.deleteSpecificReportOnHomeworkOfCourseInClass(classId, courseId, homeworkId, reportId)

    // ----------------------------
    // Homeworks Version in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions")
    fun getAllVersionsOfHomeworkOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.getAllVersionsOfHomeworkOfCourseInClass(classId, courseId, homeworkId)

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions/{versionId}")
    fun getSpecificVersionOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable version: Int
    ) = classService.getSpecificVersionOfHomeworkOfCourseInClass(classId, courseId, homeworkId, version)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions")
    fun deleteAllVersionsOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.deleteAllVersionsOfHomeworkOfCourseInClass(classId, courseId, homeworkId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions/{versionId}")
    fun deleteSpecificVersionOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable versionId: Int
    ) = classService.deleteSpecificVersionOfHomeworkOfCourseInClass(classId, courseId, homeworkId, versionId)
}