package isel.leic.ps.eduWikiAPI.domain.model.report

import java.time.Duration

data class ProgrammeReport (
        val reportId:Int = 0,
        val programmeId: Int = 0,
        val programmeFullName: String = "",
        val programmeShortName: String = "",
        val programmeAcademicDegree: String = "",
        val programmeTotalCredits: Int = 0,
        val programmeDuration: Duration = Duration.ZERO,
        val madeBy:String = "",
        val votes: Int = 0
)