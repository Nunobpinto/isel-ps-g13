package isel.ps.eduwikimobile.controller

import com.android.volley.VolleyError
import isel.ps.eduwikimobile.paramsContainer.ProgrammeCollectionParametersContainer
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.paramsContainer.CourseCollectionParametersContainer
import isel.ps.eduwikimobile.paramsContainer.IParametersContainer

class AppController {

    companion object {
        const val ALL_PROGRAMMES = "ALL_PROGRAMMES"
        const val ALL_COURSES = "ALL_COURSES"
        const val ALL_COURSES_OF_SPECIFIC_PROGRAMME = "ALL_COURSES_OF_SPECIFIC_PROGRAMME"

        fun actionHandler(action: String, params: IParametersContainer) {
            return when (action) {
                ALL_PROGRAMMES -> getAllProgrammes(params as ProgrammeCollectionParametersContainer)
                ALL_COURSES -> getAllCourses(params as CourseCollectionParametersContainer)
                ALL_COURSES_OF_SPECIFIC_PROGRAMME -> getCoursesOfSpecificProgramme(params as CourseCollectionParametersContainer)
                else -> throw UnsupportedOperationException("Action not supported!")
            }
        }

        private fun getAllProgrammes(params: ProgrammeCollectionParametersContainer) =
                params.app.service.getAllProgrammes(params)

        private fun getAllCourses(params: CourseCollectionParametersContainer) =
                params.app.service.getAllCourses(params)

        private fun getCoursesOfSpecificProgramme(params: CourseCollectionParametersContainer) =
                params.app.service.getCoursesOfSpecificProgramme(params)

    }

}