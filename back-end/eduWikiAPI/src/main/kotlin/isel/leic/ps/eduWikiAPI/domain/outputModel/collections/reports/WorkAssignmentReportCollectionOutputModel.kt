package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.WorkAssignmentReportOutputModel

data class WorkAssignmentReportCollectionOutputModel (
     val workAssignmentReportList: List<WorkAssignmentReportOutputModel>
)