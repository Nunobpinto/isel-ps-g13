package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseReportOutputModel

data class CourseReportCollectionOutputModel (
        val courseReportList: List<CourseReportOutputModel>
)