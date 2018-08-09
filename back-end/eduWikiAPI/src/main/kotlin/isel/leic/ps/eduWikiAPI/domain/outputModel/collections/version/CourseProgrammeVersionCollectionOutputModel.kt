package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.CourseProgrammeVersionOutputModel

data class CourseProgrammeVersionCollectionOutputModel (
        val courseProgrammeVersionList: List<CourseProgrammeVersionOutputModel>
)