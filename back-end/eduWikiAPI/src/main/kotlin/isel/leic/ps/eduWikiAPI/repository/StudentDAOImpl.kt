package isel.leic.ps.eduWikiAPI.repository

import org.springframework.stereotype.Repository

@Repository
class StudentDAOImpl{

    companion object {
        //TABLE NAMES
        const val STUDENT_TABLE = "student"
        const val STUDENT_REPORT_TABLE = "student_report"
        // FIELDS
        const val REPUTATION_ID = "reputation_id"
        const val REPUTATION_POINTS = "reputation_points"
        const val REPUTATION_RANK = "reputation_rank"
        const val STUDENT_REPORT_ID = "report_id"
        const val STUDENT_REPORTED_BY = "reported_by"
        const val STUDENT_USERNAME = "student_username"
        const val STUDENT_REASON = "student_reason"
        const val STUDENT_TIMESTAMP = "time_stamp"
    }


}