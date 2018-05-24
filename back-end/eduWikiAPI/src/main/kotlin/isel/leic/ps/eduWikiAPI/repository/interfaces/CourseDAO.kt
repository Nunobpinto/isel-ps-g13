package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion

interface CourseDAO {

    /**
     * Main entities queries
     */

    fun getSpecificCourse(courseId: Int) : Course

    fun getAllCourses() : List<Course>

    fun deleteCourse(courseId: Int) : Int

    fun deleteAllCourses() : Int

    fun updateCourse(course: Course) : Int

    fun createCourse(course: Course)

    fun voteOnCourse(courseId: Int, inputVote: VoteInputModel)

    fun getTermsOfCourse(courseId: Int): List<Term>

    fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int): WorkAssignment

    fun getClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>

    /**
     * Stage entities queries
     */

    fun deleteCourseStage(courseStageId: Int) : Int

    fun deleteAllCourseStages() : Int

    fun createCourseStage(courseStage: CourseStage)

    fun voteOnCourseStage(courseStageId: Int, voteType: Int)

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(stageId: Int): CourseStage

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage

    fun getStageEntriesFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    fun getStageEntryFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage

    /**
     * Version entities queries
     */

    fun getVersionCourse(versionCourseId: Int, version: Int) : CourseVersion

    fun getAllVersionCourses() : List<CourseVersion>

    fun deleteVersionCourse(versionCourseId: Int, version: Int) : Int

    fun deleteAllVersionCourses() : Int

    fun createVersionCourse(courseVersion: CourseVersion)

    /**
     * Report entity queries
     */

    fun reportCourse(courseId: Int, courseReport: CourseReport)

    fun deleteReportOnCourse(reportId: Int) : Int

    fun deleteAllReportsOnCourse(courseId : Int) : Int

    fun deleteAllReports() : Int

    fun getAllReportsOnCourse(courseId: Int) : List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport

    fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport>

    fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport

    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    fun getSpecificReportFromWorkItemOnSpecificTermOfCourse(reportId: Int): WorkAssignmentReport

    fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel)

}