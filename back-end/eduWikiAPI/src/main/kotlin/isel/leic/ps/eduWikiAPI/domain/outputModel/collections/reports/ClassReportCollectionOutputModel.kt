package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ClassReportOutputModel

data class ClassReportCollectionOutputModel (
        val classReportList: List<ClassReportOutputModel>
)