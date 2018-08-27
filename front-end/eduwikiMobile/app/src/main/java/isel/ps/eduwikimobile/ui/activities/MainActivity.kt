package isel.ps.eduwikimobile.ui.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.model.single.*
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.fragments.collection.ClassCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.CourseCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.ProgrammeCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.single.*
import isel.ps.eduwikimobile.comms.DownloadAsyncTask
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.paramsContainer.DownloadFileContainer
import isel.ps.eduwikimobile.paramsContainer.ResourceParametersContainer


class MainActivity : AppCompatActivity(), IDataComunication {

    lateinit var toolbar: ActionBar
    lateinit var fragmentsMap: HashMap<String, Fragment>
    lateinit var app: EduWikiApplication
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
    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = this.applicationContext as EduWikiApplication
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
            R.id.navigation_organization -> {
                loadFragment(fragmentsMap["organization"]!!)
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
                "course_class" to CourseClassFragment()
        )
    }

    fun <T> navigateToListItem(item: T, path: String?) {
        val mFragment = fragmentsMap[item.toString()]
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            DownloadAsyncTask().execute(DownloadFileContainer(url, applicationContext))
        }
    }

    fun downloadResource(sheetId: String) {
        app.controller.actionHandler(
                AppController.SPECIFIC_RESOURCE,
                ResourceParametersContainer(
                        activity = this,
                        resourceId = sheetId,
                        app = applicationContext as EduWikiApplication,
                        successCb = { _ -> Toast.makeText(this, "Download Completed", Toast.LENGTH_LONG).show() },
                        errorCb = { error -> Toast.makeText(this, "Error" + error.message, Toast.LENGTH_LONG).show() }
                )
        )
    }

}

