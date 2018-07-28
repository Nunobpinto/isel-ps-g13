package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi
import isel.leic.ps.eduWikiAPI.service.interfaces.UserService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.Reputation
import isel.leic.ps.eduWikiAPI.exceptions.BadRequestException
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi
import java.util.regex.Pattern

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getUser(username: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    override fun saveUser(inputUser: UserInputModel): User {
        val user = toUser(inputUser)
        val valid = isEmailValid(user.organizationEmail)
        if(!valid){
            throw BadRequestException(msg="invalid organization email", action = "Get an actual organization email")
        }
        return jdbi.inTransaction<User,Exception> {
            val userDao = it.attach(UserDAOJdbi::class.java)
            val repDao = it.attach(ReputationDAOJdbi::class.java)
            val user1= userDao.createUser(user)
            val reputation = Reputation(
                    reputationPoints = 1,
                    reputationRole = "ROLE_BEGINNER",
                    username = user1.username
            )
            repDao.saveNewUser(reputation)
            user1
        }

    }

    override fun deleteUser(username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}