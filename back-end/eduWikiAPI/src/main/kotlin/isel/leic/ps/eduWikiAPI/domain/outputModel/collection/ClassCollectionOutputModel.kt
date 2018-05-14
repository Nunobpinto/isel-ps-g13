package isel.leic.ps.eduWikiAPI.domain.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JAction
import com.google.code.siren4j.annotations.Siren4JActionField
import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.component.impl.ActionImpl.*
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ClassOutputModel

@Siren4JEntity(
        name = "classes",
        suppressClassProperty = true,
        uri = "/api/classes",
        links = []
)
class ClassCollectionOutputModel(
        checklists: Collection<ClassOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<ClassOutputModel>() {
    init {
        addAll(checklists)
       /* this.offset = offset.toLong()
        this.limit = limit.toLong()*/

    }
}