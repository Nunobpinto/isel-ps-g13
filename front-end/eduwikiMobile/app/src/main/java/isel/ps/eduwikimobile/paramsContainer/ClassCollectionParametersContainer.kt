package isel.ps.eduwikimobile.paramsContainer

import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.domain.model.collection.ClassCollection
import isel.ps.eduwikimobile.exceptions.AppException

class ClassCollectionParametersContainer (
        app: EduWikiApplication,
        successCb: (ClassCollection) -> Unit,
        errorCb: (AppException) -> Unit
) : ParametersContainer<ClassCollection> (app, successCb, errorCb)
