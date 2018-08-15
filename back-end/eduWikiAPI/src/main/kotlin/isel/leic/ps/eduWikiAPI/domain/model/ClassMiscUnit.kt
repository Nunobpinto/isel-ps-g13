package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class ClassMiscUnit (
        @ColumnName(CLASS_MISC_UNIT_ID)
        val classMiscUnitId: Int = -1,
        @ColumnName(CLASS_MISC_UNIT_TYPE)
        val miscType: ClassMiscUnitType,
        @ColumnName(COURSE_CLASS_ID)
        val courseClassId: Int = 0
)