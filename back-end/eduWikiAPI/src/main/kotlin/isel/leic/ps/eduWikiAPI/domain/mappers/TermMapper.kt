package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TermCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TermOutputModel

fun toTermOutputModel(term: Term) = TermOutputModel(
        termId = term.termId,
        shortName = term.shortName,
        year = term.year,
        type = term.type,
        timestamp = term.timestamp
)

fun toTermCollectionOutputModel(termList: List<TermOutputModel>) = TermCollectionOutputModel(
        termList = termList
)