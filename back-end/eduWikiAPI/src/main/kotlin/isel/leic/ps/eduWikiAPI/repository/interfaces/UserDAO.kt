package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.User
import java.util.*

interface UserDAO {

    fun getUser(username: String): Optional<User>

    fun createUser(user: User): User
}