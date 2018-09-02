package isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces

import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TermCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TermOutputModel

interface TermService {

    fun getAllTerms(): TermCollectionOutputModel

    fun getSpecificTerm(termId: Int): TermOutputModel
}