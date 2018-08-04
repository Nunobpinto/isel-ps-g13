package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.User

data class OnRegistrationEvent (
        val user: User = User()
)