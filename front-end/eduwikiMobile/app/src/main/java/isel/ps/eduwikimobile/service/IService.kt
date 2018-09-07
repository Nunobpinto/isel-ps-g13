package isel.ps.eduwikimobile.service

import isel.ps.eduwikimobile.domain.single.User
import isel.ps.eduwikimobile.domain.paramsContainer.*

interface IService {
    fun getAllProgrammes(params : ProgrammeCollectionParametersContainer)
    fun getAllCourses(params : CourseCollectionParametersContainer)
    fun getCoursesOfSpecificProgramme(params: CourseProgrammeCollectionParametersContainer)
    fun getAllClasses(params: ClassCollectionParametersContainer)
    fun getOrganization(params: OrganizationParametersContainer)
    fun getWorkAssignmentsOfSpecificCourse(params: WorkAssignmentCollectionParametersContainer)
    fun getExamsOfSpecificCourse(params: ExamCollectionParametersContainer)
    fun getClassesOfSpecificCourse(params: ClassCollectionParametersContainer)
    fun getTermsOfCourse(params: TermCollectionParametersContainer)
    fun getAllCoursesOfSpecificClass(params: CoursesOfSpecificClassParametersContainer)
    fun getAllLecturesOfCourseClass(params: LectureCollectionParametersContainer)
    fun getAllHomeworksOfCourseClass(params: HomeworkCollectionParametersContainer)
    fun getResourceFile(params: ResourceParametersContainer)
    fun getFeedActions(params: ActionsFeedParametersContainer)
    fun getUserFollowingClasses(params: CourseClassCollectionParametersContainer)
    fun getUserFollowingCourses(params: CourseCollectionParametersContainer)
    fun getUserFollowingProgramme(params: UserProgrammeParametersContainer)
    fun getAuthUser(params: LoginParametersContainer)
    fun getUserProfileInfo(params: EntityParametersContainer<User>)
}