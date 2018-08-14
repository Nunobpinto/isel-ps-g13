package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ClassOutputModel

data class ClassCollectionOutputModel (
        val classList: List<ClassOutputModel>
)