package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import java.util.*

interface HomeworkDAO {

    fun getAllHomeworksFromCourseInClass(courseClassId: Int): List<Homework>

    fun getSpecificHomeworkFromSpecificCourseInClass(courseClassId: Int, homeworkId: Int): Optional<Homework>

    fun createHomework(homework: Homework): Optional<Homework>

    fun voteOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, vote: Vote): Int

    fun deleteAllHomeworksOfCourseInClass(courseClassId: Int): Int

    fun deleteSpecificHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int

    fun getAllStagedHomeworksOfCourseInClass(courseClassId: Int): List<HomeworkStage>

    fun getSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Optional<HomeworkStage>

    fun createStagingHomework(homeworkStage: HomeworkStage): Optional<HomeworkStage>

    fun deleteSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Int

    fun createHomeworkVersion(homeworkVersion: HomeworkVersion): Optional<HomeworkVersion>

    fun voteOnStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int, vote: Vote): Int

    fun getAllReportsOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int): List<HomeworkReport>

    fun getSpecificReportOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport>

    fun createReportOnHomework(homeworkReport: HomeworkReport): Optional<HomeworkReport>

    fun updateHomeWork(homework: Homework): Optional<Homework>


}