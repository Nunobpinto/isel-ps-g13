package isel.ps.eduwikimobile.ui.fragments.single

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
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
import isel.ps.eduwikimobile.adapters.CourseClassListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Class
import isel.ps.eduwikimobile.domain.single.CourseClass
import isel.ps.eduwikimobile.domain.paramsContainer.CoursesOfSpecificClassParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.course_collection_fragment.*

class ClassFragment : Fragment() {

    lateinit var app: EduWikiApplication
    lateinit var dataComunication: IDataComunication
    lateinit var courseClassList: MutableList<CourseClass>
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseClassListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        courseClassList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.course_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.courses_recycler_view)

        val bundle: Bundle = arguments
        val klass = bundle.getParcelable<Class>("item_selected")

        if(courseClassList.size > 0) {
            courseClassList.clear()
        }
        view.findViewById<ProgressBar>(R.id.courses_progress_bar).visibility = View.VISIBLE
        fetchAllCoursesOfClass(klass.classId)

        courseAdapter = CourseClassListAdapter(context, courseClassList)
        recyclerView.adapter = courseAdapter

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = klass.lecturedTerm + "/" + klass.className + "/" + "Courses"
        mainActivity.toolbar.subtitle = klass.createdBy

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true
        return view
    }

    private fun fetchAllCoursesOfClass(classId: Int) {
        app.controller.actionHandler(
                AppController.ALL_COURSES_OF_SPECIFIC_CLASS,
                CoursesOfSpecificClassParametersContainer(
                        classId = classId,
                        app = app,
                        successCb = { courses ->
                            courseClassList.addAll(courses.courseClassList)
                            courseAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            courses_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
    }

}