package isel.leic.ps.eduWikiAPI.outputModel.single

import com.google.code.siren4j.annotations.Siren4JEntity

@Siren4JEntity(
        name = "reputation",
        suppressClassProperty = true,
        //uri = "/api/classes/{classId}/courses/{courseId}/lessons/{lessonId}",
        links = []
)
class ReputationOutputModel (
        val reputationId: Int = 0,
        val points: Int = 0,
        val rank: Int = 0,
        val version: Int = 0,
        val studentId: Int = 0
)