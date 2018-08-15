package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.repository.interfaces.TermDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TermDAOImpl : TermDAO {

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

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllTerms(): List<Term> =
            jdbi.open().attach(TermDAOJdbi::class.java).getAllTerms()

    override fun getTerm(termId: Int): Optional<Term> =
            jdbi.open().attach(TermDAOJdbi::class.java).getTerm(termId)

    interface TermDAOJdbi : TermDAO {
        @SqlQuery("SELECT * FROM $TERM_TABLE WHERE $TERM_ID = :termId")
        override fun getTerm(termId: Int): Optional<Term>

        @SqlQuery("SELECT * FROM $TERM_TABLE")
        override fun getAllTerms(): List<Term>
    }
}