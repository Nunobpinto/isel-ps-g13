package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.WorkAssignmentVersionOutputModel

data class WorkAssignmentVersionCollectionOutputModel (
        val workAssignmentVersionList: List<WorkAssignmentVersionOutputModel>
)