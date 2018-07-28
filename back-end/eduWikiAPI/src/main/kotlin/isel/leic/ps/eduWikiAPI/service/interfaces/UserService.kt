package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.model.User


interface UserService {

    fun getUser(username: String): User

    fun saveUser(inputUser: UserInputModel): User

    fun deleteUser(username: String)
}