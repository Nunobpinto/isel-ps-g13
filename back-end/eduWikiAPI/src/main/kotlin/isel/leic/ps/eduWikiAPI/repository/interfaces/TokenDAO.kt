package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.ValidationToken
import java.util.*

interface TokenDAO {

    fun saveToken(validationToken: ValidationToken): ValidationToken

    fun getToken(token: UUID): Optional<ValidationToken>

    fun deleteToken(token: UUID): Int
}