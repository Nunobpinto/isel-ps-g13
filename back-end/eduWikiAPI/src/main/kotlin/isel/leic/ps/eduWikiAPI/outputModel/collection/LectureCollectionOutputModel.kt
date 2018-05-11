package isel.leic.ps.eduWikiAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.outputModel.single.LectureOutputModel

@Siren4JEntity(
        name = "lectures",
        suppressClassProperty = true,
        uri = "/api/classes/{classId}/courses/{courseId}/lessons",
        links = []
)
class LectureCollectionOutputModel(
        lectures: Collection<LectureOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<LectureOutputModel>() {
    init {
        addAll(lectures)
        /* this.offset = offset.toLong()
         this.limit = limit.toLong()*/

    }
}
