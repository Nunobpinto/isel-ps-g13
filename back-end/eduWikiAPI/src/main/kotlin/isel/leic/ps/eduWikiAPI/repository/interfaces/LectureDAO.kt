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

    fun createLecture(lecture: Lecture): Optional<Lecture>

    fun voteOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, vote: Vote): Int

    fun deleteAllLecturesOfCourseInClass(courseClassId: Int): Int

    fun deleteSpecificLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int

    fun getAllReportsOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int): List<LectureReport>

    fun getSpecificReportOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Optional<LectureReport>

    fun deleteAllReportsOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int

    fun deleteSpecificReportOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Int

    fun createReportOnLecture(lectureReport: LectureReport): Optional<LectureReport>

    fun voteOnReportOfLecture(lectureId: Int, reportId: Int, vote: Vote): Int

    fun updateLecture(lecture: Lecture): Optional<Lecture>

    fun createLectureVersion(lectureVersion: LectureVersion): Optional<LectureVersion>

    fun getAllStagedLecturesOfCourseInClass(courseClassId: Int): List<LectureStage>

    fun getSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Optional<LectureStage>

    fun createStagingLecture(lectureStage: LectureStage): Optional<LectureStage>

    fun voteOnStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int, vote: Vote): Int

    fun getAllVersionsOfLectureOfCourseInclass(courseClassId: Int, lectureId: Int): List<LectureVersion>

    fun getSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Optional<LectureVersion>

    fun deleteAllVersionsOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int

    fun deleteSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Int
}