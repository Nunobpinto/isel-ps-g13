package isel.leic.ps.eduWikiAPI.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.outputModel.single.ExamOutputModel

@Siren4JEntity(
        name = "exams",
        suppressClassProperty = true,
        uri = "/api/courses/{courseId}/terms/{termId}/exams",
        links = []
)
class ExamCollectionOutputModel(
        courses: Collection<ExamOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<ExamOutputModel>() {
    init {
        addAll(courses)
        /* this.offset = offset.toLong()
         this.limit = limit.toLong()*/

    }
}