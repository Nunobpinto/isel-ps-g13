package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.CourseVersionOutputModel

data class CourseVersionCollectionOutputModel (
        val courseVersionList: List<CourseVersionOutputModel>
)