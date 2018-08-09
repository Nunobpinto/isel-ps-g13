package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseClassReportOutputModel

data class CourseClassReportCollectionOutputModel (
        val courseClassReportList: List<CourseClassReportOutputModel>
)