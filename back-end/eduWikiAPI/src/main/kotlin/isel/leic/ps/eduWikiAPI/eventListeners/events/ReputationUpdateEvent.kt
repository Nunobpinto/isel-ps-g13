package isel.leic.ps.eduWikiAPI.eventListeners.events

class ReputationUpdateEvent(
        val owner: String,
        val pointsGiven: Int,
        val givenBy: String,
        val actionId: Int
)
