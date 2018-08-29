package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.ActivityCompat
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
import isel.ps.eduwikimobile.domain.single.Exam
import isel.ps.eduwikimobile.domain.single.Term
import isel.ps.eduwikimobile.domain.paramsContainer.ResourceParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class ExamFragment : Fragment() {

    lateinit var dataComunication: IDataComunication
    lateinit var app: EduWikiApplication
    lateinit var exam: Exam
    var course: Course? = null
    var term: Term? = null

    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.exam_details_fragment, container, false)
        val bundle: Bundle = arguments
        exam = bundle.getParcelable("item_selected")
        course = dataComunication.getCourse()
        term = dataComunication.getTerm()
        dataComunication.setExam(exam)

        val examName = view.findViewById<TextView>(R.id.exam_details_name)
        val examVotes = view.findViewById<TextView>(R.id.exam_votes)
        val examDueDate = view.findViewById<TextView>(R.id.exam_due_date)
        val examLocation = view.findViewById<TextView>(R.id.exam_location)
        val examSheet = view.findViewById<Button>(R.id.download_exam_sheet)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        if (course != null && term != null) {
            mainActivity.toolbar.title = course!!.shortName + "/" + term!!.shortName + "/" + "Exams"
        } else { //TODO pedido para obter o course
            mainActivity.toolbar.title = "Exam"
        }
        mainActivity.toolbar.subtitle = exam.createdBy
        examName.text = exam.phase + " " + exam.type
        examVotes.text = exam.votes.toString()
        examDueDate.text = exam.dueDate
        examLocation.text = exam.location
        if (exam.sheetId != null) {
            examSheet.visibility = View.VISIBLE
            examSheet.setOnClickListener {
                app.controller.actionHandler(
                        AppController.SPECIFIC_RESOURCE,
                        ResourceParametersContainer(
                                activity = mainActivity,
                                resourceId = exam.sheetId!!,
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

