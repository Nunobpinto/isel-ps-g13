package isel.ps.eduwikimobile.service

import isel.ps.eduwikimobile.paramsContainer.CourseCollectionParametersContainer
import isel.ps.eduwikimobile.paramsContainer.ProgrammeCollectionParametersContainer

interface IService {
    fun getAllProgrammes(params : ProgrammeCollectionParametersContainer)
    fun getAllCourses(params : CourseCollectionParametersContainer)
    fun getCoursesOfSpecificProgramme(params: CourseCollectionParametersContainer)

}