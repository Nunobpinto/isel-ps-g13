package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Student


interface UserService {

    fun getUser(username: String): Student

    fun saveUser(username: String): Student

    fun deleteUser(username: String)
}