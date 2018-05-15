package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion

interface WorkAssignmentDAO {

    /**
     * Main entities queries
     */

    fun getWorkAssignment(workAssignmentId: Int) : WorkAssignment

    fun getAllWorkAssignment() : List<WorkAssignment>

    fun deleteWorkAssignment(workAssignmentId: Int) : Int

    fun deleteAllWorkAssignments() : Int

    fun updateWorkAssignment(workAssignment: WorkAssignment, user: String) : Int

    fun createWorkAssignment(workAssignment: WorkAssignment)

    fun voteOnWorkAssignment(workAssignmentId: Int, voteType: Int)

    /**
     * Stage entities queries
     */
    fun getWorkAssignmentStage(workAssignmentId: Int) : WorkAssignmentStage

    fun getAllWorkAssignmentStages() : List<WorkAssignmentStage>

    fun deleteWorkAssignmentStage(workAssignmentId: Int) : Int

    fun deleteAllWorkAssignmentStages() : Int

    fun createWorkAssignmentStage(workAssignmentStage: WorkAssignmentStage)

    fun voteOnWorkAssignmentStage(workAssignmentId: Int, voteType: Int)

    /**
     * Version entities queries
     */

    fun getVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int) : WorkAssignmentVersion

    fun getAllVersionWorkAssignments() : List<WorkAssignmentVersion>

    fun deleteVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int) : Int

    fun deleteAllVersionWorkAssignments() : Int

    fun createVersionWorkAssignment(workAssignmentVersion: WorkAssignmentVersion)

    /**
     * Report entity queries
     */

    fun reportWorkAssignment(workAssignmentReport: WorkAssignmentReport)

    fun deleteReportOnWorkAssignment(reportId: Int) : Int

    fun deleteAllReportsOnWorkAssignment(workAssignmentId : Int) : Int

    fun deleteAllReports(): Int

}