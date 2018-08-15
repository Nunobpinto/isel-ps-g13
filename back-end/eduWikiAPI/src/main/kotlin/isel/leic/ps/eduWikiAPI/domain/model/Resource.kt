package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ResourceDAOImpl.Companion.RESOURCE_BYTE_SEQUENCE
import isel.leic.ps.eduWikiAPI.repository.ResourceDAOImpl.Companion.RESOURCE_CONTENT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ResourceDAOImpl.Companion.RESOURCE_ORIGINAL_FILENAME
import isel.leic.ps.eduWikiAPI.repository.ResourceDAOImpl.Companion.RESOURCE_UUID
import isel.leic.ps.eduWikiAPI.repository.ResourceDAOImpl.Companion.RESOURCE_SIZE
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.*

data class Resource (
        @ColumnName(RESOURCE_UUID)
        val uuId: UUID = UUID.randomUUID(),
        @ColumnName(RESOURCE_BYTE_SEQUENCE)
        val byteSequence: ByteArray = ByteArray(0),
        @ColumnName(RESOURCE_CONTENT_TYPE)
        val contentType: String = "",
        @ColumnName(RESOURCE_ORIGINAL_FILENAME)
        val originalFilename: String = "",
        @ColumnName(RESOURCE_SIZE)
        val size: Long = 0
)