package isel.ps.eduwikimobile.service

import isel.ps.eduwikimobile.API_URL
import isel.ps.eduwikimobile.domain.model.collection.*
import isel.ps.eduwikimobile.domain.model.single.Organization
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.paramsContainer.*
import isel.ps.eduwikimobile.repository.IEduWikiRepository

class Service(
        private val repository: IEduWikiRepository
) : IService {

    companion object {
        val ALL_PROGRAMMES_URL = API_URL + "/programmes"
        val ALL_COURSES_URL = API_URL + "/courses"
        val ALL_CLASSES_URL = API_URL + "/classes"
        val ORGANIZATION_URL = API_URL + "/organization"
        val RESOURCES = API_URL + "/resources"
        val USER_FOLLOWING_CLASSES = API_URL + "/user/classes"
        val USER_FOLLOWING_COURSES = API_URL + "/user/courses"
        val USER_FOLLOWING_PROGRAMME = API_URL + "/user/programme"
        val FEED = API_URL + "/user/feed"
    }

    override fun getAllProgrammes(params: ProgrammeCollectionParametersContainer) =
            repository.getEntity(
                    ALL_PROGRAMMES_URL,
                    ProgrammeCollection::class.java,
                    params
            )

    override fun getAllCourses(params: CourseCollectionParametersContainer) =
            repository.getEntity(
                    ALL_COURSES_URL,
                    CourseCollection::class.java,
                    params
            )

    override fun getCoursesOfSpecificProgramme(params: CourseProgrammeCollectionParametersContainer) =
            repository.getEntity(
                    "$ALL_PROGRAMMES_URL/${params.programmeId}/courses",
                    CourseProgrammeCollection::class.java,
                    params
            )

    override fun getAllClasses(params: ClassCollectionParametersContainer) =
            repository.getEntity(
                    ALL_CLASSES_URL,
                    ClassCollection::class.java,
                    params
            )

    override fun getOrganization(params: OrganizationParametersContainer) =
            repository.getEntity(
                    ORGANIZATION_URL,
                    Organization::class.java,
                    params
            )

    override fun getWorkAssignmentsOfSpecificCourse(params: WorkAssignmentCollectionParametersContainer) =
            repository.getEntity(
                    "$ALL_COURSES_URL/${params.courseId}/terms/${params.termId}/work-assignments",
                    WorkAssignmentCollection::class.java,
                    params
            )

    override fun getExamsOfSpecificCourse(params: ExamCollectionParametersContainer) =
            repository.getEntity(
                    "$ALL_COURSES_URL/${params.courseId}/terms/${params.termId}/exams",
                    ExamCollection::class.java,
                    params
            )

    override fun getClassesOfSpecificCourse(params: ClassCollectionParametersContainer) =
            repository.getEntity(
                    "$ALL_COURSES_URL/${params.courseId}/terms/${params.termId}/classes",
                    ClassCollection::class.java,
                    params
            )

    override fun getTermsOfCourse(params: TermCollectionParametersContainer) =
            repository.getEntity(
                    "$ALL_COURSES_URL/${params.courseId}/terms",
                    TermCollection::class.java,
                    params
            )


    override fun getAllCoursesOfSpecificClass(params: CoursesOfSpecificClassParametersContainer) {
        repository.getEntity(
                "$ALL_CLASSES_URL/${params.classId}/courses",
                CourseClassCollection::class.java,
                params
        )
    }

    override fun getAllLecturesOfCourseClass(params: LectureCollectionParametersContainer) =
            repository.getEntity(
                    "$ALL_CLASSES_URL/${params.classId}/courses/${params.courseId}/lectures",
                    LectureCollection::class.java,
                    params
            )

    override fun getAllHomeworksOfCourseClass(params: HomeworkCollectionParametersContainer) =
            repository.getEntity(
                    "$ALL_CLASSES_URL/${params.classId}/courses/${params.courseId}/homeworks",
                    HomeworkCollection::class.java,
                    params
            )

    override fun getResourceFile(params: ResourceParametersContainer) =
            repository.getResourceFile(
                    "$RESOURCES/${params.resourceId}",
                    params
            )

    override fun getFeedActions(params: ActionsFeedParametersContainer) =
            repository.getEntity(
                    FEED,
                    UserActionCollection::class.java,
                    params
            )


    override fun getUserFollowingClasses(params: CourseClassCollectionParametersContainer) =
            repository.getEntity(
                    USER_FOLLOWING_CLASSES,
                    CourseClassCollection::class.java,
                    params
            )

    override fun getUserFollowingCourses(params: CourseCollectionParametersContainer) =
        repository.getEntity(
                USER_FOLLOWING_COURSES,
                CourseCollection::class.java,
                params
        )

    override fun getUserFollowingProgramme(params: EntityParametersContainer<Programme>) =
            repository.getEntity(
                    USER_FOLLOWING_PROGRAMME,
                    Programme::class.java,
                    params
            )
}