package isel.leic.ps.eduWikiAPI.domain.model

import org.jdbi.v3.core.mapper.reflect.ColumnName
import isel.leic.ps.eduWikiAPI.repository.TokenDAOJdbi.Companion.VALIDATION_TOKEN_ID
import isel.leic.ps.eduWikiAPI.repository.TokenDAOJdbi.Companion.VALIDATION_TOKEN
import isel.leic.ps.eduWikiAPI.repository.TokenDAOJdbi.Companion.VALIDATION_TOKEN_DATE
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

import java.util.*

data class ValidationToken (
        @ColumnName(VALIDATION_TOKEN_ID)
        val id: Int = 0,
        @ColumnName(VALIDATION_TOKEN)
        val token: UUID = UUID.randomUUID(),
        @ColumnName(VALIDATION_TOKEN_DATE)
        val date: Timestamp =  Timestamp.valueOf(LocalDateTime.now().plusDays(1))
)