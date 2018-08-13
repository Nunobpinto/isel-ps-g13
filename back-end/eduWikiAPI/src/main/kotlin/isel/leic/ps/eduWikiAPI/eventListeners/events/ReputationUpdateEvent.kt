package isel.leic.ps.eduWikiAPI.eventListeners.events

data class ReputationUpdateEvent(
        val owner: String,
        val pointsGiven: Int,
        val givenBy: String,
        val actionId: Int
)
