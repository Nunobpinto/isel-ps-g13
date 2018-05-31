package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_VOTE
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_CONTACT
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_REPORT_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class OrganizationReport (
        @ColumnName(ORG_REPORT_ID)
        val reportId: Int = 0,
        @ColumnName(ORG_ID)
        val organization_id: Int = 0,
        @ColumnName(ORG_FULL_NAME)
        val fullName: String? = null,
        @ColumnName(ORG_SHORT_NAME)
        val shortName: String? = null,
        @ColumnName(ORG_ADDRESS)
        val address: String? = null,
        @ColumnName(ORG_CONTACT)
        val contact: String? = null,
        @ColumnName(ORG_REPORTED_BY)
        val reportedBy:String = "",
        @ColumnName(ORG_VOTE)
        val votes: Int = 0,
        @ColumnName(ORG_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1)
)