package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Vote
import java.util.*

interface CrudDAO<T> {

    fun create(t: T): T

    fun get(key: Int): Optional<T>

    fun delete(key: Int): Int

    fun update(t: T): Optional<T>

    fun vote(key: Int, vote: Vote): Int

}