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

    fun getSpecificWorkAssignment(courseMiscUnitId: Int, courseId: Int, termId: Int) : Optional<WorkAssignment>

    fun getAllWorkAssignment() : List<WorkAssignment>

    fun deleteSpecificWorkAssignment(courseMiscUnitId: Int) : Int

    fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment) : Int

    fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment) : Optional<WorkAssignment>

    fun voteOnWorkAssignment(courseMiscUnitId: Int, vote: Vote) : Int

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    /**
     * Stage entities queries
     */

    fun getWorkAssignmentSpecificStageEntry(stageId: Int) : Optional<WorkAssignmentStage>

    fun getAllWorkAssignmentStages() : List<WorkAssignmentStage>

    fun deleteSpecificStagedWorkAssignment(stageId: Int) : Int

    fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage>

    fun createStagingWorkAssingment(courseId: Int, termId: Int, stage: WorkAssignmentStage): Optional<WorkAssignmentStage>

    fun voteOnStagedWorkAssignment(stageId: Int, vote: Vote): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion>

    fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, version: Int) : Optional<WorkAssignmentVersion>

    fun deleteVersionWorkAssignment(workAssignmentId: Int, version: Int) : Int

    fun deleteAllVersionOfWorkAssignments(workAssignmentId: Int) : Int

    fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion) : Optional<WorkAssignmentVersion>

    /**
     * Report entity queries
     */

    fun deleteReportOnWorkAssignment(reportId: Int) : Int

    fun deleteAllReportsOnWorkAssignment(courseMiscUnitId : Int) : Int

    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): Optional<WorkAssignmentReport>

    fun voteOnReportToWorkAssignmentOnCourseInTerm(reportId: Int, vote: Vote): Int

    fun getSpecificReportOfWorkAssignment(workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport>

}