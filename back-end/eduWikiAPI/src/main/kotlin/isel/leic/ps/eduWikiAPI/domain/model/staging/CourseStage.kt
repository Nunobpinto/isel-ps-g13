package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VOTES
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseStage (
        @ColumnName(COURSE_STAGE_ID)
        val courseId: Int = 0,
        @ColumnName(ORG_ID)
        val organizationId: Int = 0,
        @ColumnName(COURSE_FULL_NAME)
        val fullName: String = "",
        @ColumnName(COURSE_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(COURSE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(0)
)