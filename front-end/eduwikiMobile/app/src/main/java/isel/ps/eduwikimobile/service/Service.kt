package isel.ps.eduwikimobile.service

import com.android.volley.VolleyError
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.paramsContainer.*
import isel.ps.eduwikimobile.repository.EduWikiRepository
import isel.ps.eduwikimobile.repository.IEduWikiRepository

class Service : IService {

    private var remoteRepository: IEduWikiRepository

    init {
        remoteRepository = EduWikiRepository()
    }

    override fun getAllProgrammes(params: ProgrammeCollectionParametersContainer) =
            remoteRepository.getAllProgrammes(
                    params.app,
                    { programmes -> params.successCb(programmes) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getAllCourses(params: CourseCollectionParametersContainer) =
            remoteRepository.getAllCourses(
                    params.app,
                    { courses -> params.successCb(courses) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getCoursesOfSpecificProgramme(params: CourseProgrammeCollectionParametersContainer) =
            remoteRepository.getCoursesOfSpeficiProgramme(
                    params.programmeId,
                    params.app,
                    { courses -> params.successCb(courses) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getAllClasses(params: ClassCollectionParametersContainer) =
            remoteRepository.getAllClasses(
                    params.app,
                    { classes -> params.successCb(classes) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getOrganization(params: OrganizationParametersContainer) =
            remoteRepository.getOrganization(
                    params.app,
                    { organization -> params.successCb(organization) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getWorkAssignmentsOfSpecificCourse(params: WorkAssignmentCollectionParametersContainer) =
            remoteRepository.getWorkAssignmentsOfSpecificCourse(
                    params.termId,
                    params.courseId,
                    params.app,
                    { works -> params.successCb(works) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getExamsOfSpecificCourse(params: ExamCollectionParametersContainer) =
            remoteRepository.getExamsOfSpecificCourse(
                    params.termId,
                    params.courseId,
                    params.app,
                    { exams -> params.successCb(exams) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getClassesOfSpecificCourse(params: CourseClassCollectionParametersContainer) =
            remoteRepository.getClassesOfSpecificCourse(
                    params.termId,
                    params.courseId,
                    params.app,
                    { classes -> params.successCb(classes) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

    override fun getTermsOfCourse(params: TermCollectionParametersContainer) {
        remoteRepository.getTermsOfCourse(
                params.courseId,
                params.app,
                { terms -> params.successCb(terms)},
                { error: VolleyError -> params.errorCb(AppException(error.message!!))}
        )
    }

    override fun getAllCoursesOfSpecificClass(params: CoursesOfSpecificClassParametersContainer) {
        remoteRepository.getAllCoursesOfSpecificClass(
                params.classId,
                params.app,
                { courses -> params.successCb(courses)},
                { error: VolleyError -> params.errorCb(AppException(error.message!!))}
        )
    }

    override fun getAllLecturesOfCourseClass(params: LectureCollectionParametersContainer) {
        remoteRepository.getAllLecturesOfCourseClass(
                params.courseId,
                params.classId,
                params.app,
                { lectures -> params.successCb(lectures)},
                { error: VolleyError -> params.errorCb(AppException(error.message!!))}
        )
    }

    override fun getAllHomeworksOfCourseClass(params: HomeworkCollectionParametersContainer) {
        remoteRepository.getAllHomeworksOfCourseClass(
                params.courseId,
                params.classId,
                params.app,
                { homeworks -> params.successCb(homeworks)},
                { error: VolleyError -> params.errorCb(AppException(error.message!!))}
        )
    }


}