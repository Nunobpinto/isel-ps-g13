package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.repository.interfaces.TermDAO
import org.jdbi.v3.sqlobject.statement.SqlQuery
import java.util.*

interface TermDAOJdbi : TermDAO {

    companion object {
        //TABLE NAME
        const val TERM_TABLE = "term"
        // FIELDS
        const val TERM_ID = "term_id"
        const val TERM_SHORT_NAME = "term_short_name"
        const val TERM_YEAR = "term_year"
        const val TERM_TYPE = "term_type"
        const val TERM_TIMESTAMP = "time_stamp"
    }

    @SqlQuery("SELECT * FROM $TERM_TABLE WHERE $TERM_ID = :termId")
    override fun getTerm(termId: Int): Optional<Term>

    @SqlQuery("SELECT * FROM $TERM_TABLE")
    override fun getAllTerms(): List<Term>
}