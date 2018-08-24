package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import java.util.*

interface WorkAssignmentDAO {

    /**
     * Main entities queries
     */

    fun getSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int) : Optional<WorkAssignment>

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment) : WorkAssignment

    fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment) : WorkAssignment

    fun updateVotesOnWorkAssignment(workAssignmentId: Int, votes: Int): Int

    fun deleteSpecificWorkAssignment(courseId: Int, termId: Int, workAssignmentId: Int) : Int

    /**
     * Stage entities queries
     */

    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage>

    fun createStagingWorkAssingment(courseId: Int, termId: Int, workAssignmentStage: WorkAssignmentStage): WorkAssignmentStage

    fun updateStagedWorkAssignmentVotes(stageId: Int, votes: Int): Int

    fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int) : Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int): List<WorkAssignmentVersion>

    fun getVersionOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, version: Int) : Optional<WorkAssignmentVersion>

    fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion) : WorkAssignmentVersion

    /**
     * Report entity queries
     */

    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    fun getSpecificReportOfWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport>

    fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): WorkAssignmentReport

    fun updateVotesOnReportedWorkAssignment(reportId: Int, votes: Int): Int

    fun deleteReportOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int) : Int

    fun getWorkAssignmentByLogId(logId: Int): Optional<WorkAssignment>

    fun getWorkAssignmentReportByLogId(logId: Int): Optional<WorkAssignmentReport>

    fun getWorkAssignmentStageByLogId(logId: Int): Optional<WorkAssignmentStage>

    fun getAllWorkAssignmentsOfSpecificCourse(courseId: Int): List<WorkAssignment>

    fun getAllStagedWorkAssignmentOnSpecificCourse(courseId: Int): List<WorkAssignmentStage>

}