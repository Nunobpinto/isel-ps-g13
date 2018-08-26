package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.UserReportOutputModel

data class UserReportCollectionOutputModel (
        val reports: List<UserReportOutputModel>
)
