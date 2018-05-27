package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseProgrammeReport (
        @ColumnName(CourseDAOImpl.COURSE_ID)
        val id: Int = 0,
        @ColumnName(CourseDAOImpl.COURSE_VOTES)
        val votes: Int = 0,
        @ColumnName(CourseDAOImpl.LECTURED_TERM)
        val lecturedTerm: String? = null,
        @ColumnName(CourseDAOImpl.PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(CourseDAOImpl.OPTIONAL)
        val optional: Boolean? = null,
        @ColumnName(CourseDAOImpl.CREDITS)
        val credits: Int? = null,
        @ColumnName(CourseDAOImpl.COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1),
        @ColumnName(CourseDAOImpl.COURSE_REPORTED_BY)
        val reportedBy: String = ""
)