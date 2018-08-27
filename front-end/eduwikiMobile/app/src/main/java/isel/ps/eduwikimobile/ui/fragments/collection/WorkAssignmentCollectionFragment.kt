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
import isel.ps.eduwikimobile.adapters.WorkAssignmentListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.Course
import isel.ps.eduwikimobile.domain.model.single.Term
import isel.ps.eduwikimobile.domain.model.single.WorkAssignment
import isel.ps.eduwikimobile.paramsContainer.WorkAssignmentCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.work_assignment_collection_fragment.*

class WorkAssignmentCollectionFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var workAssignmentList: MutableList<WorkAssignment>
    private lateinit var workAssignmentAdapter: WorkAssignmentListAdapter
    lateinit var dataComunication: IDataComunication
    lateinit var course: Course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        workAssignmentList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.work_assignment_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.work_assignments_recycler_view)

        val bundle: Bundle = arguments
        val term: Term = bundle.getParcelable("actualTerm")

        course = dataComunication.getCourse()!!
        dataComunication.setTerm(term)

        val activity = activity as MainActivity
        activity.toolbar.title = course.shortName + "/" + term.shortName + "/" + "Work-Assignments"
        activity.toolbar.subtitle = ""

        fetchWorkAssignmentItems(course.courseId, term.termId)

        workAssignmentAdapter = WorkAssignmentListAdapter(context, workAssignmentList)
        recyclerView.adapter = workAssignmentAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true
        return view
    }


    private fun fetchWorkAssignmentItems(courseId: Int, termId: Int) {
        app.controller.actionHandler(
                AppController.WORK_ASSIGNMENTS,
                WorkAssignmentCollectionParametersContainer(
                        termId = termId,
                        courseId = courseId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { works ->
                            workAssignmentList.addAll(works.workAssignmentList)
                            workAssignmentAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            work_assignments_progress_bar.visibility = View.GONE;
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