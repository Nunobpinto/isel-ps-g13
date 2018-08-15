package isel.ps.eduwikimobile.service

import com.android.volley.VolleyError
import isel.ps.eduwikimobile.exceptions.AppException
import isel.ps.eduwikimobile.paramsContainer.CourseCollectionParametersContainer
import isel.ps.eduwikimobile.paramsContainer.ProgrammeCollectionParametersContainer
import isel.ps.eduwikimobile.repository.EduWikiRepository
import isel.ps.eduwikimobile.repository.IEduWikiRepository

class Service() : IService {

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

    override fun getCoursesOfSpecificProgramme(params: CourseCollectionParametersContainer) =
            remoteRepository.getCoursesOfSpeficiProgramme(
                    params.programmeId!!,
                    params.app,
                    { courses -> params.successCb(courses) },
                    { error: VolleyError -> params.errorCb(AppException(error.message!!)) }
            )

}