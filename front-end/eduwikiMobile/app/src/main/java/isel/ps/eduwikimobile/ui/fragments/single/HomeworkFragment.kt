package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.CourseClass
import isel.ps.eduwikimobile.domain.model.single.Homework
import isel.ps.eduwikimobile.paramsContainer.ResourceParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class HomeworkFragment : Fragment() {

    lateinit var dataComunication: IDataComunication
    lateinit var homework: Homework
    lateinit var mainActivity: MainActivity
    lateinit var app: EduWikiApplication
    var courseClass: CourseClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.homework_details_fragment, container, false)
        val bundle: Bundle = arguments
        homework = bundle.getParcelable("item_selected")
        dataComunication.setHomework(homework)
        courseClass = dataComunication.getCourseClass()

        val homeworkName = view.findViewById<TextView>(R.id.homework_details_name)
        val homeworkDueDate = view.findViewById<TextView>(R.id.homework_details_insert_due_date)
        val homeworkLateDelivery = view.findViewById<Switch>(R.id.homework_details_switch_late_delivery)
        val homeworkMultipleDeliveries = view.findViewById<Switch>(R.id.homework_details_switch_multiple_deliveries)
        val homeworkSheet = view.findViewById<TextView>(R.id.homework_details_sheet)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        if (courseClass != null) {
            mainActivity.toolbar.title = courseClass!!.lecturedTerm + "/" + courseClass!!.className + "/" + courseClass!!.courseShortName + "/Homework"
        } else { //TODO pedido para obter o course
            mainActivity.toolbar.title = "Homework"
        }
        mainActivity.toolbar.subtitle = homework.createdBy

        homeworkName.text = homework.homeworkName
        homeworkDueDate.text = homework.dueDate
        homeworkLateDelivery.isChecked = homework.lateDelivery
        homeworkMultipleDeliveries.isChecked = homework.multipleDeliveries
        if (homework.sheetId != null) {
            homeworkSheet.visibility = View.VISIBLE
            homeworkSheet.setOnClickListener {
                app.controller.actionHandler(
                        AppController.SPECIFIC_RESOURCE,
                        ResourceParametersContainer(
                                activity = mainActivity,
                                resourceId = homework.sheetId!!,
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