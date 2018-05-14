package isel.leic.ps.eduWikiAPI.domain.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TermOutputModel

@Siren4JEntity(
        name = "terms",
        suppressClassProperty = true,
        //uri = "/api/courses",
        links = []
)
class TermCollectionOutputModel (
        terms: Collection<TermOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<TermOutputModel>() {
    init {
        addAll(terms)
        /* this.offset = offset.toLong()
         this.limit = limit.toLong()*/

    }
}