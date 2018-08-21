package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import isel.leic.ps.eduWikiAPI.repository.interfaces.ResourceDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ResourceDAOImpl : ResourceDAO {

    companion object {

        //TABLES
        const val RESOURCE_TABLE = "resource"
        //FIELDS
        const val RESOURCE_BYTE_SEQUENCE = "byte_sequence"
        const val RESOURCE_CONTENT_TYPE = "content_type"
        const val RESOURCE_ORIGINAL_FILENAME = "original_filename"
        const val RESOURCE_UUID = "uuid"
        const val RESOURCE_SIZE = "size"
    }

    @Qualifier("ResourcesJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun storeResource(uuId: UUID, byteSequence: ByteArray, contentType: String, originalFilename: String, size: Long): Resource =
            jdbi.open().attach(ResourceDAOJdbi::class.java).storeResource(uuId, byteSequence, contentType, originalFilename, size)

    override fun getResource(uuId: UUID): Optional<Resource> =
            jdbi.open().attach(ResourceDAOJdbi::class.java).getResource(uuId)

    override fun batchDeleteResources(uuIds: List<UUID>): IntArray =
            jdbi.open().attach(ResourceDAOJdbi::class.java).batchDeleteResources(uuIds)

    override fun deleteSpecificResource(uuId: UUID): Int =
            jdbi.open().attach(ResourceDAOJdbi::class.java).deleteSpecificResource(uuId)

    internal interface ResourceDAOJdbi : ResourceDAO {

        @SqlUpdate(
                "INSERT INTO :schema.$RESOURCE_TABLE ( " +
                        "$RESOURCE_UUID, " +
                        "$RESOURCE_BYTE_SEQUENCE, " +
                        "$RESOURCE_CONTENT_TYPE, " +
                        "$RESOURCE_ORIGINAL_FILENAME, " +
                        "$RESOURCE_SIZE " +
                        ") " +
                        "VALUES (:uuId, :byteSequence, :contentType, :originalFilename, :size)"
        )
        @GetGeneratedKeys
        override fun storeResource(uuId: UUID, byteSequence: ByteArray, contentType: String, originalFilename: String, size: Long): Resource

        @SqlQuery(
                "SELECT * FROM :schema.$RESOURCE_TABLE WHERE $RESOURCE_UUID = :uuId"
        )
        override fun getResource(uuId: UUID): Optional<Resource>

        @SqlBatch(
                "DELETE FROM :schema.$RESOURCE_TABLE WHERE $RESOURCE_UUID = :uuIds"
        )
        override fun batchDeleteResources(uuIds: List<UUID>): IntArray

        @SqlUpdate(
                "DELETE FROM :schema.$RESOURCE_TABLE WHERE $RESOURCE_UUID = :uuId"
        )
        override fun deleteSpecificResource(uuId: UUID): Int
    }

}