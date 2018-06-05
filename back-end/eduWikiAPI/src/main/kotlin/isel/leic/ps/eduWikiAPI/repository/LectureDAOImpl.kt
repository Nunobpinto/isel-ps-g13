package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LectureDAOImpl: LectureDAO {

    @Autowired
    lateinit var handle: Handle

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): List<Lecture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificLectureFromCourseInClass(classId: Int): Optional<Lecture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}