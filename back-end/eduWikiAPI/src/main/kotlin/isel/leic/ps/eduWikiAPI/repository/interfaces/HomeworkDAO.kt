package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import java.util.*

interface HomeworkDAO {

    /**
     * Main entities queries
     */

    fun getAllHomeworksFromCourseInClass(courseClassId: Int): List<Homework>

    fun getSpecificHomeworkFromSpecificCourseInClass(courseClassId: Int, homeworkId: Int): Optional<Homework>

    fun createHomeworkOnCourseInClass(courseClassId: Int, homework: Homework): Homework

    fun updateHomework(homework: Homework): Homework

    fun updateVotesOnHomework(homeworkId: Int, votes: Int): Int

    fun deleteSpecificHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int

    /**
     * Report entities queries
     */

    fun getAllReportsOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int): List<HomeworkReport>

    fun getSpecificReportOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport>

    fun createReportOnHomework(homeworkReport: HomeworkReport): HomeworkReport

    fun updateVotesOnReportedHomework(homeworkId: Int, reportId: Int, votes: Int): Int

    fun deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Int

    /**
     * Stage entities queries
     */

    fun getAllStagedHomeworksOfCourseInClass(courseClassId: Int): List<HomeworkStage>

    fun getSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Optional<HomeworkStage>

    fun createStagingHomeworkOnCourseInClass(courseClassId: Int, homeworkStage: HomeworkStage): HomeworkStage

    fun updateVotesOnStagedHomework(stageId: Int, votes: Int): Int

    fun deleteSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfHomeworkOfCourseInclass(courseClassId: Int, homeworkId: Int): List<HomeworkVersion>

    fun getSpecificVersionOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, version: Int): Optional<HomeworkVersion>

    fun createHomeworkVersion(homeworkVersion: HomeworkVersion): HomeworkVersion

    fun getHomeworkByLogId(logId: Int): Optional<Homework>

    fun getHomeworkReportByLogId(logId: Int): Optional<HomeworkReport>

    fun getHomeworkStageByLogId(logId: Int): Optional<HomeworkStage>
}