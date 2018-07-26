package isel.leic.ps.eduWikiAPI.domain.outputModel.error

data class ErrorOutputModel(
        val title : String,
        val detail : String,
        val status : Int,
        val type: String
)