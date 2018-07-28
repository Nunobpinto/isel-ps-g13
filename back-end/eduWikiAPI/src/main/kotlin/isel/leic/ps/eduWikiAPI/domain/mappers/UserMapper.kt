package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.model.User


fun toUser(inputModel: UserInputModel) = User(
        username = inputModel.username,
        familyName = inputModel.familyName,
        givenName = inputModel.givenName,
        organizationEmail = inputModel.organizationEmail,
        password = inputModel.password,
        personalEmail = inputModel.personalEmail
)