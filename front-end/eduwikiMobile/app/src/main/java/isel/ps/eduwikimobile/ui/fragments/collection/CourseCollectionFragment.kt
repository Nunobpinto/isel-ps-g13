package isel.ps.eduwikimobile.ui.fragments.collection

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.paramsContainer.CourseCollectionParametersContainer
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.course_collection_fragment.*

class CourseCollectionFragment : Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseList: MutableList<Course>
    private lateinit var cAdapter: CourseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        courseList = ArrayList()
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.toolbar.title = "Courses"
        activity.toolbar.subtitle = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.course_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.courses_recycler_view)

        if (courseList.size != 0) {
            courseList.clear()
        }

        view.findViewById<ProgressBar>(R.id.courses_progress_bar).visibility = View.VISIBLE
        getCourseItems()

        cAdapter = CourseListAdapter(context, courseList)
        recyclerView.adapter = cAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getCourseItems() {
        app.controller.actionHandler(
                AppController.ALL_COURSES,
                CourseCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { courses ->
                            courseList.addAll(courses.courseList)
                            cAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            courses_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if (error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            } else {
                                courses_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )
    }

}