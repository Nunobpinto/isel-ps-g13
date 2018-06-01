package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import java.util.*

interface ClassService {
    /**
     * Class
     */
    fun getAllClasses(): List<Class>

    fun getSpecificClass(classId: Int): Class

    fun createClass(input: ClassInputModel): Optional<Class>

    fun voteOnClass(classId: Int, vote: VoteInputModel): Int

    fun updateClass(classId: Int, input: ClassInputModel): Int

    fun deleteSpecificClass(classId: Int): Int

    /**
     * Course Report
     */
    fun getReportsOfClass(classId: Int): List<ClassReport>

    fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReport

    fun reportClass(classId: Int, report: Any): Int

    fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateClassFromReport(classId: Int, reportId: Int): Int

    fun deleteAllReportsInClass(classId: Int): Int

    fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int

    /**
     * Course Stage
     */

}