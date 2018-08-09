package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseProgrammeReportOutputModel

data class CourseProgrammeReportCollectionOutputModel (
        val courseProgrammeReportList: List<CourseProgrammeReportOutputModel>
)