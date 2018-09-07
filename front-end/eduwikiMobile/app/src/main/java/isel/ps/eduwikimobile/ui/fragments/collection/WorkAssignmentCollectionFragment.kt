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
import isel.ps.eduwikimobile.adapters.WorkAssignmentListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.single.Term
import isel.ps.eduwikimobile.domain.single.WorkAssignment
import isel.ps.eduwikimobile.domain.paramsContainer.WorkAssignmentCollectionParametersContainer
import isel.ps.eduwikimobile.domain.single.CourseProgramme
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.work_assignment_collection_fragment.*

class WorkAssignmentCollectionFragment : Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var workAssignmentList: MutableList<WorkAssignment>
    private lateinit var workAssignmentAdapter: WorkAssignmentListAdapter
    private lateinit var dataComunication: IDataComunication
    private lateinit var mainActivity: MainActivity
    private var course: Course? = null
    private var courseProgramme: CourseProgramme? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = activity.applicationContext as EduWikiApplication
        workAssignmentList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.work_assignment_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.work_assignments_recycler_view)

        val bundle: Bundle = arguments
        val term: Term = bundle.getParcelable("actualTerm")
        dataComunication.setTerm(term)

        if (workAssignmentList.size != 0) {
            workAssignmentList.clear()
        }
        view.findViewById<ProgressBar>(R.id.work_assignments_progress_bar).visibility = View.VISIBLE

        if (dataComunication.getCourse() != null) {
            course = dataComunication.getCourse()
            getWorkAssignmentItems(course!!.courseId, term.termId)
            mainActivity.toolbar.title = course!!.shortName + "/" + term.shortName + "/" + "Work-Assignments"
            mainActivity.toolbar.subtitle = course!!.createdBy
        } else {
            courseProgramme = dataComunication.getCourseProgramme()
            getWorkAssignmentItems(courseProgramme!!.courseId, term.termId)
            mainActivity.toolbar.title = courseProgramme!!.shortName + "/" + term.shortName + "/" + "Work-Assignments"
            mainActivity.toolbar.subtitle = courseProgramme!!.createdBy
        }

        workAssignmentAdapter = WorkAssignmentListAdapter(context, workAssignmentList)
        recyclerView.adapter = workAssignmentAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true
        return view
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getWorkAssignmentItems(courseId: Int, termId: Int) {
        app.controller.actionHandler(
                AppController.WORK_ASSIGNMENTS,
                WorkAssignmentCollectionParametersContainer(
                        termId = termId,
                        courseId = courseId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { works ->
                            workAssignmentList.addAll(works.workAssignmentList)
                            workAssignmentAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            work_assignments_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if (error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            } else {
                                work_assignments_progress_bar.visibility = View.GONE
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