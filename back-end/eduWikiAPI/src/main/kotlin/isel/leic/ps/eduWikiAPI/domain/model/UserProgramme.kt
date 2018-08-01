package isel.leic.ps.eduWikiAPI.domain.model

import org.jdbi.v3.core.mapper.reflect.ColumnName
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_USERNAME
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.PROGRAMME_ID

data class UserProgramme (
        @ColumnName(USER_USERNAME)
        val username: String = "",
        @ColumnName(PROGRAMME_ID)
        val programmeId: Int = 0
)