package isel.leic.ps.eduWikiAPI.eventListeners.events

import isel.leic.ps.eduWikiAPI.domain.model.User

data class OnRegistrationEvent (
        val user: User = User()
)