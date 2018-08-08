package isel.ps.eduwikimobile.controller

import com.android.volley.VolleyError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.controller.parametersContainer.ProgrammeParametersContainer
import isel.ps.eduwikimobile.exceptions.AppException

class AppController {

    companion object {
        const val ALL_PROGRAMMES = "ALL_PROGRAMMES"
        val app = EduWikiApplication()
    }

    fun actionHandler(action: String, params: ProgrammeParametersContainer) {
        return when (action) {
            ALL_PROGRAMMES -> getAllProgrammes(params)
            else -> throw UnsupportedOperationException("Action not supported!")
        }
    }

    private fun getAllProgrammes(params: ProgrammeParametersContainer) {
        app.remoteRepository.getAllProgrammes(
                app,
                { movies, _ -> params.successCb(Pair(movies, null)) },
                { error: VolleyError -> params.errorCb(AppException("An internet connection is required to access this functionality")) }
        )
    }


}