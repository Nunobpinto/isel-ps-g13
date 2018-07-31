package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport


fun toUser(inputModel: UserInputModel?) = User(
        username = inputModel!!.username,
        familyName = inputModel.familyName!!,
        givenName = inputModel.givenName!!,
        organizationEmail = inputModel.organizationEmail!!,
        password = inputModel.password!!,
        personalEmail = inputModel.personalEmail!!
)

fun toUserReport(username: String, reportedBy: String, reportInput: UserReportInputModel) = UserReport(
        username = username,
        reportedBy = reportedBy,
        reason = reportInput.reason
)