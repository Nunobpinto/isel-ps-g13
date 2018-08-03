package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.model.UserCourseClass

fun toUserCourseClass(username: String, inputModel: UserCourseClassInputModel) = UserCourseClass(
        username = username,
        courseId = inputModel.courseId,
        termId = inputModel.termId,
        classId = inputModel.classId
)