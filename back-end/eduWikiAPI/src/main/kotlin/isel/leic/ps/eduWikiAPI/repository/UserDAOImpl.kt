package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VOTES
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_VERSION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_VOTES
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_ROLE
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_TABLE
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_USER
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.MASTER_SCHEMA
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserDAOImpl : UserDAO {

    companion object {
        // TABLE NAMES
        const val USER_TABLE = "user_account"
        const val USER_REPORT_TABLE = "user_report"
        const val USER_COURSE_CLASS_TABLE = "user_course_class"
        const val USER_PROGRAMME_TABLE = "user_programme"
        // USER FIELDS
        const val USER_USERNAME = "user_username"
        const val USER_PASSWORD = "user_password"
        const val USER_GIVEN_NAME = "user_given_name"
        const val USER_FAMILY_NAME = "user_family_name"
        const val USER_EMAIL = "user_email"
        const val USER_CONFIRMED_FLAG = "user_confirmed"
        const val USER_LOCKED = "user_locked"
        // USER_REPORT FIELDS
        const val USER_REPORT_USER = "user_username"
        const val USER_REPORT_REPORT_ID = "report_id"
        const val USER_REPORT_LOG_ID = "log_id"
        const val USER_REPORT_REASON = "reason"
        const val USER_REPORT_REPORTED_BY = "reported_by"
        const val USER_REPORT_TIMESTAMP = "time_stamp"
        // USER_COURSE_CLASS FIELDS
        const val USER_COURSE_CLASS_COURSE_ID = "course_id"
        const val USER_COURSE_CLASS_CLASS_ID = "course_class_id"
        // USER_PROGRAMME FIELDS
        const val USER_PROGRAMME_PROGRAMME_ID = "programme_id"
    }

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getUser(username: String): Optional<User> =
            jdbi.open().attach(UserDAOJdbi::class.java).getUser(username)

    override fun createUser(user: User): User =
            jdbi.open().attach(UserDAOJdbi::class.java).createUser(user)

    override fun confirmUser(username: String): User =
            jdbi.open().attach(UserDAOJdbi::class.java).confirmUser(username)

    override fun getCoursesOfUser(username: String): List<Course> =
            jdbi.open().attach(UserDAOJdbi::class.java).getCoursesOfUser(username)

    override fun getClassesOfUser(username: String): List<UserCourseClass> =
            jdbi.open().attach(UserDAOJdbi::class.java).getClassesOfUser(username)

    override fun getProgrammeOfUser(username: String): Optional<Programme> =
            jdbi.open().attach(UserDAOJdbi::class.java).getProgrammeOfUser(username)

    override fun addProgrammeToUser(username: String, programmeId: Int): UserProgramme =
            jdbi.open().attach(UserDAOJdbi::class.java).addProgrammeToUser(username, programmeId)

    override fun addCourseToUser(userCourseClass: UserCourseClass): UserCourseClass =
            jdbi.open().attach(UserDAOJdbi::class.java).addCourseToUser(userCourseClass)

    override fun addClassToUser(userCourseClass: UserCourseClass): UserCourseClass =
            jdbi.open().attach(UserDAOJdbi::class.java).addClassToUser(userCourseClass)

    override fun deleteAllCoursesOfUser(username: String): Int =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteAllCoursesOfUser(username)

    override fun deleteProgramme(username: String): Int =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteProgramme(username)

    override fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteSpecificCourseOfUser(username, courseId)

    override fun reportUser(report: UserReport): UserReport =
            jdbi.open().attach(UserDAOJdbi::class.java).reportUser(report)

    override fun updateUser(newUser: User): User =
            jdbi.open().attach(UserDAOJdbi::class.java).updateUser(newUser)

    override fun deleteUser(username: String): Int =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteUser(username)

    override fun deleteSpecificReportOfUser(username: String, reportId: Int): Int =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteSpecificReportOfUser(username, reportId)

    override fun getSpecificReportOfUser(username: String, reportId: Int): Optional<UserReport> =
            jdbi.open().attach(UserDAOJdbi::class.java).getSpecificReportOfUser(username, reportId)

    override fun getAllReportsOfUser(username: String): List<UserReport> =
            jdbi.open().attach(UserDAOJdbi::class.java).getAllReportsOfUser(username)

    override fun deleteAllClassesOfUser(username: String): Int =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteAllClassesOfUser(username)

    override fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteSpecificClassOfUser(username, courseClassId)

    override fun getDevs(): List<User> =
            jdbi.open().attach(UserDAOJdbi::class.java).getDevs()

    override fun getUsersByRole(role: String): List<User> =
            jdbi.open().attach(UserDAOJdbi::class.java).getUsersByRole(role)

    override fun lockUser(username: String): User =
            jdbi.open().attach(UserDAOJdbi::class.java).lockUser(username)

    override fun deleteAllReportsOnUser(username: String) =
            jdbi.open().attach(UserDAOJdbi::class.java).deleteAllReportsOnUser(username)

    override fun getUserByEmail(email: String): Optional<User> =
            jdbi.open().attach(UserDAOJdbi::class.java).getUserByEmail(email)

    interface UserDAOJdbi : UserDAO {

        @SqlQuery("SELECT * FROM :schema.$USER_TABLE where $USER_USERNAME = :username")
        override fun getUser(username: String): Optional<User>

        @SqlUpdate("INSERT INTO :schema.$USER_TABLE (" +
                "$USER_USERNAME," +
                "$USER_PASSWORD," +
                "$USER_GIVEN_NAME," +
                "$USER_FAMILY_NAME," +
                "$USER_CONFIRMED_FLAG," +
                "$USER_EMAIL, "+
                "$USER_LOCKED "+
                ") VALUES ( " +
                ":user.username," +
                ":user.password," +
                ":user.givenName," +
                ":user.familyName," +
                ":user.confirmed," +
                ":user.email, " +
                ":user.locked)"
        )
        @GetGeneratedKeys
        override fun createUser(user: User): User

        @SqlUpdate("UPDATE :schema.$USER_TABLE " +
                "SET $USER_CONFIRMED_FLAG = true " +
                "WHERE $USER_USERNAME = :username")
        @GetGeneratedKeys
        override fun confirmUser(username: String): User

        @SqlQuery(
                "SELECT " +
                        "C.$COURSE_ID, " +
                        "C.$COURSE_LOG_ID, " +
                        "C.$COURSE_VERSION, " +
                        "C.$COURSE_FULL_NAME, " +
                        "C.$COURSE_SHORT_NAME, " +
                        "C.$COURSE_VOTES, " +
                        "C.$COURSE_TIMESTAMP, " +
                        "C.$COURSE_CREATED_BY " +
                        "FROM :schema.$USER_COURSE_CLASS_TABLE AS U " +
                        "INNER JOIN :schema.$COURSE_TABLE AS C " +
                        "ON U.$USER_COURSE_CLASS_COURSE_ID = C.$COURSE_ID " +
                        "WHERE $USER_USERNAME = :username"
        )
        override fun getCoursesOfUser(username: String): List<Course>

        @SqlQuery("SELECT * FROM :schema.$USER_COURSE_CLASS_TABLE WHERE $USER_USERNAME = :username")
        override fun getClassesOfUser(username: String): List<UserCourseClass>

        @SqlQuery(
                "SELECT " +
                        "P.$PROGRAMME_ID, " +
                        "P.$PROGRAMME_VERSION, " +
                        "P.$PROGRAMME_LOG_ID, " +
                        "P.$PROGRAMME_VOTES, " +
                        "P.$PROGRAMME_CREATED_BY, " +
                        "P.$PROGRAMME_FULL_NAME, " +
                        "P.$PROGRAMME_SHORT_NAME, " +
                        "P.$PROGRAMME_ACADEMIC_DEGREE, " +
                        "P.$PROGRAMME_TOTAL_CREDITS, " +
                        "P.$PROGRAMME_DURATION, " +
                        "P.$PROGRAMME_TIMESTAMP " +
                        "FROM :schema.$USER_PROGRAMME_TABLE AS U " +
                        "INNER JOIN :schema.$PROGRAMME_TABLE AS P " +
                        "ON U.$USER_PROGRAMME_PROGRAMME_ID = P.$PROGRAMME_ID " +
                        "WHERE U.$USER_USERNAME = :username"
        )
        override fun getProgrammeOfUser(username: String): Optional<Programme>

        @SqlUpdate("INSERT INTO :schema.$USER_COURSE_CLASS_TABLE (" +
                "$USER_USERNAME, " +
                "$USER_COURSE_CLASS_COURSE_ID)"+
                " VALUES ( " +
                ":userCourseClass.username," +
                ":userCourseClass.courseId)")
        @GetGeneratedKeys
        override fun addCourseToUser(userCourseClass: UserCourseClass): UserCourseClass

        @SqlUpdate("UPDATE :schema.$USER_COURSE_CLASS_TABLE " +
                "SET $USER_COURSE_CLASS_CLASS_ID = :userCourseClass.courseClassId " +
                "WHERE $USER_USERNAME = :userCourseClass.username " +
                "AND $USER_COURSE_CLASS_COURSE_ID = :userCourseClass.courseId"
        )
        @GetGeneratedKeys
        override fun addClassToUser(userCourseClass: UserCourseClass): UserCourseClass


        @SqlUpdate("INSERT INTO :schema.$USER_PROGRAMME_TABLE (" +
                "$USER_USERNAME," +
                USER_PROGRAMME_PROGRAMME_ID +
                ") VALUES ( " +
                ":username," +
                ":programmeId " +
                ")")
        @GetGeneratedKeys
        override fun addProgrammeToUser(username: String, programmeId: Int): UserProgramme

        @SqlUpdate("DELETE FROM :schema.$USER_COURSE_CLASS_TABLE WHERE $USER_USERNAME = :username")
        override fun deleteAllCoursesOfUser(username: String): Int

        @SqlUpdate("DELETE FROM :schema.$USER_COURSE_CLASS_TABLE " +
                "WHERE $USER_USERNAME = :username " +
                "AND $USER_COURSE_CLASS_COURSE_ID = :courseId")
        override fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int

        @SqlUpdate("INSERT INTO :schema.$USER_REPORT_TABLE (" +
                "$USER_USERNAME, " +
                "$USER_REPORT_REASON, " +
                "$USER_REPORT_REPORTED_BY, " +
                "$USER_REPORT_TIMESTAMP)" +
                "VALUES (" +
                ":report.username," +
                ":report.reason, " +
                ":report.reportedBy, " +
                ":report.timestamp " +
                ")")
        @GetGeneratedKeys
        override fun reportUser(report: UserReport): UserReport

        @SqlUpdate("DELETE FROM :schema.$USER_PROGRAMME_TABLE WHERE $USER_USERNAME = :username")
        override fun deleteProgramme(username: String): Int

        @SqlUpdate("UPDATE :schema.$USER_TABLE" +
                " SET $USER_GIVEN_NAME = :newUser.givenName," +
                "$USER_FAMILY_NAME = :newUser.familyName," +
                "WHERE $USER_USERNAME = :newUser.username")
        @GetGeneratedKeys
        override fun updateUser(newUser: User): User

        @SqlUpdate("DELETE FROM :schema.$USER_TABLE WHERE $USER_USERNAME = :username")
        override fun deleteUser(username: String): Int

        @SqlUpdate("DELETE FROM :schema.$USER_REPORT_TABLE WHERE $USER_USERNAME = :username AND $USER_REPORT_REPORT_ID = :reportId")
        override fun deleteSpecificReportOfUser(username: String, reportId: Int): Int

        @SqlQuery("SELECT * FROM :schema.$USER_REPORT_TABLE WHERE $USER_USERNAME = :username")
        override fun getAllReportsOfUser(username: String): List<UserReport>

        @SqlQuery("SELECT * FROM :schema.$USER_REPORT_TABLE WHERE $USER_USERNAME = :username AND $USER_REPORT_REPORT_ID = :reportId")
        override fun getSpecificReportOfUser(username: String, reportId: Int): Optional<UserReport>

        @SqlUpdate("UPDATE :schema.$USER_COURSE_CLASS_TABLE" +
                " SET $COURSE_CLASS_ID = NULL " +
                "WHERE $USER_USERNAME = :username.username")
        override fun deleteAllClassesOfUser(username: String): Int

        @SqlUpdate("UPDATE :schema.$USER_COURSE_CLASS_TABLE" +
                " SET $COURSE_CLASS_ID = NULL " +
                "WHERE $USER_USERNAME = :username.username " +
                "AND $COURSE_CLASS_ID = :courseClassId"
        )
        override fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$USER_TABLE")
        override fun getDevs(): List<User>

        @SqlQuery(
                "SELECT " +
                        "U.$USER_USERNAME, " +
                        "U.$USER_PASSWORD, " +
                        "U.$USER_GIVEN_NAME, " +
                        "U.$USER_FAMILY_NAME, " +
                        "U.$USER_CONFIRMED_FLAG, " +
                        "U.$USER_EMAIL, " +
                        "U.$USER_LOCKED " +
                        "FROM :schema.$USER_TABLE AS U " +
                        "INNER JOIN :schema.$REPUTATION_TABLE AS R " +
                        "ON U.$USER_USERNAME = R.$REPUTATION_USER " +
                        "WHERE R.$REPUTATION_ROLE = :role"
        )
        override fun getUsersByRole(role: String): List<User>

        @SqlUpdate(
                "UPDATE :schema.$USER_TABLE " +
                        "SET $USER_LOCKED = true " +
                        "WHERE $USER_USERNAME = :username"
        )
        @GetGeneratedKeys
        override fun lockUser(username: String): User

        @SqlUpdate("DELETE FROM :schema.$USER_REPORT_TABLE WHERE $USER_USERNAME = :username")
        override fun deleteAllReportsOnUser(username: String)

        @SqlQuery("SELECT * FROM :schema.$USER_TABLE where $USER_EMAIL = :email")
        override fun getUserByEmail(email: String): Optional<User>

    }

}