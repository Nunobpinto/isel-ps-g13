package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.domain.enums.CourseMiscUnitType
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TERM_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName

class CourseMiscUnitStage(
        @ColumnName(COURSE_MISC_UNIT_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(COURSE_MISC_UNIT_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_TERM_ID)
        val termId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_TYPE)
        val miscType: CourseMiscUnitType
)