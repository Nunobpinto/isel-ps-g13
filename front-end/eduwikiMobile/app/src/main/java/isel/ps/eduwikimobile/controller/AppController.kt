package isel.ps.eduwikimobile.controller

import isel.ps.eduwikimobile.paramsContainer.*

class AppController {

    companion object {
        const val ALL_PROGRAMMES = "ALL_PROGRAMMES"
        const val ALL_COURSES = "ALL_COURSES"
        const val ALL_COURSES_OF_SPECIFIC_PROGRAMME = "ALL_COURSES_OF_SPECIFIC_PROGRAMME"
        const val ALL_CLASSES = "ALL_CLASSES"
        const val ORGANIZATION = "ORGANIZATION"
        const val WORK_ASSIGNMENTS = "WORK_ASSIGNMENTS"
        const val EXAMS = "EXAMS"
        const val ALL_CLASSES_OF_SPECIFIC_COURSE = "ALL_CLASSES_OF_SPECIFIC_COURSE"
        const val TERMS_OF_COURSE = "TERMS_OF_COURSE"
        const val SPECIFIC_RESOURCE = "SPECIFIC_RESOURCE"
        const val ALL_COURSES_OF_SPECIFIC_CLASS = "ALL_COURSES_OF_SPECIFIC_CLASS"
        const val ALL_LECTURES_OF_COURSE_CLASS = "ALL_LECTURES_OF_COURSE_CLASS"
        const val ALL_HOMEWORKS_OF_COURSE_CLASS = "ALL_HOMEWORKS_OF_COURSE_CLASS"

        fun actionHandler(action: String, params: IParametersContainer) {
            return when (action) {
                ALL_PROGRAMMES -> getAllProgrammes(params as ProgrammeCollectionParametersContainer)
                ALL_COURSES -> getAllCourses(params as CourseCollectionParametersContainer)
                ALL_COURSES_OF_SPECIFIC_PROGRAMME -> getCoursesOfSpecificProgramme(params as CourseProgrammeCollectionParametersContainer)
                ALL_CLASSES -> getAllClasses(params as ClassCollectionParametersContainer)
                ORGANIZATION -> getOrganization(params as OrganizationParametersContainer)
                WORK_ASSIGNMENTS -> getWorkAssignmentsOfSpecificCourse(params as WorkAssignmentCollectionParametersContainer)
                EXAMS -> getExamsOfSpecificCourse(params as ExamCollectionParametersContainer)
                ALL_CLASSES_OF_SPECIFIC_COURSE -> getClassesOfSpecificCourse(params as CourseClassCollectionParametersContainer)
                TERMS_OF_COURSE -> getTermsOfCourse(params as TermCollectionParametersContainer)
                SPECIFIC_RESOURCE -> getSpecificResource(params as ResourceParametersContainer)
                ALL_COURSES_OF_SPECIFIC_CLASS -> getAllCoursesOfSpecificClass(params as CoursesOfSpecificClassParametersContainer)
                ALL_LECTURES_OF_COURSE_CLASS -> getAllLecturesOfCourseClass(params as LectureCollectionParametersContainer)
                ALL_HOMEWORKS_OF_COURSE_CLASS -> getAllHomeworksOfCourseClass(params as HomeworkCollectionParametersContainer)
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

        private fun getOrganization(params: OrganizationParametersContainer) =
                params.app.service.getOrganization(params)

        private fun getWorkAssignmentsOfSpecificCourse(params: WorkAssignmentCollectionParametersContainer) =
                params.app.service.getWorkAssignmentsOfSpecificCourse(params)

        private fun getExamsOfSpecificCourse(params: ExamCollectionParametersContainer) =
                params.app.service.getExamsOfSpecificCourse(params)

        private fun getClassesOfSpecificCourse(params: CourseClassCollectionParametersContainer) =
                params.app.service.getClassesOfSpecificCourse(params)

        private fun getTermsOfCourse(params: TermCollectionParametersContainer) =
                params.app.service.getTermsOfCourse(params)

        private fun getAllCoursesOfSpecificClass(params: CoursesOfSpecificClassParametersContainer) =
                params.app.service.getAllCoursesOfSpecificClass(params)

        private fun  getAllLecturesOfCourseClass(params: LectureCollectionParametersContainer) =
                params.app.service.getAllLecturesOfCourseClass(params)

        private fun getAllHomeworksOfCourseClass(params: HomeworkCollectionParametersContainer) =
                params.app.service.getAllHomeworksOfCourseClass(params)

        private fun getSpecificResource(params: ResourceParametersContainer) =
                params.app.service.getSpecificResource(params)

    }

}