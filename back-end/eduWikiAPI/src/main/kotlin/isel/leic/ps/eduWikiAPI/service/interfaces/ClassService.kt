package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage

interface ClassService {

    fun getAllClasses(): List<Class>

    fun getSpecificClass(classId: Int): Class

    fun getReportsOfClass(classId: Int): List<ClassReport>

    fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReport

    fun getAllStagedClasses(): List<ClassStage>

    fun getSpecificStagedClass(stageId: Int): ClassStage

}