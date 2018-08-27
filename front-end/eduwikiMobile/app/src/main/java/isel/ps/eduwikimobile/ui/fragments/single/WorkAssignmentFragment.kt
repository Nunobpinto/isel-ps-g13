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
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.model.single.WorkAssignment
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class WorkAssignmentFragment : Fragment() {

    lateinit var dataComunication: IDataComunication
    lateinit var workAssignment: WorkAssignment
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.work_assignment_details_fragment, container, false)

        val bundle: Bundle = arguments
        workAssignment = bundle.getParcelable("item_selected")

        dataComunication.setWorkAssignment(workAssignment)

        val workAssignmentName = view.findViewById<TextView>(R.id.work_assignment_details_name)
        val workAssignmentVotes = view.findViewById<TextView>(R.id.work_assignment_votes)
        val workAssignmentDueDate = view.findViewById<TextView>(R.id.work_assignment_due_date)
        val workAssignmentIndividual = view.findViewById<TextView>(R.id.work_assignment_individual)
        val workAssignmentRequiresReport = view.findViewById<TextView>(R.id.work_assignment_requires_report)
        val workAssignmentMultipleDeliveries = view.findViewById<TextView>(R.id.work_assignment_multiple_deliveries)
        val workAssignmentLateDelivery = view.findViewById<TextView>(R.id.work_assignment_late_delivery)
        val workAssignmentSupplement = view.findViewById<Button>(R.id.download_work_assignment_supplement)
        val workAssignmentSheet = view.findViewById<Button>(R.id.download_work_assignment_sheet)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        val title: String = dataComunication.getCourse()!!.shortName + "/" + dataComunication.getTerm()!!.shortName + "/" + "Work-Assignments"
        mainActivity.toolbar.title = title
        mainActivity.toolbar.subtitle = workAssignment.createdBy

        workAssignmentName.text = workAssignment.phase + "Work-Assignment"
        workAssignmentVotes.text = workAssignment.votes.toString()
        workAssignmentDueDate.text = workAssignment.dueDate
        workAssignmentIndividual.text = if (workAssignment.individual) "Yes" else "No"
        workAssignmentRequiresReport.text = if (workAssignment.requiresReport) "Yes" else "No"
        workAssignmentMultipleDeliveries.text = if (workAssignment.multipleDeliveries) "Yes" else "No"
        workAssignmentLateDelivery.text = if (workAssignment.lateDelivery) "Yes" else "No"

        workAssignmentSheet.visibility = if (workAssignment.sheetId != null) View.VISIBLE else View.GONE
        workAssignmentSupplement.visibility = if (workAssignment.supplementId != null) View.VISIBLE else View.GONE

        workAssignmentSupplement.setOnClickListener {
            mainActivity.downloadResource(workAssignment.supplementId)
        }

        workAssignmentSheet.setOnClickListener {
            mainActivity.downloadResource(workAssignment.sheetId)
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