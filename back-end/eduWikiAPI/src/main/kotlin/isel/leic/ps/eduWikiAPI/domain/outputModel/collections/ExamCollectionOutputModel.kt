package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.ExamOutputModel

data class ExamCollectionOutputModel (
        val examList: List<ExamOutputModel>
)