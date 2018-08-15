package isel.ps.eduwikimobile.ui.activities

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Display
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.ui.fragments.*


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    lateinit var map: HashMap<String, Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initiateAndPopulateMap()
        toolbar = this.supportActionBar!!

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        toolbar.setTitle("Home")
        loadFragment(HomeFragment())
    }

    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment
        when (item.getItemId()) {
            R.id.navigation_home -> {
                toolbar.title = "Home"
                toolbar.subtitle = ""
                fragment = HomeFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_classes -> {
                toolbar.title = "Classes"
                toolbar.subtitle = ""
                fragment = ClassesFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_programmes -> {
                toolbar.title = "Programmes"
                toolbar.subtitle = ""
                fragment = ProgrammeCollectionFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_courses -> {
                toolbar.title = "Courses"
                toolbar.subtitle = ""
                fragment = CourseCollectionFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_organization -> {
                toolbar.title = "Organization"
                toolbar.subtitle = ""
                fragment = OrganizationFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun initiateAndPopulateMap() {
        map = HashMap()
        map.put("Programme", ProgrammeFragment())
        map.put("Course", CourseFragment())
    }

    fun <T> navigateToListItem (item: T) {
        val name = item.toString()
        val mFragment = map[item.toString()]
        val mBundle = Bundle()
        mBundle.putParcelable("item_selected", item as Parcelable);
        mFragment!!.arguments = mBundle
        loadFragment(mFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
