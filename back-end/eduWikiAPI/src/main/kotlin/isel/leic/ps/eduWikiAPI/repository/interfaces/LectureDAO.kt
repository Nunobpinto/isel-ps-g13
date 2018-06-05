package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import java.util.*

interface LectureDAO {
    /**
     * Main queries
     */
    fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture>

    fun getSpecificLectureFromCourseInClass(classId: Int): Optional<Lecture>
}