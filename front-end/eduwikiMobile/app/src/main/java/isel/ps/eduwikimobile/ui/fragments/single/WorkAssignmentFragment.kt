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
import isel.ps.eduwikimobile.domain.model.single.WorkAssignment
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class WorkAssignmentFragment : Fragment() {

    lateinit var app: EduWikiApplication
    lateinit var dataComunication: IDataComunication
    lateinit var workAssignment: WorkAssignment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
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

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        val title: String = dataComunication.getCourse()!!.shortName + "/" + dataComunication.getTerm()!!.shortName + "/" + "Work-Assignments"
        mainActivity.toolbar.title = title
        mainActivity.toolbar.subtitle = workAssignment.createdBy

        workAssignmentName.text = workAssignment.phase + "Work-Assignment"
        workAssignmentVotes.text = workAssignment.votes.toString()
        workAssignmentDueDate.text = workAssignment.dueDate
        workAssignmentIndividual.text = if(workAssignment.individual) "Yes" else "No"
        workAssignmentRequiresReport.text = if(workAssignment.requiresReport) "Yes" else "No"
        workAssignmentMultipleDeliveries.text = if(workAssignment.multipleDeliveries) "Yes" else "No"
        workAssignmentLateDelivery.text = if(workAssignment.lateDelivery) "Yes" else "No"

        workAssignmentSupplement.setOnClickListener {
            downloadWorkAssignmentSupplement(workAssignment.supplementId)
        }

        workAssignmentSheet.setOnClickListener {
            downloadWorkAssignmentSheet(workAssignment.supplementId)
        }

        return view
    }

    private fun downloadWorkAssignmentSheet(sheetId: String) {
        Toast.makeText(context, "Download", Toast.LENGTH_LONG).show()
        /* val uri = Uri.parse(API_URL + "/resources/" + sheetId)
         val req = DownloadManager.Request(uri)
         req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
         req.setAllowedOverRoaming(false)
         req.setTitle(exam.phase + exam.type)
         req.setVisibleInDownloadsUi(true)
         req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, exam.phase + exam.type)

         //if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

         val activity = activity as MainActivity
         activity.refId = activity.downloadManager.enqueue(req)
         activity.resourceList.add(activity.refId!!)*/
    }

    private fun downloadWorkAssignmentSupplement(supplementId: String) {
        Toast.makeText(context, "Download", Toast.LENGTH_LONG).show()
        /* val uri = Uri.parse(API_URL + "/resources/" + sheetId)
         val req = DownloadManager.Request(uri)
         req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
         req.setAllowedOverRoaming(false)
         req.setTitle(exam.phase + exam.type)
         req.setVisibleInDownloadsUi(true)
         req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, exam.phase + exam.type)

         //if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

         val activity = activity as MainActivity
         activity.refId = activity.downloadManager.enqueue(req)
         activity.resourceList.add(activity.refId!!)*/
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