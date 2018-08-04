package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Vote
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

    fun getAllWorkAssignment() : List<WorkAssignment>

    fun deleteSpecificWorkAssignment(workAssignmentId: Int) : Int

    fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment) : WorkAssignment

    fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment) : WorkAssignment

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    fun getVotesOnWorkAssignment(workAssignmentId: Int): Int

    fun updateVotesOnWorkAssignment(workAssignmentId: Int, votes: Int): Int

    /**
     * Stage entities queries
     */

    fun getWorkAssignmentSpecificStageEntry(stageId: Int) : Optional<WorkAssignmentStage>

    fun getAllStagedWorkAssignments() : List<WorkAssignmentStage>

    fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int) : Int

    fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage>

    fun createStagingWorkAssingment(courseId: Int, termId: Int, workAssignmentStage: WorkAssignmentStage): WorkAssignmentStage

    fun getVotesOnStagedWorkAssignment(stageId: Int): Int

    fun updateStagedWorkAssignmentVotes(stageId: Int, votes: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion>

    fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, version: Int) : Optional<WorkAssignmentVersion>

    fun deleteVersionWorkAssignment(workAssignmentId: Int, version: Int) : Int

    fun deleteAllVersionOfWorkAssignments(workAssignmentId: Int) : Int

    fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion) : WorkAssignmentVersion

    /**
     * Report entity queries
     */

    fun deleteReportOnWorkAssignment(reportId: Int) : Int

    fun deleteAllReportsOnWorkAssignment(courseMiscUnitId : Int) : Int

    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): WorkAssignmentReport

    fun getSpecificReportOfWorkAssignment(workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport>

    fun getVotesOnReportedWorkAssignment(reportId: Int): Int

    fun updateVotesOnReportedWorkAssignment(reportId: Int, votes: Int): Int

}