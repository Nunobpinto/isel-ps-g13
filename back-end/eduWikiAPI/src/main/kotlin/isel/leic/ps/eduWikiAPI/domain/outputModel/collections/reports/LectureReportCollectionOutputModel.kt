package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.LectureReportOutputModel

data class LectureReportCollectionOutputModel (
        val lectureReportList: List<LectureReportOutputModel>
)