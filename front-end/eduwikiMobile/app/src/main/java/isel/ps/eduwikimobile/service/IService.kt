package isel.ps.eduwikimobile.service

import isel.ps.eduwikimobile.paramsContainer.*

interface IService {
    fun getAllProgrammes(params : ProgrammeCollectionParametersContainer)
    fun getAllCourses(params : CourseCollectionParametersContainer)
    fun getCoursesOfSpecificProgramme(params: CourseProgrammeCollectionParametersContainer)
    fun getAllClasses(params: ClassCollectionParametersContainer)
    fun getOrganization(params: OrganizationParametersContainer)
    fun getWorkAssignmentsOfSpecificCourse(params: WorkAssignmentCollectionParametersContainer)
    fun getExamsOfSpecificCourse(params: ExamCollectionParametersContainer)
    fun getClassesOfSpecificCourse(params: CourseClassCollectionParametersContainer)
    fun getTermsOfCourse(params: TermCollectionParametersContainer)
    fun getAllCoursesOfSpecificClass(params: CoursesOfSpecificClassParametersContainer)
    fun getAllLecturesOfCourseClass(params: LectureCollectionParametersContainer)
    fun getAllHomeworksOfCourseClass(params: HomeworkCollectionParametersContainer)
    fun getResourceFile(params: ResourceParametersContainer)
    fun getFeedActions(params: ActionsFeedParametersContainer)
    fun getUserFollowingItems(params: FollowingParametersContainer)
    fun getActionEntity()
}