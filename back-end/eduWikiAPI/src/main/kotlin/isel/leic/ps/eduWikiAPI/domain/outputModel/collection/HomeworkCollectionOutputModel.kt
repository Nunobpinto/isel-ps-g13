package isel.leic.ps.eduWikiAPI.domain.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.HomeworkOutputModel

@Siren4JEntity(
        name = "homeworks",
        suppressClassProperty = true,
        uri = "/api/classes/{classId}/courses/{courseId}/homeworks",
        links = []
)
class HomeworkCollectionOutputModel(
        homeworks: Collection<HomeworkOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<HomeworkOutputModel>() {
    init {
        addAll(homeworks)
        /* this.offset = offset.toLong()
         this.limit = limit.toLong()*/

    }
}