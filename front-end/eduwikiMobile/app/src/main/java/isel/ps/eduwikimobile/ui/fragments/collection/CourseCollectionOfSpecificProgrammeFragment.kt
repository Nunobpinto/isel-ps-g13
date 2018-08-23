package isel.ps.eduwikimobile.ui.fragments.collection

import android.content.Context
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
import isel.ps.eduwikimobile.paramsContainer.CourseProgrammeCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import kotlinx.android.synthetic.main.course_collection_fragment.*

class CourseCollectionOfSpecificProgrammeFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var coursesOfProgrammeList: MutableList<Course>
    private lateinit var cAdapter: CourseListAdapter
    lateinit var dataComunication: IDataComunication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        coursesOfProgrammeList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.course_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.courses_recycler_view)

        val programmeId = dataComunication.getProgramme()!!.programmeId

        if (coursesOfProgrammeList.size > 0) {
            coursesOfProgrammeList.clear()
        }

        fetchCoursesOfProgramme(programmeId)

        cAdapter = CourseListAdapter(context, coursesOfProgrammeList, dataComunication.getProgramme()!!.shortName)
        recyclerView.adapter = cAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun fetchCoursesOfProgramme(programmeId: Int) {
        AppController.actionHandler(
                AppController.ALL_COURSES_OF_SPECIFIC_PROGRAMME,
                CourseProgrammeCollectionParametersContainer(
                        programmeId = programmeId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { courses ->
                            coursesOfProgrammeList.addAll(courses.courseProgrammeList)
                            cAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            courses_progress_bar.visibility = View.GONE;
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            dataComunication = context as IDataComunication
        } catch (e: ClassCastException) {
            throw ClassCastException(e.message)
        }
    }

}