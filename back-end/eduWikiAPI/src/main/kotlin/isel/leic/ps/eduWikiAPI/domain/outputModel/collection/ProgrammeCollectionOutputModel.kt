package isel.leic.ps.eduWikiAPI.domain.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ProgrammeOutputModel

@Siren4JEntity(
        name = "programmes",
        suppressClassProperty = true,
        uri = "/api/programmes",
        links = []
)
class ProgrammeCollectionOutputModel(
        programmes: Collection<ProgrammeOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<ProgrammeOutputModel>() {
    init {
        addAll(programmes)
        /* this.offset = offset.toLong()
         this.limit = limit.toLong()*/

    }
}