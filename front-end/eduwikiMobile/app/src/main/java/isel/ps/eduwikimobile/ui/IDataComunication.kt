package isel.ps.eduwikimobile.ui

import isel.ps.eduwikimobile.domain.model.single.*

interface IDataComunication {
    fun getProgramme(): Programme?
    fun setProgramme(programme: Programme)
    fun getClass(): Class?
    fun setClass(klass: Class)
    fun getCourse(): Course?
    fun setCourse(course: Course)
    fun getOrganization(): Organization?
    fun setOrganization(org: Organization)
    fun getTerm(): Term?
    fun setTerm(term: Term)
    fun getExam(): Exam?
    fun setExam(exam: Exam)
    fun getWorkAssignment(): WorkAssignment?
    fun setWorkAssignment(workAssignment: WorkAssignment)
    fun getCourseClass(): CourseClass?
    fun setCourseClass(courseClass: CourseClass)
    fun getHomework(): Homework?
    fun setHomework(homework: Homework)
    fun getLecture(): Lecture?
    fun setLecture(lecture: Lecture)
}