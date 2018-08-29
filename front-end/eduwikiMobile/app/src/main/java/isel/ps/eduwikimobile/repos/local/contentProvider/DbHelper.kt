package isel.ps.eduwikimobile.repos.local.contentProvider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper (
        ctx: Context?,
        version: Int = 1,
        dbName: String = "EDU_WIKI_DB"
) : SQLiteOpenHelper(
        ctx,
        dbName,
        null,
        version
) {

    companion object {

        const val DROP_COURSE =
                "drop table if exists ${ContentProvider.COURSE}"

        const val DROP_CLASS =
                "drop table if exists ${ContentProvider.CLASS}"

        const val DROP_PROGRAMME =
                "drop table if exists ${ContentProvider.PROGRAMME}"

        const val DROP_LECTURE =
                "drop table if exists ${ContentProvider.LECTURE}"

        const val DROP_WORK_ASSIGNMENT =
                "drop table if exists ${ContentProvider.WORK_ASSIGNMENT}"

        const val DROP_EXAM =
                "drop table if exists ${ContentProvider.EXAM}"

        const val DROP_HOMEWORK =
                "drop table if exists ${ContentProvider.HOMEWORK}"

        const val DROP_ORGANIZATION =
                "drop table if exists ${ContentProvider.ORGANIZATION}"

        /*const val CREATE_CLASS =
                "create table ${ContentProvider.CLASS} ( " +
                        "${ContentProvider.MOVIE_ID} integer primary key, " +
                        "${ContentProvider.TITLE} text not null , " +
                        "${ContentProvider.RELEASE_DATE} real , " +
                        "${ContentProvider.POSTER} text , " +
                        "${ContentProvider.VOTE_AVERAGE} real , " +
                        "${ContentProvider.RUNTIME} integer , " +
                        "${ContentProvider.POPULARITY} real , " +
                        "${ContentProvider.OVERVIEW} text , " +
                        "${ContentProvider.GENRES} text )"
        const val CREATE_COURSE =
                "create table ${ContentProvider.COURSE} ( " +
                        "${ContentProvider.MOVIE_ID} integer primary key, " +
                        "${ContentProvider.TITLE} text not null , " +
                        "${ContentProvider.RELEASE_DATE} real , " +
                        "${ContentProvider.POSTER} text , " +
                        "${ContentProvider.VOTE_AVERAGE} real , " +
                        "${ContentProvider.RUNTIME} integer , " +
                        "${ContentProvider.POPULARITY} real , " +
                        "${ContentProvider.OVERVIEW} text , " +
                        "${ContentProvider.GENRES} text)"
        const val CREATE_PROGRAMME =
                "create table ${ContentProvider.PROGRAMME} ( " +
                        "${ContentProvider.MOVIE_ID} integer primary key, " +
                        "${ContentProvider.POSTER} text ," +
                        "${ContentProvider.TITLE} text not null, " +
                        "${ContentProvider.RELEASE_DATE} text)"*/
    }

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}