package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.Student
import isel.leic.ps.eduWikiAPI.service.interfaces.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {
    override fun getUser(username: String) : Student {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveUser(username: String) : Student{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUser(username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}