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
import isel.ps.eduwikimobile.domain.model.single.Exam
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
class ExamFragment : Fragment() {

    lateinit var app: EduWikiApplication
    lateinit var dataComunication: IDataComunication
    lateinit var exam: Exam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.exam_details_fragment, container, false)

        val bundle: Bundle = arguments
        exam = bundle.getParcelable<Exam>("item_selected")

        dataComunication.setExam(exam)

        val examName = view.findViewById<TextView>(R.id.exam_details_name)
        val examVotes = view.findViewById<TextView>(R.id.exam_votes)
        val examDueDate = view.findViewById<TextView>(R.id.exam_due_date)
        val examLocation = view.findViewById<TextView>(R.id.exam_location)
        val examSheet = view.findViewById<Button>(R.id.download_exam_sheet)

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        val title: String = dataComunication.getCourse()!!.shortName + "/" + dataComunication.getTerm()!!.shortName + "/" + "Exams"
        mainActivity.toolbar.title = title
        mainActivity.toolbar.subtitle = exam.createdBy

        examName.text = exam.phase + " " + exam.type
        examVotes.text = exam.votes.toString()
        examDueDate.text = exam.dueDate
        examLocation.text = exam.location

        examSheet.setOnClickListener {
            downloadExamSheet(exam.sheetId)
        }

        return view
    }

    private fun downloadExamSheet(sheetId: String) {
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

