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
    fun createStagingClass(@RequestBody input: ClassInputModel) = classService.createStagingClass(input)

    @PostMapping("/stage/{stageId}")
    fun createClassFromStaged(@PathVariable stageId: Int) = classService.createClassFromStaged(stageId)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnStagedClass(stageId, vote)

    @PatchMapping("/stage/{stageId}")
    fun partialUpdateOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody input: ClassInputModel
    ) = classService.partialUpdateOnStagedClass(stageId, input)

    @DeleteMapping("/stage")
    fun deleteAllStagedClasses() = classService.deleteAllStagedClasses()

    @DeleteMapping("/stage/{stageId}")
    fun deleteStagedClass(@PathVariable stageId: Int) = classService.deleteSpecificStagedClass(stageId)

    // ----------------------------
    // Course Version Endpoints
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
    ) = classService.getSpecificCourseOfClass(classId, classId)

    @PostMapping("/{classId}/courses/{courseId}")
    fun addCourseToClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.addCourseToClass(classId, courseId)

    @PostMapping("/{classId}/courses/{courseId}/vote")
    fun voteOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnCourseInClass(classId, courseId, vote)

    @DeleteMapping("/{classId}/courses")
    fun deleteCourseInClass(@PathVariable classId: Int) = classService.deleteAllCoursesInClass(classId)

    @DeleteMapping("/{classId}/courses/{courseId}")
    fun deleteCourseInClass(
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
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnReportOfCourseInClass(classId, courseId, reportId, vote)

    @DeleteMapping("/{classId}/courses/{courseId}/reports")
    fun deleteAllCourseReporstInClass(
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
    fun getStageEntriesOfSpecificCourseOfClass(
            @PathVariable classId: Int
    ) = classService.getAllCoursesStagedInClass(classId)

    @GetMapping("/{classId}/courses/stage/{stageId}")
    fun getStageEntriesOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable stageId: Int
    ) = classService.getSpecificStagedCourseInClass(classId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/stage/")
    fun createStagingCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.createStagingCourseInClass(classId, courseId)

    @PostMapping("/{classId}/courses/stage/{stageId}")
    fun createCourseOnClassFromStage(
            @PathVariable classId: Int,
            @PathVariable stageId: Int
    ) = classService.addCourseToClassFromStaged(classId, stageId)

    @PostMapping("/{classId}/courses/stage/{stageId}/vote")
    fun voteOnStagedCourseOnClassFromStage(
            @PathVariable classId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnStagedCourseInClass(classId, stageId, vote)

    @DeleteMapping("/{classId}/courses/stage")
    fun deleteStageEntriesOfSpecificCourseOfClass(
            @PathVariable classId: Int
    ) = classService.deleteAllStagedCoursesInClass(classId)

    @DeleteMapping("/{classId}/courses/stage/{stageId}")
    fun deleteSpecificStageEntryOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable stageId: Int
    ) = classService.deleteSpecificStagedCourseInClass(classId, stageId)


    // ----------------------------
    // Lectures in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lectures")
    fun getLectureFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getAllLecturesFromCourseInClass(classId, courseId)

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}")
    fun getSpecificLectureFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.getSpecificLectureFromCourseInClass(classId, courseId, lectureId)

    @PostMapping("/{classId}/courses/{courseId}/lectures")
    fun createLectureOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody lecture: LectureInputModel
    ) = classService.createLectureOnCourseInClass(classId, courseId, lecture)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/vote")
    fun voteOnLectureOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnLecture(classId, courseId, lectureId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures")
    fun deleteAllLecturesOfCourse(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllLecturesOfCourse(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}")
    fun deleteSpecificLectureOfCourse(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.deleteSpecificLectureOfCourse(classId, courseId, lectureId)

    // ----------------------------
    // Report Lectures Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports")
    fun getAllReportsFromLectureOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.getAllReportsOfLectureFromCourse(classId, courseId, lectureId)

    @GetMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun getReportFromLectureOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int
    ) = classService.getSpecificReportOfLectureFromCourse(classId, courseId, lectureId, reportId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports")
    fun deleteAllReportsOnLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.deleteAllReportsInLecture(classId, courseId, lectureId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun deleteReportOnLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int
    ) = classService.deleteSpecificReportInLecture(classId, courseId, lectureId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports")
    fun createReportOnLectureOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @RequestBody report: LessonReportInputModel
    ) = classService.reportLectureOnCourse(classId, courseId, lectureId, report)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}/vote")
    fun voteOnReportFromLectureOnSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int,
            @PathVariable vote: VoteInputModel
    ) = classService.voteOnReportOfLessonInCourse(classId, courseId, lectureId, reportId, vote)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun updateLectureFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int
    ) = classService.updateLessonFromReport(classId, courseId, lectureId, reportId)

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
    ) = classService.getSpecificStagedLectureOfCourse(classId, courseId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage")
    fun createStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody lecture: LectureInputModel
    ) = classService.createStagingLectureOfCourseInClass(classId, courseId, lecture)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/stage")
    fun deletAllStagedLecturesOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllStagedLecturesOfCourseInClass(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}")
    fun deleteSpecificStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.deleteSpecificStagedLectureOfCourse(classId, courseId, stageId)

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}/vote")
    fun voteOnStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnStagedLectureOfCourseInClass(classId, courseId, stageId, vote)

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}")
    fun createLectureFromStage(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.createLectureFromStage(classId, courseId, stageId)

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
            @PathVariable versionId: Int
    ) = classService.getSpecificVersionOfLectureOfCourseInClass(classId, courseId, lectureId, versionId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/versions")
    fun deleteAllVersionsOfLectureCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int
    ) = classService.deleteAllVersionsOfLectureOfCourseInTerm(classId, courseId, lectureId)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/versions/{versionId}")
    fun deleteSpecificVersionOfLectureCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable versionId: Int
    ) = classService.deleteSpecificVersionOfLecture(classId, courseId, lectureId, versionId)

    // ----------------------------
    // Homeworks in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks")
    fun getHomeWorksFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.getAllHomeworksOfCourseInClass(classId, courseId)

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}")
    fun getSpecificHomeWorkFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.getSpecificHomeworkOfCourseInClass(classId, courseId, homeworkId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks")
    fun createHomeWork(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody homeworkInputModel: HomeworkInputModel
    ) = classService.createHomeworkOfCourseInClass(classId, courseId, homeworkInputModel)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/vote")
    fun voteOnHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnHomeworkOfCourseInClass(classId, courseId, homeworkId, vote)

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

    @PostMapping("/{classId}/courses/{courseId}/stage")
    fun createStagedHomework(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody homeworkInputModel: HomeworkInputModel
    ) = classService.createStagingHomeworkOfCourseInClass(classId, courseId, homeworkInputModel)

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
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnStagedHomeworkOfCourseInClass(classId, courseId, stageId, vote)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/stage")
    fun deleteAllStagedHomework(
            @PathVariable classId: Int,
            @PathVariable courseId: Int
    ) = classService.deleteAllStagedHomeworksOfCourseInClass(classId, courseId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun deleteStagedHomework(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int
    ) = classService.deleteStagedHomeworkOfCourseInClass(classId, courseId, stageId)

    // ----------------------------
    // Homeworks Reports in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun getAllReportsOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int
    ) = classService.getAllReportsOfHomeworkOfCourseInClass(classId, courseId, homeWorkId)

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun getSpecificReportOfHomeworkFromSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int
    ) = classService.getSpecificReportOfHomeworkOfCourseInClass(classId, courseId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun createReportOnHomework(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @RequestBody homeworkReportInputModel: HomeworkReportInputModel
    ) = classService.reportHomeworkOnCourse(classId, courseId, homeworkId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun updateHomeworkFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int
    ) = classService.updateHomeworkFromReport(classId, courseId, homeWorkId, reportId)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}/vote")
    fun voteOnReportOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeWorkId: Int,
            @PathVariable reportId: Int,
            @RequestBody vote: VoteInputModel
    ) = classService.voteOnReportOfHomeworkInCourse(classId, courseId, homeWorkId, reportId, vote)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun deleteAllReportsOnHomework(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.deleteAllReportsInHomework(classId, courseId, homeworkId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun deleteSpecificReportOnHomework(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int
    ) = classService.deleteSpecificReportInHomework(classId, courseId, homeworkId, reportId)

    // ----------------------------
    // Homeworks Version in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions")
    fun getAllVersionsOfHomeworkOfCourseClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.getAllVersionsOfHomeworkOnCourseInClass(classId, courseId, homeworkId)

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions/{versionId}")
    fun getSpecificVersionOfHomeworkCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable versionId: Int
    ) = classService.getSpecificVersionOfHomework(classId, courseId, homeworkId, versionId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions")
    fun deleteAllVersionsOfHomeworkCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.deleteAllVersionsOfHomework(classId, courseId, homeworkId)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions/{versionId}")
    fun deleteSpecificVersionOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable versionId: Int
    ) = classService.deleteSpecificVersionOfHomework(classId, courseId, homeworkId, versionId)
}