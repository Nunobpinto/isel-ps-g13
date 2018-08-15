package isel.ps.eduwikimobile.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.Course
import isel.ps.eduwikimobile.paramsContainer.CourseCollectionParametersContainer
import kotlinx.android.synthetic.main.courses_fragment.*

class CourseCollectionFragment : Fragment() {

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
        val view: View = inflater.inflate(R.layout.courses_fragment, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)

        fetchCourseItems()

        cAdapter = CourseListAdapter(context, courseList)
        recyclerView.adapter = cAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    fun fetchCourseItems() {
        AppController.actionHandler(
                AppController.ALL_COURSES,
                CourseCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { courses ->
                            courseList.addAll(courses.courseProgrammeList)
                            cAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            courses_progress_bar.visibility = View.GONE;
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
    }
}