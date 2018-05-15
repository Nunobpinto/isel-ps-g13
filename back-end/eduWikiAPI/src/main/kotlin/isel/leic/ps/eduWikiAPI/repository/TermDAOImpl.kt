package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.repository.interfaces.TermDAO
import org.jdbi.v3.core.Jdbi
import org.jooq.DSLContext
import org.jooq.impl.DSL.*

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
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getTerm(termId: Int): Term= dbi.withHandle<Term, Exception> {
        it.createQuery(dsl
                .select(
                        field(TERM_ID),
                        field(TERM_SHORT_NAME),
                        field(TERM_YEAR),
                        field(TERM_TYPE)
                )
                .from(table(TERM_TABLE))
                .where(field(TERM_ID).eq(termId))
                .sql
        ).mapTo(Term::class.java).findOnly()
    }

    override fun getAllTerms(): List<Term> = dbi.withHandle<List<Term>, Exception> {
        it.createQuery(dsl
                .select(
                        field(TERM_ID),
                        field(TERM_SHORT_NAME),
                        field(TERM_YEAR),
                        field(TERM_TYPE)
                )
                .from(table(TERM_TABLE))
                .sql
        ).mapTo(Term::class.java).list()
    }

    override fun deleteTerm(termId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(TERM_TABLE))
                .where(field(TERM_ID).eq(termId))
                .sql
        )
    }

    override fun deleteAllTerm(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(TERM_TABLE))
                .sql
        )
    }

    override fun createTerm(term: Term) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(TERM_TABLE),
                        field(TERM_ID),
                        field(TERM_SHORT_NAME),
                        field(TERM_YEAR),
                        field(TERM_TYPE)
                )
                .values(
                        term.id,
                        term.shortName,
                        term.year,
                        term.type
                ).sql
        )
    }
}