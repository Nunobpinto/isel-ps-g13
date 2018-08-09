package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ProgrammeReportOutputModel

data class ProgrammeReportCollectionOutputModel (
        val programmeReportList: List<ProgrammeReportOutputModel>
)