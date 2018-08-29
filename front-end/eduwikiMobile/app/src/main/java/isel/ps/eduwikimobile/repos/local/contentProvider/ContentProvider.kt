package isel.ps.eduwikimobile.repos.local.contentProvider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class ContentProvider : ContentProvider() {

    companion object {
        //Tables
        const val COURSE = "Course"
        const val CLASS = "Class"
        const val PROGRAMME = "Programme"
        const val LECTURE = "Lecture"
        const val EXAM = "Exam"
        const val HOMEWORK = "Homework"
        const val WORK_ASSIGNMENT = "WorkAssignment"
        const val ORGANIZATION = "Organization"

        //columns of course table
        const val COURSE_ID = "course_id"
        const val COURSE_CREATED_BY = "created_by"
        const val COURSE_FULL_NAME = "full_name"
        const val COURSE_SHORT_NAME = "short_name"
        const val COURSE_TIMESTAMP = "timestamp"

        //columns of class table
        const val CLASS_ID = "class_id"
        const val CLASS_CREATED_BY = "created_by"
        const val CLASS_NAME = "class_name"
        const val CLASS_TERM_ID = "term_id"
        const val CLASS_LECTURED_TERM = "lectured_term"
        const val CLASS_TIMESTAMP = "timestamp"

        //columns of programme table
        const val PROGRAMME_ID = "programme_id"
        const val PROGRAMME_CREATED_BY = "created_by"
        const val PROGRAMME_FULL_NAME = "full_name"
        const val PROGRAMME_SHORT_NAME = "short_name"
        const val PROGRAMME_ACADEMIC_DEGREE = "academic_degree"
        const val PROGRAMME_TOTAL_CREDITS = "total_credits"
        const val PROGRAMME_DURATION = "duration"
        const val PROGRAMME_TIMESTAMP = "timestamp"

        //columns of exam table
        const val EXAM_ID = "exam_Id"
        const val EXAM_CREATED_BY = "created_By"
        const val EXAM_DUE_DATE = "due_date"
        const val EXAM_TYPE = "type"
        const val EXAM_PHASE = "phase"
        const val EXAM_LOCATION = "location"
        const val EXAM_TIMESTAMP = "timestamp"

        //columns of lecture table
        const val LECTURE_ID = "lecture_Id"
        const val LECTURE_CREATED_BY = "created_by"
        const val LECTURE_WEEK_DAY = "week_day"
        const val LECTURE_DURATION = "duration"
        const val LECTURE_BEGINS = "begins"
        const val LECTURE_LOCATION = "location"
        const val LECTURE_TIMESTAMP = "timestamp"

        //columns of homework table
        const val HOMEWORK_ID = "homework_id"
        const val HOMEWORK_CREATED_BY = "created_by"
        const val HOMEWORK_HOMEWORK_NAME = "homework_name"
        const val HOMEWORK_DUE_DATE = "due_date"
        const val HOMEWORK_LATE_DELIVERY = "late_delivery"
        const val HOMEWORK_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val HOMEWORK_TIMESTAMP = "timestamp"

        //columns of work-assignment table
        const val WORK_ASSIGNMENT_ID = "work_assignment_id"
        const val WORK_ASSIGNMENT_CREATED_BY = "created_by"
        const val WORK_ASSIGNMENT_PHASE = "phase"
        const val WORK_ASSIGNMENT_DUE_DATE = "due_date"
        const val WORK_ASSIGNMENT_INDIVIUAL = "individual"
        const val WORK_ASSIGNMENT_LATE_DELIVERY = "late_delivery"
        const val WORK_ASSIGNMENT_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val WORK_ASSIGNMENT_REQUIRES_REPORT = "requires_report"
        const val WORK_ASSIGNMENT_TIMESTAMP = "timestamp"

        //columns of organization table
        const val ORGANIZATION_CREATED_BY = "created_by"
        const val ORGANIZATION_FULL_NAME = "full_name"
        const val ORGANIZATION_SHORT_NAME = "short_name"
        const val ORGANIZATION_ADDRESS = "address"
        const val ORGANIZATION_CONTACT = "contact"
        const val ORGANIZATION_WEBSITE = "website"
        const val ORGANIZATION_TIMESTAMP = "timestamp"

        //configure remoteRepository
        const val AUTHORITY = "ps.isel.eduWikiMobileApplication"

        //paths for each table
        const val COURSE_PATH = "course"
        const val CLASS_PATH = "class"
        const val PROGRAMME_PATH = "programme"
        const val LECTURE_PATH = "lecture"
        const val EXAM_PATH = "exam"
        const val HOMEWORK_PATH = "homework"
        const val WORK_ASSIGNMENT_PATH = "workassignment"
        const val ORGANIZATION_PATH = "organization"

        //URIs to access db tables
        val COURSE_URI = Uri.parse("content://$AUTHORITY/$COURSE_PATH")
        val CLASS_URI = Uri.parse("content://$AUTHORITY/$CLASS_PATH")
        val PROGRAMME_URI = Uri.parse("content://$AUTHORITY/$PROGRAMME_PATH")
        val LECTURE_URI = Uri.parse("content://$AUTHORITY/$LECTURE_PATH")
        val EXAM_URI = Uri.parse("content://$AUTHORITY/$EXAM_PATH")
        val HOMEWORK_URI = Uri.parse("content://$AUTHORITY/$HOMEWORK_PATH")
        val WORK_ASSIGNMENT_URI = Uri.parse("content://$AUTHORITY/$WORK_ASSIGNMENT_PATH")
        val ORGANIZATION_URI = Uri.parse("content://$AUTHORITY/$ORGANIZATION_PATH")

        //auxiliary code to return when each uri is matched
        const val COURSE_LIST_CODE = 1
        const val COURSE_ITEM_CODE = 2
        const val CLASS_LIST_CODE = 3
        const val CLASS_ITEM_CODE = 4
        const val PROGRAMME_LIST_CODE = 5
        const val PROGRAMME_ITEM_CODE = 6
        const val LECTURE_LIST_CODE = 7
        const val LECTURE_ITEM_CODE = 8
        const val EXAM_LIST_CODE = 9
        const val EXAM_ITEM_CODE = 10
        const val HOMEWORK_LIST_CODE = 11
        const val HOMEWORK_ITEM_CODE = 12
        const val WORK_ASSIGNMENT_LIST_CODE = 13
        const val WORK_ASSIGNMENT_ITEM_CODE = 14
        const val ORGANIZATION_LIST_CODE = 15
        const val ORGANIZATION_ITEM_CODE = 16

        //auxiliary types for getType method
        val COURSE_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/courses"
        val COURSE_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/course"
        val CLASS_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/classes"
        val CLASS_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/class"
        val PROGRAMME_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/programmes"
        val PROGRAMME_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/programme"
        val LECTURE_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/lectures"
        val LECTURE_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/lecture"
        val EXAM_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/exams"
        val EXAM_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/exam"
        val HOMEWORK_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/homeworks"
        val HOMEWORK_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/homework"
        val WORK_ASSIGNMENT_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/workassignments"
        val WORK_ASSIGNMENT_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/workassignment"
        val ORGANIZATION_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/organizations"
        val ORGANIZATION_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/organization"

    }

    @Volatile private lateinit var dbHelper: DbHelper
    @Volatile private lateinit var uriMatcher: UriMatcher

    override fun onCreate(): Boolean {
        dbHelper = DbHelper(this@ContentProvider.context)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        with(uriMatcher) {
            addURI(AUTHORITY, COURSE_PATH, COURSE_LIST_CODE)
            addURI(AUTHORITY, "$COURSE_PATH/#", COURSE_ITEM_CODE)
            addURI(AUTHORITY, CLASS_PATH, CLASS_LIST_CODE)
            addURI(AUTHORITY, "$CLASS_PATH/#", CLASS_ITEM_CODE)
            addURI(AUTHORITY, PROGRAMME_PATH, PROGRAMME_LIST_CODE)
            addURI(AUTHORITY, "$PROGRAMME_PATH/#", PROGRAMME_ITEM_CODE)
            addURI(AUTHORITY, LECTURE_PATH, LECTURE_LIST_CODE)
            addURI(AUTHORITY, "$LECTURE_PATH/#", LECTURE_ITEM_CODE)
            addURI(AUTHORITY, EXAM_PATH, EXAM_LIST_CODE)
            addURI(AUTHORITY, "$EXAM_PATH/#", EXAM_ITEM_CODE)
            addURI(AUTHORITY, WORK_ASSIGNMENT_PATH, WORK_ASSIGNMENT_LIST_CODE)
            addURI(AUTHORITY, "$WORK_ASSIGNMENT_PATH/#", WORK_ASSIGNMENT_ITEM_CODE)
            addURI(AUTHORITY, ORGANIZATION_PATH, ORGANIZATION_LIST_CODE)
            addURI(AUTHORITY, "$ORGANIZATION_PATH/#", ORGANIZATION_ITEM_CODE)
            addURI(AUTHORITY, HOMEWORK_PATH, HOMEWORK_LIST_CODE)
            addURI(AUTHORITY, "$HOMEWORK_PATH/#", HOMEWORK_ITEM_CODE)
        }
        return true
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
       /* val tableInfo = getTable(uri)
        val db = dbHelper.writableDatabase
        return db.use {
            val id = db.insert(tableInfo.first, null, values)
           if (id < 0) {
                null
            } else {
                context.contentResolver.notifyChange(uri, null)
                Uri.parse("content://$AUTHORITY/${tableInfo.second}/$id")
            }
        }*/
        return Uri.parse("bla")
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*private fun getTableEntry(uri: Uri, selection: String?, selectionArgs: Array<String>?): Triple<String, String?, Array<String>?> {
       /* val itemSelection = "$MOVIE_ID = ${uri.pathSegments.last()}"
        return when (uriMatcher.match(uri)) {
            UPCOMING_ITEM_CODE -> Triple(UPCOMING, itemSelection, null)
            NOW_PLAYING_ITEM_CODE -> Triple(NOW_PLAYING, itemSelection, null)
            FOLLOWING_ITEM_CODE -> Triple(FOLLOWING, itemSelection, null)
            else -> getTable(uri).let { Triple(it.first, selection, selectionArgs) }
        }*/
    }*/

    private fun getTable(uri: Uri): Pair<String, String> = when (uriMatcher.match(uri)) {
        COURSE_LIST_CODE -> Pair(COURSE, COURSE_PATH)
        CLASS_LIST_CODE -> Pair(CLASS, CLASS_PATH)
        PROGRAMME_LIST_CODE -> Pair(PROGRAMME, PROGRAMME_PATH)
        LECTURE_LIST_CODE -> Pair(LECTURE, LECTURE_PATH)
        HOMEWORK_LIST_CODE -> Pair(HOMEWORK, HOMEWORK_PATH)
        WORK_ASSIGNMENT_LIST_CODE -> Pair(WORK_ASSIGNMENT, WORK_ASSIGNMENT_PATH)
        EXAM_LIST_CODE -> Pair(EXAM, EXAM_PATH)
        ORGANIZATION_LIST_CODE -> Pair(ORGANIZATION, ORGANIZATION_PATH)
        else -> null
    } ?: throw IllegalArgumentException("Uri $uri not supported")


}