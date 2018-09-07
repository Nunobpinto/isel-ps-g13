package isel.ps.eduwikimobile.ui

import isel.ps.eduwikimobile.domain.single.*

interface IDataComunication {
    fun getProgramme(): Programme?
    fun setProgramme(programme: Programme)
    fun getCourse(): Course?
    fun setCourse(course: Course)
    fun getTerm(): Term?
    fun setTerm(term: Term)
    fun getCourseClass(): CourseClass?
    fun setCourseClass(courseClass: CourseClass)
    fun getCourseProgramme() : CourseProgramme?
    fun setCourseProgramme(courseProgramme: CourseProgramme)
}