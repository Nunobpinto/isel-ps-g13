package isel.leic.ps.eduWikiAPI.domain.model;

import java.sql.Timestamp

data class Reputation (
        val id: Int = 0,
        val points: Int = 0,
        val rank: Int = 0,
        val version: Int = 0,
        val studentId: Int = 0
)