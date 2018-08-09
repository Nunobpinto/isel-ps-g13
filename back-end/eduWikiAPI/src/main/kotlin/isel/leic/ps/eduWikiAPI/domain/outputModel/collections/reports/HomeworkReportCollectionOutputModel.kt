package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.HomeworkReportOutputModel

data class HomeworkReportCollectionOutputModel (
     val homeworkReportList: List<HomeworkReportOutputModel>
)