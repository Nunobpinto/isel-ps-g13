package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.single.Term
import isel.ps.eduwikimobile.domain.single.WorkAssignment
import isel.ps.eduwikimobile.domain.paramsContainer.ResourceParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class WorkAssignmentFragment : Fragment() {

    lateinit var dataComunication: IDataComunication
    lateinit var workAssignment: WorkAssignment
    lateinit var mainActivity: MainActivity
    lateinit var app: EduWikiApplication
    var course: Course? = null
    var term: Term? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.work_assignment_details_fragment, container, false)
        val bundle: Bundle = arguments
        workAssignment = bundle.getParcelable("item_selected")
        dataComunication.setWorkAssignment(workAssignment)
        course = dataComunication.getCourse()
        term = dataComunication.getTerm()

        val workAssignmentName = view.findViewById<TextView>(R.id.work_assignment_details_name)
        val workAssignmentVotes = view.findViewById<TextView>(R.id.work_assignment_votes)
        val workAssignmentDueDate = view.findViewById<TextView>(R.id.lecture_details_week_day)
        val workAssignmentIndividual = view.findViewById<TextView>(R.id.work_assignment_individual)
        val workAssignmentRequiresReport = view.findViewById<TextView>(R.id.insert_lecture_location)
        val workAssignmentMultipleDeliveries = view.findViewById<TextView>(R.id.lecture_details_duration)
        val workAssignmentLateDelivery = view.findViewById<TextView>(R.id.lecture_details_begins)
        val workAssignmentSupplement = view.findViewById<Button>(R.id.download_work_assignment_supplement)
        val workAssignmentSheet = view.findViewById<Button>(R.id.download_work_assignment_sheet)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        if (course != null && term != null) {
            mainActivity.toolbar.title = course!!.shortName + "/" + term!!.shortName + "/" + "Work-Assignments"
        } else { //TODO pedido para obter o course
            mainActivity.toolbar.title = "Work-Assignments"
        }
        mainActivity.toolbar.subtitle = workAssignment.createdBy

        workAssignmentName.text = workAssignment.phase + "Work-Assignment"
        workAssignmentVotes.text = workAssignment.votes.toString()
        workAssignmentDueDate.text = workAssignment.dueDate
        workAssignmentIndividual.text = if (workAssignment.individual) "Yes" else "No"
        workAssignmentRequiresReport.text = if (workAssignment.requiresReport) "Yes" else "No"
        workAssignmentMultipleDeliveries.text = if (workAssignment.multipleDeliveries) "Yes" else "No"
        workAssignmentLateDelivery.text = if (workAssignment.lateDelivery) "Yes" else "No"

        if (workAssignment.supplementId != null) {
            workAssignmentSupplement.visibility = View.VISIBLE
            workAssignmentSupplement.setOnClickListener {
                app.controller.actionHandler(
                        AppController.SPECIFIC_RESOURCE,
                        ResourceParametersContainer(
                                activity = mainActivity,
                                resourceId = workAssignment.supplementId!!,
                                app = app,
                                successCb = { _ -> Toast.makeText(mainActivity, "Download Completed", Toast.LENGTH_LONG).show() },
                                errorCb = { error -> Toast.makeText(mainActivity, "Error" + error.message, Toast.LENGTH_LONG).show() }
                        )
                )
            }
        }

        if (workAssignment.sheetId != null) {
            workAssignmentSheet.visibility = View.VISIBLE
            workAssignmentSheet.setOnClickListener {
                app.controller.actionHandler(
                        AppController.SPECIFIC_RESOURCE,
                        ResourceParametersContainer(
                                activity = mainActivity,
                                resourceId = workAssignment.sheetId!!,
                                app = app,
                                successCb = { _ -> Toast.makeText(mainActivity, "Download Completed", Toast.LENGTH_LONG).show() },
                                errorCb = { error -> Toast.makeText(mainActivity, "Error" + error.message, Toast.LENGTH_LONG).show() }
                        )
                )
            }
        }

        return view
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