package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion

interface WorkAssignmentDAO {

    /**
     * Main entities queries
     */

    fun getSpecificWorkAssignment(courseMiscUnitId: Int) : WorkAssignment

    fun getAllWorkAssignment() : List<WorkAssignment>

    fun deleteWorkAssignment(courseMiscUnitId: Int) : Int

    fun deleteWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment) : Int

    fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment) : Int

    fun voteOnWorkAssignment(courseMiscUnitId: Int, voteType: Int)

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    /**
     * Stage entities queries
     */

    fun getWorkAssignmentSpecificStageEntry(stageId: Int) : WorkAssignmentStage

    fun getAllWorkAssignmentStages() : List<WorkAssignmentStage>

    fun deleteStagedWorkAssignment(stageId: Int) : Int

    fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun createWorkAssignmentStage(workAssignmentStage: WorkAssignmentStage)

    fun voteOnWorkAssignmentStage(courseMiscUnitStageId: Int, voteType: Int)

    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage

    fun createStagingWorkAssingment(courseId: Int, termId: Int, stage: WorkAssignmentStage): Int

    fun voteOnStagedWorkAssignment(stageId: Int, inputVote: VoteInputModel): Int

    /**
     * Version entities queries
     */

    fun getVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int) : WorkAssignmentVersion

    fun deleteVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int) : Int

    fun deleteAllVersionWorkAssignments(versionWorkAssignmentId: Int) : Int

    fun addToWorkAssignmentVersion(workAssignment: WorkAssignment) : Int

    /**
     * Report entity queries
     */

    fun reportWorkAssignment(workAssignmentReport: WorkAssignmentReport)

    fun deleteReportOnWorkAssignment(workAssignmentId: Int, reportId: Int) : Int

    fun deleteAllReportsOnWorkAssignment(courseMiscUnitId : Int) : Int

    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): Int

    fun voteOnReportToWorkAssignmentOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int

    fun getSpecificReportOfWorkAssignment(workAssignmentId: Int, reportId: Int): WorkAssignmentReport


}