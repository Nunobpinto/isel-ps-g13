package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import isel.leic.ps.eduWikiAPI.domain.outputModel.AuthUserOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.UserOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.UserReportOutputModel


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

fun toAuthUserOutputModel(user: User) = AuthUserOutputModel(
        username = user.username,
        givenName = user.givenName,
        familyName = user.givenName,
        personalEmail = user.personalEmail,
        organizationEmail = user.organizationEmail,
        userPrivilege = user.userPrivilege,
        userReputation = user.userReputation
)

fun toUserOutputModel(user: User) = UserOutputModel(
        username = user.username,
        userReputation = user.userReputation
)

fun toUserReportOutput(userReport: UserReport) = UserReportOutputModel(
        reportId = userReport.reportId,
        username = userReport.reason,
        reportedBy = userReport.reportedBy,
        timestamp = userReport.timestamp,
        reason = userReport.reason

)