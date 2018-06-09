package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.repository.interfaces.TermDAO
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

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
    }

    @Autowired
    lateinit var handle: Handle

    override fun getTerm(termId: Int) =
        handle.createQuery("").mapTo(Term::class.java).findFirst()

    override fun getAllTerms() =
        handle.createQuery("").mapTo(Term::class.java).list()

    override fun deleteTerm(termId: Int) =
        handle.createUpdate("").execute()

    override fun deleteAllTerm() =
        handle.createUpdate("").execute()

    override fun createTerm(term: Term) =
        handle.createUpdate("")
                .executeAndReturnGeneratedKeys()
                .mapTo(Term::class.java)
                .findFirst()

}