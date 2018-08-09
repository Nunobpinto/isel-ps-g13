package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ClassVersionOutputModel

data class ClassVersionCollectionOutputModel (
        val classVersionList: List<ClassVersionOutputModel>
)