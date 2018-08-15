package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Term
import java.util.*

interface TermDAO {
    /**
     * Main entities queries
     */

    fun getTerm(termId: Int) : Optional<Term>

    fun getAllTerms() : List<Term>
}