package isel.ps.eduwikimobile.ui.activities

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.model.collection.ExamCollection
import isel.ps.eduwikimobile.domain.model.single.*
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.fragments.*

class MainActivity : AppCompatActivity(), IDataComunication {

    lateinit var toolbar: ActionBar
    lateinit var map: HashMap<String, Fragment>
    var actualProgramme: Programme? = null
    var actualCourse: Course? = null
    var actualClass: Class? = null
    var actualOrganization: Organization? = null
    var actualTerm: Term? = null

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
        val fragment: Fragment
        when (item.itemId) {
            R.id.navigation_home -> {
                toolbar.title = "Home"
                toolbar.subtitle = ""
                loadFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_classes -> {
                loadFragment(ClassCollectionFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_programmes -> {
                loadFragment(ProgrammeCollectionFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_courses -> {
                loadFragment(CourseCollectionFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_organization -> {
                loadFragment(OrganizationFragment())
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
        map.put("WorkAssignment", WorkAssignmentFragment())
    }

    fun <T> navigateToListItem (item: T) {
        val mFragment = map[item.toString()]
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

    override fun getProgramme(): Programme = actualProgramme!!

    override fun setProgramme(programme: Programme) { actualProgramme = programme }

    override fun getClass(): Class = actualClass!!

    override fun setClass(klass: Class) { actualClass = klass }

    override fun getCourse(): Course = actualCourse!!

    override fun setCourse(course: Course) { actualCourse = course }

    override fun getOrganization(): Organization = actualOrganization!!

    override fun setOrganization(org: Organization) { actualOrganization = org}

    override fun getTerm(): Term = actualTerm!!

    override fun setTerm(term: Term) { actualTerm = term }

}
