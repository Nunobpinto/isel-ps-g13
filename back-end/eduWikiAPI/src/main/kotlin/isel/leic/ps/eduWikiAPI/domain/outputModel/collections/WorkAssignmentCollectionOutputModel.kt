package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.WorkAssignmentOutputModel

data class WorkAssignmentCollectionOutputModel (
        val workAssignmentList: List<WorkAssignmentOutputModel>
)