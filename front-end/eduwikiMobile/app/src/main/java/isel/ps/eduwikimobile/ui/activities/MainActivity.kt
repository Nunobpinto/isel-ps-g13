package isel.ps.eduwikimobile.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.single.*
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.fragments.collection.ClassCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.CourseCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.ProgrammeCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.single.*
import isel.ps.eduwikimobile.comms.Session


class MainActivity : AppCompatActivity(), IDataComunication {

    lateinit var toolbar: ActionBar
    lateinit var fragmentsMap: HashMap<String, Fragment>
    var actualProgramme: Programme? = null
    var actualCourse: Course? = null
    var actualTerm: Term? = null
    var actualCourseClass: CourseClass? = null
    var actualCourseProgramme: CourseProgramme? = null

    lateinit var fileUrl: String
    lateinit var fileHeader: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initiateAndPopulateFragmentsMap()

        toolbar = this.supportActionBar!!
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        toolbar.title = "Home"
        loadFragment(fragmentsMap["home"]!!)
    }

    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                loadFragment(fragmentsMap["home"]!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_classes -> {
                loadFragment(fragmentsMap["class_collection"]!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_programmes -> {
                loadFragment(fragmentsMap["programme_collection"]!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_courses -> {
                loadFragment(fragmentsMap["course_collection"]!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                loadFragment(fragmentsMap["profile"]!!)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun initiateAndPopulateFragmentsMap() {
        fragmentsMap = hashMapOf(
                "home" to HomeFragment(),
                "programme" to ProgrammeFragment(),
                "course" to CourseFragment(),
                "class" to ClassFragment(),
                "organization" to OrganizationFragment(),
                "exam" to ExamFragment(),
                "work_assignment" to WorkAssignmentFragment(),
                "class_collection" to ClassCollectionFragment(),
                "course_collection" to CourseCollectionFragment(),
                "programme_collection" to ProgrammeCollectionFragment(),
                "course_class" to CourseClassFragment(),
                "lecture" to LectureFragment(),
                "homework" to HomeworkFragment(),
                "profile" to ProfileFragment(),
                "course_programme" to CourseProgrammeFragment()
        )
    }

    fun <T> navigateToListItem(item: T) {
        val mFragment = fragmentsMap[item.toString()]
        val mBundle = Bundle()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            (application as EduWikiApplication).repository.downloadFile(fileHeader, fileUrl, application as EduWikiApplication)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu!!.getItem(0).title = Session().getAuthUsername(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == R.id.logoutBtn) {
            Session().setLogout(this)
            startActivity(Intent(baseContext, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getProgramme(): Programme? = actualProgramme

    override fun setProgramme(programme: Programme) {
        actualProgramme = programme
    }

    override fun getCourse(): Course? = actualCourse

    override fun setCourse(course: Course) {
        actualCourse = course
    }

    override fun getTerm(): Term? = actualTerm

    override fun setTerm(term: Term) {
        actualTerm = term
    }

    override fun getCourseClass(): CourseClass? = actualCourseClass

    override fun setCourseClass(courseClass: CourseClass) {
        actualCourseClass = courseClass
    }

    override fun getCourseProgramme(): CourseProgramme? = actualCourseProgramme

    override fun setCourseProgramme(courseProgramme: CourseProgramme) {
        actualCourseProgramme = courseProgramme
    }

}

