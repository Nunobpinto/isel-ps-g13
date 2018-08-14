package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.WorkAssignmentOutputModel

data class WorkAssignmentCollectionOutputModel (
        val workAssignmentList: List<WorkAssignmentOutputModel>
)