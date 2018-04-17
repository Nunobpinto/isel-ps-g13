package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.model.User

interface UserService {

    fun getUser(username: String): User

    fun saveUser(username: String): User

    fun deleteUser(username: String)
}