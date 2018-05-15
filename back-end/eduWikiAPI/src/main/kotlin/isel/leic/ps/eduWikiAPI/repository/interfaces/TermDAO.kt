package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Term

interface TermDAO {
    /**
     * Main entities queries
     */

    fun getTerm(termId: Int) : Term

    fun getAllTerms() : List<Term>

    fun deleteTerm(termId: Int) : Int

    fun deleteAllTerm() : Int

    fun createTerm(term: Term)
}