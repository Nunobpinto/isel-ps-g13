package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.ClassMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import java.util.*

interface LectureDAO {

    /**
     * Main queries
     */

    fun getAllLecturesFromCourseInClass(courseClassId: Int): List<Lecture>

    fun getSpecificLectureFromCourseInClass(courseClassId: Int, lectureId: Int): Optional<Lecture>

    fun createLectureOnCourseInClass(courseClassId: Int, lecture: Lecture): Lecture

    fun updateLecture(lecture: Lecture): Lecture

    fun updateVotesOnLecture(lectureId: Int, votes: Int): Int

    fun deleteSpecificLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int

    /**
     * Stage queries
     */

    fun getAllStagedLecturesOfCourseInClass(courseClassId: Int): List<LectureStage>

    fun getSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Optional<LectureStage>

    fun createStagingLectureOnCourseInClass(courseClassId: Int, lectureStage: LectureStage): LectureStage

    fun updateVotesOnStagedLecture(stageId: Int, votes: Int): Int

    fun deleteSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Int

    /**
     * Report queries
     */

    fun getAllReportsOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int): List<LectureReport>

    fun getSpecificReportOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Optional<LectureReport>

    fun createReportOnLecture(lectureReport: LectureReport): LectureReport

    fun updateVotesOnReportedLecture(lectureId: Int, reportId: Int, votes: Int): Int

    fun deleteSpecificReportOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Int

    /**
     * Version queries
     */

    fun getAllVersionsOfLectureOfCourseInclass(courseClassId: Int, lectureId: Int): List<LectureVersion>

    fun getSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Optional<LectureVersion>

    fun createLectureVersion(lectureVersion: LectureVersion): LectureVersion

    fun getLectureByLogId(logId: Int): Optional<Lecture>

    fun getLectureReportByLogId(logId: Int): Optional<LectureReport>

    fun getLectureStageByLogId(logId: Int): Optional<LectureStage>

}