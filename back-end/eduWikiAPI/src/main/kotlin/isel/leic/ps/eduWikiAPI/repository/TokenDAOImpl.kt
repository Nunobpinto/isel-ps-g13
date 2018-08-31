package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.ValidationToken
import isel.leic.ps.eduWikiAPI.repository.interfaces.TokenDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TokenDAOImpl : TokenDAO {

    companion object {
        const val VALIDATION_TOKEN_TABLE = "validation_token"
        const val VALIDATION_TOKEN = "token"
        const val VALIDATION_TOKEN_ID = "token_id"
        const val VALIDATION_TOKEN_DATE = "validation_date"
    }

    @Autowired
    lateinit var jdbi: Jdbi

    override fun saveToken(validationToken: ValidationToken): ValidationToken =
            jdbi.open().attach(TokenDAOJdbi::class.java).saveToken(validationToken)

    override fun getToken(token: UUID): Optional<ValidationToken> =
            jdbi.open().attach(TokenDAOJdbi::class.java).getToken(token)

    override fun deleteToken(token: UUID): Int =
            jdbi.open().attach(TokenDAOJdbi::class.java).deleteToken(token)

    interface TokenDAOJdbi : TokenDAO {
        @SqlUpdate("INSERT INTO :schema.$VALIDATION_TOKEN_TABLE (" +
                "$VALIDATION_TOKEN, "+
                "$VALIDATION_TOKEN_DATE)"+
                "VALUES ( " +
                ":validationToken.token, " +
                ":validationToken.date " +
                ")")
        @GetGeneratedKeys
        override fun saveToken(validationToken: ValidationToken): ValidationToken

        @SqlQuery("SELECT * FROM :schema.$VALIDATION_TOKEN_TABLE WHERE $VALIDATION_TOKEN = :token")
        override fun getToken(token: UUID): Optional<ValidationToken>

        @SqlUpdate("DELETE FROM :schema.$VALIDATION_TOKEN_TABLE WHERE $VALIDATION_TOKEN = :token")
        override fun deleteToken(token: UUID): Int
    }

}