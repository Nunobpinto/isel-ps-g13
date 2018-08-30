package isel.ps.eduwikimobile.ui.fragments.collection

import android.content.Context
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
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.paramsContainer.CourseCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import kotlinx.android.synthetic.main.course_collection_fragment.*

class UserCourseCollectionFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseList: MutableList<Course>
    private lateinit var cAdapter: CourseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        courseList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.course_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.courses_recycler_view)

        if (courseList.size == 0){
            view.findViewById<ProgressBar>(R.id.courses_progress_bar).visibility = View.VISIBLE
            fetchCourseItems()
        }

        cAdapter = CourseListAdapter(context, courseList, null)
        recyclerView.adapter = cAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun fetchCourseItems() {
        app.controller.actionHandler(
                AppController.USER_FOLLOWING_COURSES,
                CourseCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { courses ->
                            courseList.addAll(courses.courseList)
                            cAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            courses_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
    }

}