package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.model.UserCourseClass
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserCourseClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserCourseOutputModel

fun toUserCourseClass(username: String, inputModel: UserCourseClassInputModel) = UserCourseClass(
        username = username,
        courseId = inputModel.courseId,
        courseClassId = inputModel.courseClassId
)

fun toUserCourseOutputModel(userCourseClass: UserCourseClass) = UserCourseOutputModel(
        username = userCourseClass.username,
        courseId = userCourseClass.courseId
)

fun toUserCourseClassOutputModel(userCourseClass: UserCourseClass) = UserCourseClassOutputModel(
        username = userCourseClass.username,
        courseId = userCourseClass.courseId,
        courseClassId = userCourseClass.courseClassId!!
)