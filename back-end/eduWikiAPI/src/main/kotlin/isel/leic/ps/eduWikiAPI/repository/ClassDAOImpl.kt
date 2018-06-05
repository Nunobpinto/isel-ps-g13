package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ClassDAOImpl : ClassDAO {

    companion object {
        //TABLE NAMES
        const val CLASS_TABLE = "class"
        const val COURSE_CLASS_TABLE = "course_class"
        const val CLASS_REPORT_TABLE = "class_report"
        const val CLASS_STAGE_TABLE = "class_stage"
        const val CLASS_VERSION_TABLE = "class_stage"
        // FIELDS
        const val CLASS_TERM_ID = "term_id"
        const val CLASS_ID = "class_id"
        const val CLASS_VERSION = "class_version"
        const val CLASS_NAME = "class_name"
        const val CLASS_CREATED_BY = "created_by"
        const val CLASS_TIMESTAMP = "timestamp"
        const val CLASS_VOTES = "votes"
        const val CLASS_STAGE_ID = "course_stage_id"
        // COURSE_CLASS_FIELDS
        const val CRS_CLASS_CLASS_ID = "class_id"
        const val CRS_CLASS_TERM_ID = "term_id"
        const val CRS_CLASS_COURSE_ID = "course_id"
        const val CRS_CLASS_VOTES = "votes"
        const val CRS_CLASS_TIMESTAMP = "time_stamp"
    }

    @Autowired
    lateinit var dbi: Jdbi

}