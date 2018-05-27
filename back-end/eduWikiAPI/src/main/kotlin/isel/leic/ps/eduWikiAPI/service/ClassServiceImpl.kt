package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var classDAO: ClassDAO

    override fun getAllClasses(): List<Class> = classDAO.getAllClasses()

    override fun getSpecificClass(classId: Int): Class = classDAO.getClass(classId)

    override fun getReportsOfClass(classId: Int): List<ClassReport> = classDAO.getAllReportsOfClass(classId)

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReport = classDAO.getReportOfClass(classId, reportId)

    override fun getAllStagedClasses(): List<ClassStage> = classDAO.getAllClassStages()

    override fun getSpecificStagedClass(stageId: Int): ClassStage = classDAO.getClassStage(stageId)
}