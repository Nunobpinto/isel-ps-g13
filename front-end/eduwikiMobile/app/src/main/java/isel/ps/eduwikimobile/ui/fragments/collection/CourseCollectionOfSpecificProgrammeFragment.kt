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
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseProgrammeListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.paramsContainer.CourseProgrammeCollectionParametersContainer
import isel.ps.eduwikimobile.domain.single.CourseProgramme
import isel.ps.eduwikimobile.ui.IDataComunication
import kotlinx.android.synthetic.main.course_collection_fragment.*

class CourseCollectionOfSpecificProgrammeFragment : Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var coursesOfProgrammeList: MutableList<CourseProgramme>
    private lateinit var cAdapter: CourseProgrammeListAdapter
    private lateinit var dataComunication: IDataComunication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        coursesOfProgrammeList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.course_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.courses_recycler_view)

        if (coursesOfProgrammeList.size != 0) {
            coursesOfProgrammeList.clear()
        }
        view.findViewById<ProgressBar>(R.id.courses_progress_bar).visibility = View.VISIBLE
        getCoursesOfProgramme(dataComunication.getProgramme()!!.programmeId)

        cAdapter = CourseProgrammeListAdapter(context, coursesOfProgrammeList)
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

    private fun getCoursesOfProgramme(programmeId: Int) {
        app.controller.actionHandler(
                AppController.ALL_COURSES_OF_SPECIFIC_PROGRAMME,
                CourseProgrammeCollectionParametersContainer(
                        programmeId = programmeId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { courses ->
                            coursesOfProgrammeList.addAll(courses.courseProgrammeList)
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            dataComunication = context as IDataComunication
        } catch (e: ClassCastException) {
            throw ClassCastException(e.message)
        }
    }

}