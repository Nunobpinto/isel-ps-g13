package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import isel.leic.ps.eduWikiAPI.repository.interfaces.ResourceDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface ResourceDAOJdbi : ResourceDAO {

    companion object {
        //TABLES
        const val RESOURCE_TABLE = "resource"
        const val RESOURCE_VALIDATOR_TABLE = "resource_validator"

        //FIELDS
        const val RESOURCE_BYTE_SEQUENCE = "byte_sequence"
        const val RESOURCE_CONTENT_TYPE = "content_type"
        const val RESOURCE_ORIGINAL_FILENAME = "original_filename"
        const val RESOURCE_SHEET_ID = "sheet_id"
        const val RESOURCE_SIZE = "size"
        const val RESOURCE_VALIDATOR_SHEET_ID = "sheet_id"
        const val RESOURCE_VALIDATOR_VALID = "valid"
    }


    @SqlUpdate(
            "INSERT INTO $RESOURCE_TABLE ( " +
                    "$RESOURCE_SHEET_ID, " +
                    "$RESOURCE_BYTE_SEQUENCE, " +
                    "$RESOURCE_CONTENT_TYPE, " +
                    "$RESOURCE_ORIGINAL_FILENAME, " +
                    "$RESOURCE_SIZE " +
                    ") " +
                    "VALUES (:sheetId, :byteSequence, :contentType, :originalFilename, :size)"
    )
    @GetGeneratedKeys
    override fun storeResource(
            sheetId: UUID,
            byteSequence: ByteArray,
            contentType: String,
            originalFilename: String,
            size: Long
    ): Resource

    @SqlQuery(
            "SELECT * FROM $RESOURCE_TABLE WHERE $RESOURCE_SHEET_ID = :sheetId"
    )
    override fun getResource(sheetId: UUID): Optional<Resource>

    @SqlUpdate(
            "INSERT INTO $RESOURCE_VALIDATOR_TABLE ( " +
                    "$RESOURCE_VALIDATOR_SHEET_ID, " +
                    "$RESOURCE_VALIDATOR_VALID " +
                    ") " +
                    "VALUES (:sheetId, :valid)"
    )
    override fun createResourceValidatorEntry(sheetId: UUID, valid: Int)

}