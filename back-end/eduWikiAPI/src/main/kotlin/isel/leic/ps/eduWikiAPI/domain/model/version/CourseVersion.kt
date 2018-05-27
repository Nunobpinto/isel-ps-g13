package isel.leic.ps.eduWikiAPI.domain.model.version

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseVersion (
        @ColumnName("course_id")
        val courseId: Int = 0,
        @ColumnName("organization_id")
        val organizationId: Int = 0,
        @ColumnName("course_full_name")
        val fullName: String = "",
        @ColumnName("course_short_name")
        val shortName: String = "",
        @ColumnName("created_by")
        val createdBy: String = "",
        @ColumnName("time_stamp")
        val timestamp: Timestamp = Timestamp(0),
        @ColumnName("course_version")
        val version: Int = 0,
        @ColumnName("course_credits")
        val credits: Int = 0,
        @ColumnName("course_optional")
        val optional: Boolean = false,
        @ColumnName("course_lectured_term")
        val lectured: String = ""
)