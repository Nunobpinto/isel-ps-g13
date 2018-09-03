package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.HomeworkStageOutputModel
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.time.LocalDate

@RestController
@RequestMapping("/classes")
class ClassController {

    @Autowired
    lateinit var classService: ClassService

    // ---------- Class ----------

    // ----------------------------
    // Class Endpoints
    // ----------------------------

    @GetMapping
    fun getAllClasses() = classService.getAllClasses()

    @GetMapping("/{classId}")
    fun getSpecificClass(@PathVariable classId: Int) = classService.getSpecificClass(classId)

    @PostMapping
    fun createClass(@RequestBody input: ClassInputModel, principal: Principal) = classService.createClass(input, principal)

    @PostMapping("/{classId}/vote")
    fun voteOnClass(
            @PathVariable classId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnClass(classId, inputVote, principal)

    @PatchMapping("/{classId}")
    fun partialUpdateOnClass(
            @PathVariable classId: Int,
            @RequestBody input: ClassInputModel,
            principal: Principal
    ) = classService.partialUpdateOnClass(classId, input, principal)

    @DeleteMapping("/{classId}")
    fun deleteSpecificClass(@PathVariable classId: Int, principal: Principal) = classService.deleteSpecificClass(classId, principal)

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
    fun createClassReport(
            @PathVariable classId: Int,
            @RequestBody report: ClassReportInputModel,
            principal: Principal
    ) = classService.createClassReport(classId, report, principal)

    @PostMapping("/{classId}/reports/{reportId}/vote")
    fun voteOnReportedClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnReportClass(classId, reportId, inputVote, principal)

    @PostMapping("/{classId}/reports/{reportId}")
    fun updateReportedClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.updateClassFromReport(classId, reportId, principal)

    @DeleteMapping("/{classId}/reports/{reportId}")
    fun deleteReportInClass(
            @PathVariable classId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.deleteSpecificReportInClass(classId, reportId, principal)

    // ----------------------------
    // Class Stage Endpoints
    // ----------------------------

    @GetMapping("/stage")
    fun getAllClassStageEntries() = classService.getAllStagedClasses()

    @GetMapping("/stage/{stageId}")
    fun getClassSpecificStageEntry(@PathVariable stageId: Int) = classService.getSpecificStagedClass(stageId)

    @PostMapping("/stage")
    fun createStagingClass(@RequestBody classStageInputModel: ClassInputModel, principal: Principal) = classService.createStagingClass(classStageInputModel, principal)

    @PostMapping("/stage/{stageId}")
    fun createClassFromStaged(@PathVariable stageId: Int, principal: Principal) = classService.createClassFromStaged(stageId, principal)

    @PostMapping("/stage/{stageId}/vote")
    fun voteOnStagedClass(
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnStagedClass(stageId, inputVote, principal)

    @DeleteMapping("/stage/{stageId}")
    fun deleteSpecificStagedClass(@PathVariable stageId: Int, principal: Principal) = classService.deleteSpecificStagedClass(stageId, principal)

    // ----------------------------
    // Class Version Endpoints
    // ----------------------------

    @GetMapping("/{classId}/versions")
    fun getVersionsOfClass(@PathVariable classId: Int) = classService.getAllVersionsOfClass(classId)

    @GetMapping("/{classId}/versions/{versionId}")
    fun getSpecificVersionOfClass(@PathVariable classId: Int, @PathVariable versionId: Int) = classService.getSpecificVersionOfClass(classId, versionId)

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
            principal: Principal
    ) = classService.addCourseToClass(classId, courseId, principal)

    @PostMapping("/{classId}/courses/{courseId}/vote")
    fun voteOnCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnCourseInClass(classId, courseId, inputVote, principal)

    @DeleteMapping("/{classId}/courses/{courseId}")
    fun deleteSpecificCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            principal: Principal
    ) = classService.deleteSpecificCourseInClass(classId, courseId, principal)

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
            @RequestBody courseClassReportInputModel: CourseClassReportInputModel,
            principal: Principal
    ) = classService.reportCourseInClass(classId, courseId, courseClassReportInputModel, principal)

    @PostMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun updateCourseInClassFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.updateCourseInClassFromReport(classId, courseId, reportId, principal)

    @PostMapping("/{classId}/courses/{courseId}/reports/{reportId}/vote")
    fun voteOnReportOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnReportOfCourseInClass(classId, courseId, reportId, inputVote, principal)

