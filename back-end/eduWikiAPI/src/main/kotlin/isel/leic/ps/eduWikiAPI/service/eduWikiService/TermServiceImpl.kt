package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.domain.mappers.toTermCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.toTermOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TermCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TermOutputModel
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.interfaces.TermDAO
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.TermService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class TermServiceImpl: TermService {

    @Autowired
    lateinit var termDAO: TermDAO
    override fun getAllTerms(): TermCollectionOutputModel {
        val terms = termDAO.getAllTerms()
        return toTermCollectionOutputModel(terms.map { toTermOutputModel(it) })
    }

    override fun getSpecificTerm(termId: Int): TermOutputModel {
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Please try with other id") }
        return toTermOutputModel(term)
    }
}