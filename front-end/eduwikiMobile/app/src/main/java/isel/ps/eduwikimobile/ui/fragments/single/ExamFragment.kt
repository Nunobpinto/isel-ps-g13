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
import com.android.volley.TimeoutError
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

    private lateinit var dataComunication: IDataComunication
    private lateinit var app: EduWikiApplication
    private lateinit var exam: Exam
    private var term: Term? = null
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.exam_details_fragment, container, false)
        val bundle: Bundle = arguments
        exam = bundle.getParcelable("item_selected")
        term = dataComunication.getTerm()

        val examName = view.findViewById<TextView>(R.id.exam_details_name)
        val examDueDate = view.findViewById<TextView>(R.id.exam_details_insert_due_date)
        val examLocation = view.findViewById<TextView>(R.id.exam_details_insert_location)
        val examSheet = view.findViewById<Button>(R.id.download_exam_sheet)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = exam.courseShortName + "/" + exam.termShortName + "/" + "Exams"
        mainActivity.toolbar.subtitle = exam.createdBy

        examName.text = "${exam.phase}/${exam.type}"
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
                                errorCb = { error ->
                                    if (error.exception is TimeoutError) {
                                        Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                                    } else Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                                }
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

