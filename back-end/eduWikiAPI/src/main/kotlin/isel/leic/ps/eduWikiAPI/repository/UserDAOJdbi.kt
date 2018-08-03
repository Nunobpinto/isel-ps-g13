package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.UserCourseClass
import isel.leic.ps.eduWikiAPI.domain.model.UserProgramme
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.UserReportOutputModel
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.*

interface UserDAOJdbi : UserDAO {

    companion object {
        // TABLE NAMES
        const val USER_TABLE = "user_account"
        const val USER_REPORT_TABLE = "user_report"
        const val USER_COURSE_CLASS_TABLE = "user_course_class"
        const val USER_PROGRAMME_TABLE = "user_programme"
        // FIELDS
        const val USER_USERNAME = "user_username"
        const val USER_PASSWORD = "user_password"
        const val USER_GIVEN_NAME = "user_given_name"
        const val USER_FAMILY_NAME = "user_family_name"
        const val USER_PERSONAL_EMAIL = "user_personal_email"
        const val USER_ORG_EMAIL = "user_organization_email"
        const val COURSE_ID = "course_id"
        const val CLASS_ID = "class_id"
        const val TERM_ID = "term_id"
        const val PROGRAMME_ID = "programme_id"
        const val REASON = "reason"
        const val REPORTED_BY = "reported_by"
        const val TIMESTAMP = "time_stamp"
        const val REPORT_ID = "report_id"

    }

    @SqlQuery("SELECT * FROM $USER_TABLE where $USER_USERNAME = :username")
    override fun getUser(username: String): Optional<User>

    @SqlUpdate("INSERT INTO $USER_TABLE (" +
            "$USER_USERNAME," +
            "$USER_PASSWORD," +
            "$USER_GIVEN_NAME," +
            "$USER_FAMILY_NAME," +
            "$USER_PERSONAL_EMAIL," +
            USER_ORG_EMAIL +
            ") VALUES ( " +
            ":user.username," +
            ":user.password," +
            ":user.givenName," +
            ":user.familyName," +
            ":user.personalEmail," +
            ":user.organizationEmail " +
            ")")
    @GetGeneratedKeys
    override fun createUser(user: User): User

    @SqlQuery("SELECT $COURSE_ID FROM $USER_COURSE_CLASS_TABLE WHERE $USER_USERNAME = :username")
    override fun getCoursesOfUser(username: String): List<Int>

    @SqlQuery("SELECT * FROM $USER_COURSE_CLASS_TABLE WHERE $USER_USERNAME = :username")
    override fun getClassesOfUser(username: String): List<UserCourseClass>

    @SqlQuery("SELECT $PROGRAMME_ID FROM $USER_PROGRAMME_TABLE WHERE $USER_USERNAME = :username")
    override fun getProgrammeOfUser(username: String): Int

    @SqlUpdate("INSERT INTO $USER_COURSE_CLASS_TABLE (" +
            "$USER_USERNAME," +
            COURSE_CLASS_ID +
            ") VALUES ( " +
            ":userCourseClass.username," +
            ":userCourseClass.courseId," +
            ":userCourseClass.courseClassId)")
    @GetGeneratedKeys
    override fun addCourseToUser(userCourseClass: UserCourseClass): UserCourseClass

    @SqlUpdate("Update $USER_COURSE_CLASS_TABLE" +
            " SET $COURSE_CLASS_ID = :userCourseClass.courseClassId " +
            "WHERE $USER_USERNAME = :userCourseClass.username " +
            "AND $COURSE_ID = :userCourseClass.courseId"
    )
    @GetGeneratedKeys
    override fun addClassToUser(userCourseClass: UserCourseClass): UserCourseClass


    @SqlUpdate("INSERT INTO $USER_PROGRAMME_TABLE (" +
            "$USER_USERNAME," +
            PROGRAMME_ID +
            ") VALUES ( " +
            ":username," +
            ":programmeId " +
            ")")
    @GetGeneratedKeys
    override fun addProgrammeToUser(username: String, programmeId: Int): UserProgramme

    @SqlUpdate("DELETE FROM $USER_COURSE_CLASS_TABLE WHERE $USER_USERNAME = :username")
    override fun deleteAllCoursesOfUser(username: String): Int

    @SqlUpdate("DELETE FROM $USER_COURSE_CLASS_TABLE " +
            "WHERE $USER_USERNAME = :username " +
            "AND $COURSE_ID = :courseId")
    override fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int

    @SqlUpdate("INSERT INTO $USER_REPORT_TABLE (" +
            "$USER_USERNAME," +
            "$REASON," +
            "$REPORTED_BY," +
            TIMESTAMP +
            ") VALUES ( " +
            ":report.username," +
            ":report.reason, " +
            ":report.reportedBy, " +
            ":report.timestamp " +
            ")")
    @GetGeneratedKeys
    override fun reportUser(report: UserReport): UserReport

    @SqlUpdate("DELETE FROM $USER_PROGRAMME_TABLE WHERE $USER_USERNAME = :username")
    override fun deleteProgramme(username: String): Int

    @SqlUpdate("Update $USER_TABLE" +
            " SET $USER_PASSWORD = :newUser.password," +
            "$USER_GIVEN_NAME = :newUser.givenName," +
            "$USER_FAMILY_NAME = :newUser.familyName," +
            "$USER_PERSONAL_EMAIL = :newUser.personalEmail " +
            "WHERE $USER_USERNAME = :newUser.username")
    @GetGeneratedKeys
    override fun updateUser(newUser: User): User

    @SqlUpdate("DELETE FROM $USER_TABLE WHERE $USER_USERNAME = :username")
    override fun deleteUser(username: String): Int

    @SqlUpdate("DELETE FROM $USER_REPORT_TABLE WHERE $USER_USERNAME = :username AND $REPORT_ID = :reportId")
    override fun deleteSpecificReportOfUser(username: String, reportId: Int): Int

    @SqlQuery("SELECT * FROM $USER_REPORT_TABLE WHERE $USER_USERNAME = :username")
    override fun getAllReportsOfUser(username: String): List<UserReport>

    @SqlQuery("SELECT * FROM $USER_REPORT_TABLE WHERE $USER_USERNAME = :username AND $REPORT_ID = :reportId")
    override fun getSpecficReportOfUser(username: String, reportId: Int): UserReport

    @SqlUpdate("Update $USER_COURSE_CLASS_TABLE" +
            " SET $COURSE_CLASS_ID = NULL " +
            "WHERE $USER_USERNAME = :username.username")
    override fun deleteAllClassesOfUser(username: String): Int

    @SqlUpdate("Update $USER_COURSE_CLASS_TABLE" +
            " SET $COURSE_CLASS_ID = NULL " +
            "WHERE $USER_USERNAME = :username.username " +
            "AND $COURSE_CLASS_ID = :courseClassId"
    )
    override fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int
}