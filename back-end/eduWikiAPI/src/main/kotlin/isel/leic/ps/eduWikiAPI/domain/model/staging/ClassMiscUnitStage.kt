package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_TYPE
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class ClassMiscUnitStage (
        @ColumnName(CLASS_MISC_UNIT_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(CLASS_MISC_UNIT_TYPE)
        val miscType: ClassMiscUnitType,
        @ColumnName(CLASS_MISC_UNIT_COURSE_CLASS_ID)
        val courseClassId: Int = 0
)