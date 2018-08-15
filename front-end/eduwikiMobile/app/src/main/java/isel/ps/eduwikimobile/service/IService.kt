package isel.ps.eduwikimobile.service

import isel.ps.eduwikimobile.paramsContainer.*

interface IService {
    fun getAllProgrammes(params : ProgrammeCollectionParametersContainer)
    fun getAllCourses(params : CourseCollectionParametersContainer)
    fun getCoursesOfSpecificProgramme(params: CourseProgrammeCollectionParametersContainer)
    fun getAllClasses(params: ClassCollectionParametersContainer)
    fun getOrganization(params: OrganizationParametersContainer)
}