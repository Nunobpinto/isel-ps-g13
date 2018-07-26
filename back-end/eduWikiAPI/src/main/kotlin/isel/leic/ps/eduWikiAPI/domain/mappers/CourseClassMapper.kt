package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.model.CourseClass
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage

fun stagedToCourseClass(stage: CourseClassStage) = CourseClass(
        courseId = stage.courseId,
        classId = stage.classId,
        termId = stage.termId,
        createdBy = stage.createdBy
)