package isel.ps.eduwikimobile.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.ui.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = this.supportActionBar!!

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        toolbar.setTitle("Home")
        loadFragment(HomeFragment())
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment
        when (item.getItemId()) {
            R.id.navigation_home -> {
                toolbar.setTitle("Home")
                fragment = HomeFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_classes -> {
                toolbar.setTitle("Classes")
                fragment = ClassesFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_programmes -> {
                toolbar.setTitle("Programmes")
                fragment = ProgrammesFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_courses -> {
                toolbar.setTitle("Courses")
                fragment = CoursesFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_organization -> {
                toolbar.setTitle("Organization")
                fragment = OrganizationFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
