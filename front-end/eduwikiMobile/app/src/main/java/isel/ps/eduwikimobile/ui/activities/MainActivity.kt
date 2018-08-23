package isel.ps.eduwikimobile.ui.activities

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.model.single.*
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.fragments.collection.ClassCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.CourseCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.ProgrammeCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.single.*

class MainActivity : AppCompatActivity(), IDataComunication {

    lateinit var toolbar: ActionBar
    lateinit var map: HashMap<String, Fragment>
    var actualProgramme: Programme? = null
    var actualCourse: Course? = null
    var actualClass: Class? = null
    var actualOrganization: Organization? = null
    var actualTerm: Term? = null
    var actualExam: Exam? = null
    var actualWorkAssignment: WorkAssignment? = null
    var actualCourseClass: CourseClass? = null
    var actualLecture: Lecture? = null
    var actualHomework: Homework? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initiateAndPopulateMap()
        toolbar = this.supportActionBar!!
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        toolbar.title = "Home"
        loadFragment(HomeFragment())
    }

    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                toolbar.title = "Home"
                toolbar.subtitle = ""
                loadFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_classes -> {
                loadFragment(map["Class Collection"]!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_programmes -> {
                loadFragment(map["Programme Collection"]!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_courses -> {
                loadFragment(map["Course Collection"]!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_organization -> {
                loadFragment(map["Organization"]!!)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun initiateAndPopulateMap() {
        map = HashMap()
        map.put("Programme", ProgrammeFragment())
        map.put("Course", CourseFragment())
        map.put("Class", ClassFragment())
        map.put("Organization", OrganizationFragment())
        map.put("Exam", ExamFragment())
        map.put("Work Assignment", WorkAssignmentFragment())
        map.put("Class Collection", ClassCollectionFragment())
        map.put("Course Collection", CourseCollectionFragment())
        map.put("Programme Collection", ProgrammeCollectionFragment())
        map.put("Course Class", CourseClassFragment())
    }

    fun <T> navigateToListItem(item: T, path: String?) {
        val mFragment = map[item.toString()]
        val mBundle = Bundle()
        if (path != null) {
            mBundle.putString("path", path)
        }
        mBundle.putParcelable("item_selected", item as Parcelable)
        mFragment!!.arguments = mBundle
        loadFragment(mFragment)
    }

    fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun getProgramme(): Programme? = actualProgramme

    override fun setProgramme(programme: Programme) {
        actualProgramme = programme
    }

    override fun getClass(): Class? = actualClass

    override fun setClass(klass: Class) {
        actualClass = klass
    }

    override fun getCourse(): Course? = actualCourse

    override fun setCourse(course: Course) {
        actualCourse = course
    }

    override fun getOrganization(): Organization? = actualOrganization

    override fun setOrganization(org: Organization) {
        actualOrganization = org
    }

    override fun getTerm(): Term? = actualTerm

    override fun setTerm(term: Term) {
        actualTerm = term
    }

    override fun getExam(): Exam? = actualExam

    override fun setExam(exam: Exam) {
        actualExam = exam
    }

    override fun getWorkAssignment(): WorkAssignment? = actualWorkAssignment

    override fun setWorkAssignment(workAssignment: WorkAssignment) {
        actualWorkAssignment = workAssignment
    }

    override fun getCourseClass(): CourseClass? = actualCourseClass

    override fun setCourseClass(courseClass: CourseClass) {
        actualCourseClass = courseClass
    }

    override fun getHomework(): Homework? = actualHomework

    override fun setHomework(homework: Homework) {
        actualHomework = homework
    }

    override fun getLecture(): Lecture? = actualLecture

    override fun setLecture(lecture: Lecture) {
        actualLecture = lecture
    }

}