    @DeleteMapping("/{classId}/courses/{courseId}/reports/{reportId}")
    fun deleteReportOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.deleteReportOfCourseInClass(classId, courseId, reportId, principal)

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
            principal: Principal
    ) = classService.createStagingCourseInClass(classId, courseId, principal)

    @PostMapping("/{classId}/courses/stage/{stageId}")
    fun createCourseClassFromStage(
            @PathVariable classId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = classService.addCourseInClassFromStaged(classId, stageId, principal)

    @PostMapping("/{classId}/courses/stage/{stageId}/vote")
    fun voteOnStagedCourseInClassFromStage(
            @PathVariable classId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnStagedCourseInClass(classId, stageId, inputVote, principal)

    @DeleteMapping("/{classId}/courses/stage/{stageId}")
    fun deleteSpecificStageEntryOfSpecificCourseOfClass(
            @PathVariable classId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = classService.deleteSpecificStagedCourseInClass(classId, stageId, principal)

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
            @RequestBody lectureInputModel: LectureInputModel,
            principal: Principal
    ) = classService.createLectureOnCourseInClass(classId, courseId, lectureInputModel, principal)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/vote")
    fun voteOnLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnLectureOfCourseInClass(classId, courseId, lectureId, inputVote, principal)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}")
    fun deleteSpecificLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            principal: Principal
    ) = classService.deleteSpecificLectureOfCourseInClass(classId, courseId, lectureId, principal)

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

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun deleteSpecificReportOnLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.deleteSpecificReportOnLectureOfCourseInClass(classId, courseId, lectureId, reportId, principal)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports")
    fun createReportOnLectureFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @RequestBody lectureReportInputModel: LectureReportInputModel,
            principal: Principal
    ) = classService.createReportOnLectureFromCourseInClass(classId, courseId, lectureId, lectureReportInputModel, principal)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}/vote")
    fun voteOnReportOfLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int,
            @PathVariable inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnReportOfLectureOfCourseInClass(classId, courseId, lectureId, reportId, inputVote, principal)

    @PostMapping("/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}")
    fun updateLectureFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable lectureId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.updateLectureFromReport(classId, courseId, lectureId, reportId, principal)

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
            @RequestBody lectureInputModel: LectureInputModel,
            principal: Principal
    ) = classService.createStagingLectureOfCourseInClass(classId, courseId, lectureInputModel, principal)

    @DeleteMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}")
    fun deleteSpecificStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = classService.deleteSpecificStagedLectureOfCourseInClass(classId, courseId, stageId, principal)

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}/vote")
    fun voteOnStagedLectureOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnStagedLectureOfCourseInClass(classId, courseId, stageId, inputVote, principal)

    @PostMapping("/{classId}/courses/{courseId}/lectures/stage/{stageId}")
    fun createLectureFromStaged(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = classService.createLectureFromStaged(classId, courseId, stageId, principal)

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
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestParam sheet: MultipartFile?,
            @RequestParam dueDate: String,
            @RequestParam multipleDeliveries: Boolean,
            @RequestParam lateDelivery: Boolean,
            @RequestParam homeworkName: String,
            principal: Principal
    ): HomeworkOutputModel{
        val homeworkInputModel = HomeworkInputModel(
                dueDate = LocalDate.parse(dueDate),
                multipleDeliveries = multipleDeliveries,
                lateDelivery = lateDelivery,
                homeworkName = homeworkName
        )
        return classService.createHomeworkOnCourseInClass(sheet, classId, courseId, homeworkInputModel, principal)
    }

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/vote")
    fun voteOnHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnHomeworkOfCourseInClass(classId, courseId, homeworkId, inputVote, principal)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}")
    fun deleteSpecificHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            principal: Principal
    ) = classService.deleteSpecificHomeworkOfCourseInClass(classId, courseId, homeworkId, principal)


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
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @RequestParam sheet: MultipartFile?,
            @RequestParam dueDate: String,
            @RequestParam multipleDeliveries: Boolean,
            @RequestParam lateDelivery: Boolean,
            @RequestParam homeworkName: String,
            principal: Principal
    ): HomeworkStageOutputModel {
        val homeworkInputModel = HomeworkInputModel(
                dueDate = LocalDate.parse(dueDate),
                multipleDeliveries = multipleDeliveries,
                lateDelivery = lateDelivery,
                homeworkName = homeworkName
        )
        return classService.createStagingHomeworkOnCourseInClass(sheet, classId, courseId, homeworkInputModel, principal)
    }

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun createHomeworkFromStaged(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = classService.createHomeworkFromStaged(classId, courseId, stageId, principal)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}/vote")
    fun voteOnStagedHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnStagedHomeworkOfCourseInClass(classId, courseId, stageId, inputVote, principal)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/stage/{stageId}")
    fun deleteSpecificStagedHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable stageId: Int,
            principal: Principal
    ) = classService.deleteSpecificStagedHomeworkOfCourseInClass(classId, courseId, stageId, principal)

    // ----------------------------
    // Homeworks Reports in Class Endpoints
    // ----------------------------

    @GetMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports")
    fun getAllReportsOfHomeworkFromCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int
    ) = classService.getAllReportsOfHomeworkFromCourseInClass(classId, courseId, homeworkId)

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
            @RequestBody homeworkReportInputModel: HomeworkReportInputModel,
            principal: Principal
    ) = classService.createReportOnHomeworkFromCourseInClass(classId, courseId, homeworkId, homeworkReportInputModel, principal)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun updateHomeworkFromReport(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.updateHomeworkFromReport(classId, courseId, homeworkId, reportId, principal)

    @PostMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}/vote")
    fun voteOnReportOfHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int,
            @RequestBody inputVote: VoteInputModel,
            principal: Principal
    ) = classService.voteOnReportOfHomeworkOfCourseInClass(classId, courseId, homeworkId, reportId, inputVote, principal)

    @DeleteMapping("/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}")
    fun deleteSpecificReportOnHomeworkOfCourseInClass(
            @PathVariable classId: Int,
            @PathVariable courseId: Int,
            @PathVariable homeworkId: Int,
            @PathVariable reportId: Int,
            principal: Principal
    ) = classService.deleteSpecificReportOnHomeworkOfCourseInClass(classId, courseId, homeworkId, reportId, principal)

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
            @PathVariable versionId: Int
    ) = classService.getSpecificVersionOfHomeworkOfCourseInClass(classId, courseId, homeworkId, versionId)

}