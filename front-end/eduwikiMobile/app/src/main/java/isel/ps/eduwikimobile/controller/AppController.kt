package isel.ps.eduwikimobile.controller

import isel.ps.eduwikimobile.domain.single.User
import isel.ps.eduwikimobile.domain.paramsContainer.*
import isel.ps.eduwikimobile.service.IService

class AppController(
        private val service: IService
) {

    companion object {
        const val ALL_PROGRAMMES = "ALL_PROGRAMMES"
        const val ALL_COURSES = "ALL_COURSES"
        const val ALL_COURSES_OF_SPECIFIC_PROGRAMME = "ALL_COURSES_OF_SPECIFIC_PROGRAMME"
        const val ALL_CLASSES = "ALL_CLASSES"
        const val ORGANIZATION = "ORGANIZATION_URL"
        const val WORK_ASSIGNMENTS = "WORK_ASSIGNMENTS"
        const val EXAMS = "EXAMS"
        const val ALL_CLASSES_OF_SPECIFIC_COURSE = "ALL_CLASSES_OF_SPECIFIC_COURSE"
        const val TERMS_OF_COURSE = "TERMS_OF_COURSE"
        const val SPECIFIC_RESOURCE = "SPECIFIC_RESOURCE"
        const val ALL_COURSES_OF_SPECIFIC_CLASS = "ALL_COURSES_OF_SPECIFIC_CLASS"
        const val ALL_LECTURES_OF_COURSE_CLASS = "ALL_LECTURES_OF_COURSE_CLASS"
        const val ALL_HOMEWORKS_OF_COURSE_CLASS = "ALL_HOMEWORKS_OF_COURSE_CLASS"
        const val FEED_ACTIONS = "FEED_ACTIONS"
        const val USER_FOLLOWING_CLASSES = "USER_FOLLOWING_CLASSES"
        const val USER_FOLLOWING_COURSES = "USER_FOLLOWING_COURSES"
        const val USER_FOLLOWING_PROGRAMME = "USER_FOLLOWING_PROGRAMME"
        const val AUTH_USER = "AUTH_USER"
        const val USER_PROFILE_INFO = "USER_PROFILE_INFO"
    }

    fun <T> actionHandler(action: String, params: ParametersContainer<T>) {
        return when (action) {
            ALL_PROGRAMMES -> getAllProgrammes(params as ProgrammeCollectionParametersContainer)
            ALL_COURSES -> getAllCourses(params as CourseCollectionParametersContainer)
            ALL_COURSES_OF_SPECIFIC_PROGRAMME -> getCoursesOfSpecificProgramme(params as CourseProgrammeCollectionParametersContainer)
            ALL_CLASSES -> getAllClasses(params as ClassCollectionParametersContainer)
            ORGANIZATION -> getOrganization(params as OrganizationParametersContainer)
            WORK_ASSIGNMENTS -> getWorkAssignmentsOfSpecificCourse(params as WorkAssignmentCollectionParametersContainer)
            EXAMS -> getExamsOfSpecificCourse(params as ExamCollectionParametersContainer)
            ALL_CLASSES_OF_SPECIFIC_COURSE -> getClassesOfSpecificCourse(params as ClassCollectionParametersContainer)
            TERMS_OF_COURSE -> getTermsOfCourse(params as TermCollectionParametersContainer)
            ALL_COURSES_OF_SPECIFIC_CLASS -> getAllCoursesOfSpecificClass(params as CoursesOfSpecificClassParametersContainer)
            ALL_LECTURES_OF_COURSE_CLASS -> getAllLecturesOfCourseClass(params as LectureCollectionParametersContainer)
            ALL_HOMEWORKS_OF_COURSE_CLASS -> getAllHomeworksOfCourseClass(params as HomeworkCollectionParametersContainer)
            FEED_ACTIONS -> getFeedActions(params as ActionsFeedParametersContainer)
            USER_FOLLOWING_CLASSES -> getUserFollowingClasses(params as CourseClassCollectionParametersContainer)
            USER_FOLLOWING_COURSES -> getUserFollowingCourses(params as CourseCollectionParametersContainer)
            USER_FOLLOWING_PROGRAMME -> getUserFollowingProgramme(params as UserProgrammeParametersContainer)
            SPECIFIC_RESOURCE -> getSpecificResource(params as ResourceParametersContainer)
            AUTH_USER -> getAuthenticatedUser(params as LoginParametersContainer)
            USER_PROFILE_INFO -> getUserProfileInfo(params as EntityParametersContainer<User>)
            else -> throw UnsupportedOperationException("Action not supported!")
        }
    }

    private fun getAuthenticatedUser(params: LoginParametersContainer) =
        service.getAuthUser(params)

    private fun getUserProfileInfo(params: EntityParametersContainer<User>) =
            service.getUserProfileInfo(params)

    private fun getAllProgrammes(params: ProgrammeCollectionParametersContainer) =
            service.getAllProgrammes(params)

    private fun getAllCourses(params: CourseCollectionParametersContainer) =
            service.getAllCourses(params)

    private fun getCoursesOfSpecificProgramme(params: CourseProgrammeCollectionParametersContainer) =
            service.getCoursesOfSpecificProgramme(params)

    private fun getAllClasses(params: ClassCollectionParametersContainer) =
            service.getAllClasses(params)

    private fun getOrganization(params: OrganizationParametersContainer) =
            service.getOrganization(params)

    private fun getWorkAssignmentsOfSpecificCourse(params: WorkAssignmentCollectionParametersContainer) =
            service.getWorkAssignmentsOfSpecificCourse(params)

    private fun getExamsOfSpecificCourse(params: ExamCollectionParametersContainer) =
            service.getExamsOfSpecificCourse(params)

    private fun getClassesOfSpecificCourse(params: ClassCollectionParametersContainer) =
            service.getClassesOfSpecificCourse(params)

    private fun getTermsOfCourse(params: TermCollectionParametersContainer) =
            service.getTermsOfCourse(params)

    private fun getAllCoursesOfSpecificClass(params: CoursesOfSpecificClassParametersContainer) =
            service.getAllCoursesOfSpecificClass(params)

    private fun getAllLecturesOfCourseClass(params: LectureCollectionParametersContainer) =
            service.getAllLecturesOfCourseClass(params)

    private fun getAllHomeworksOfCourseClass(params: HomeworkCollectionParametersContainer) =
            service.getAllHomeworksOfCourseClass(params)

    private fun getSpecificResource(params: ResourceParametersContainer) =
            service.getResourceFile(params)

    private fun getFeedActions(params: ActionsFeedParametersContainer) =
            service.getFeedActions(params)

    private fun getUserFollowingClasses(params: CourseClassCollectionParametersContainer) =
            service.getUserFollowingClasses(params)

    private fun getUserFollowingCourses(params: CourseCollectionParametersContainer) =
            service.getUserFollowingCourses(params)

    private fun getUserFollowingProgramme(params: UserProgrammeParametersContainer) =
            service.getUserFollowingProgramme(params)

}