package isel.leic.ps.eduWikiAPI.domain.outputModel.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.WorkAssignmentOutputModel

@Siren4JEntity(
        name = "work_items",
        suppressClassProperty = true,
        uri = "/api/courses/{courseId}/terms/{termId}/workitems",
        links = []
)
class WorkAssignmentCollectionOutputModel(
        workAssignments: Collection<WorkAssignmentOutputModel> = CollectionResource(),
        offset: String = "0",
        limit: String = "0"
) : CollectionResource<WorkAssignmentOutputModel>() {
    init {
        addAll(workAssignments)
        /* this.offset = offset.toLong()
         this.limit = limit.toLong()*/

    }
}
