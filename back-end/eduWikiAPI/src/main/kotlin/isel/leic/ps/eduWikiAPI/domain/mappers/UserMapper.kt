package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Reputation
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.UserReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.AuthUserOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ReputationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.UserReportOutputModel


fun toUser(inputModel: UserInputModel, encodedPassword: String) = User(
        username = inputModel.username,
        familyName = inputModel.familyName,
        givenName = inputModel.givenName,
        email = inputModel.email,
        password = encodedPassword
)

fun toUserReport(username: String, reportedBy: String, reportInput: UserReportInputModel) = UserReport(
        username = username,
        reportedBy = reportedBy,
        reason = reportInput.reason
)

fun toAuthUserOutputModel(user: User, reputation: Reputation) = AuthUserOutputModel(
        username = user.username,
        givenName = user.givenName,
        familyName = user.familyName,
        email = user.email,
        reputation = toReputationOutputModel(reputation)
)

fun toReputationOutputModel(reputation: Reputation) = ReputationOutputModel(
        points = reputation.points,
        role = reputation.role
)

fun toUserOutputModel(user: User) = UserOutputModel(
        username = user.username
)

fun toUserReportOutput(userReport: UserReport) = UserReportOutputModel(
        reportId = userReport.reportId,
        username = userReport.reason,
        reportedBy = userReport.reportedBy,
        timestamp = userReport.timestamp,
        reason = userReport.reason
)

fun toUserReportCollectionOutputModel(allReportsOfUser: List<UserReport>) = UserReportCollectionOutputModel(
        reports = allReportsOfUser.map { toUserReportOutput(it) }
)