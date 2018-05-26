package isel.leic.ps.eduWikiAPI.domain.model;

import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_ID
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_TYPE
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_YEAR
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class Term (
        @ColumnName(TERM_ID)
        val id: Int = 0,
        @ColumnName(TERM_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(TERM_YEAR)
        val year: String = "",
        @ColumnName(TERM_TYPE)
        val type: String = ""
)