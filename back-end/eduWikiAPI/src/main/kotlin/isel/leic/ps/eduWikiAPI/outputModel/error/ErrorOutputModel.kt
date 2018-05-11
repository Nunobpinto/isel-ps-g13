package isel.leic.ps.eduWikiAPI.outputModel.error

data class ErrorOutputModel(
        val title : String,
        val detail : String,
        val status : Int
)