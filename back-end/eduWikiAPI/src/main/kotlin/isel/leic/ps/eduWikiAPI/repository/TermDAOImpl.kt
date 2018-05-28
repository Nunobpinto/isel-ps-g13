package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.repository.interfaces.TermDAO
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
    lateinit var dbi: Jdbi

    override fun getTerm(termId: Int): Term= dbi.withHandle<Term, Exception> {
        it.createQuery("").mapTo(Term::class.java).findOnly()
    }

    override fun getAllTerms(): List<Term> = dbi.withHandle<List<Term>, Exception> {
        it.createQuery("").mapTo(Term::class.java).list()
    }

    override fun deleteTerm(termId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute("")
    }

    override fun deleteAllTerm(): Int = dbi.withHandle<Int, Exception> {
        it.execute("")
    }

    override fun createTerm(term: Term) = dbi.useHandle<Exception> {
        it.execute("")
    }
}