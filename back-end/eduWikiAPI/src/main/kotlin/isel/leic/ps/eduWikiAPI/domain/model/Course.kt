package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VOTES
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class Course(
        @ColumnName(COURSE_ID)
        val id: Int = 0,
        @ColumnName(OrganizationDAOImpl.ORG_ID)
        val organizationId: Int? = null,
        @ColumnName(COURSE_VERSION)
        val version: Int = 1,
        @ColumnName(COURSE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_FULL_NAME)
        val fullName: String ? = null,
        @ColumnName(COURSE_SHORT_NAME)
        val shortName: String ? = null,
        @ColumnName(LECTURED_TERM)
        val lecturedTerm: String ? = null,
        @ColumnName(PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(OPTIONAL)
        val optional: Boolean? = null,
        @ColumnName(CREDITS)
        val credits: Int? = null,
        @ColumnName(COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1)
)