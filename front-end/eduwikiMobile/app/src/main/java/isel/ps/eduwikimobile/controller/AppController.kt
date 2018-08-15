package isel.ps.eduwikimobile.controller

import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.paramsContainer.*

class AppController {

    companion object {
        const val ALL_PROGRAMMES = "ALL_PROGRAMMES"
        const val ALL_COURSES = "ALL_COURSES"
        const val ALL_COURSES_OF_SPECIFIC_PROGRAMME = "ALL_COURSES_OF_SPECIFIC_PROGRAMME"
        const val ALL_CLASSES = "ALL_CLASSES"
        const val ORGANIZATION = "ORGANIZATION"

        fun actionHandler(action: String, params: IParametersContainer) {
            return when (action) {
                ALL_PROGRAMMES -> getAllProgrammes(params as ProgrammeCollectionParametersContainer)
                ALL_COURSES -> getAllCourses(params as CourseCollectionParametersContainer)
                ALL_COURSES_OF_SPECIFIC_PROGRAMME -> getCoursesOfSpecificProgramme(params as CourseProgrammeCollectionParametersContainer)
                ALL_CLASSES -> getAllClasses(params as ClassCollectionParametersContainer)
                ORGANIZATION -> getOrganization(params as OrganizationParametersContainer)
                else -> throw UnsupportedOperationException("Action not supported!")
            }
        }

        private fun getAllProgrammes(params: ProgrammeCollectionParametersContainer) =
                params.app.service.getAllProgrammes(params)

        private fun getAllCourses(params: CourseCollectionParametersContainer) =
                params.app.service.getAllCourses(params)

        private fun getCoursesOfSpecificProgramme(params: CourseProgrammeCollectionParametersContainer) =
                params.app.service.getCoursesOfSpecificProgramme(params)

        private fun getAllClasses(params: ClassCollectionParametersContainer) =
                params.app.service.getAllClasses(params)

        private fun getOrganization(params : OrganizationParametersContainer) =
                params.app.service.getOrganization(params)

    }

}