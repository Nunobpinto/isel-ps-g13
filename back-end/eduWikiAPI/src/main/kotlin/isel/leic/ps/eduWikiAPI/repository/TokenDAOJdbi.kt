package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.ValidationToken
import isel.leic.ps.eduWikiAPI.repository.interfaces.TokenDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.*

interface TokenDAOJdbi : TokenDAO {

    companion object {
        const val VALIDATION_TOKEN_TABLE = "validation_token"
        const val VALIDATION_TOKEN = "token"
        const val VALIDATION_TOKEN_ID = "token_id"
        const val VALIDATION_TOKEN_DATE = "validation_date"
    }

    @SqlUpdate("INSERT INTO $VALIDATION_TOKEN_TABLE (" +
            "$VALIDATION_TOKEN, "+
            VALIDATION_TOKEN_DATE +
            ") VALUES ( " +
            ":validationToken.token, " +
            ":validationToken.date " +
            ")")
    @GetGeneratedKeys
    override fun saveToken(validationToken: ValidationToken): ValidationToken

    @SqlQuery("SELECT * FROM $VALIDATION_TOKEN_TABLE WHERE $VALIDATION_TOKEN = :token")
    override fun getToken(token: UUID): Optional<ValidationToken>

    @SqlUpdate("DELETE FROM $VALIDATION_TOKEN_TABLE WHERE $VALIDATION_TOKEN = :token")
    override fun deleteToken(token: UUID): Int
}