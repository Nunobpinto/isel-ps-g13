package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ExamReportOutputModel

data class ExamReportCollectionOutputModel (
        val examReportList: List<ExamReportOutputModel>
)
