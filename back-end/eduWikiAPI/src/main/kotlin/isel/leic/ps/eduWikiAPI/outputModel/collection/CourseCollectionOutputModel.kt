package isel.leic.ps.eduWikiAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.outputModel.single.CourseOutputModel

@Siren4JEntity(
        name = "courses",
        suppressClassProperty = true,
        uri = "/api/courses",
        links = []
)
class CourseCollectionOutputModel(
        courses: Collection<CourseOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<CourseOutputModel>() {
    init {
        addAll(courses)
        /* this.offset = offset.toLong()
         this.limit = limit.toLong()*/

    }
}