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
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.ExamListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.single.Exam
import isel.ps.eduwikimobile.domain.single.Term
import isel.ps.eduwikimobile.domain.paramsContainer.ExamCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.exam_collection_fragment.*

class ExamCollectionFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var examList: MutableList<Exam>
    private lateinit var examAdapter: ExamListAdapter
    lateinit var dataComunication: IDataComunication
    lateinit var course: Course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        examList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.exam_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.exams_recycler_view)

        val bundle: Bundle = arguments
        val term: Term = bundle.getParcelable("actualTerm")

        course = dataComunication.getCourse()!!
        dataComunication.setTerm(term)

        val activity = activity as MainActivity
        activity.toolbar.title = course.shortName + "/" + term.shortName + "/" + "Exams"
        activity.toolbar.subtitle = ""

        if ( examList.size == 0 || course.courseId != dataComunication.getCourse()!!.courseId || course.courseId != dataComunication.getTerm()!!.termId){
            examList.clear()
            view.findViewById<ProgressBar>(R.id.exams_progress_bar).visibility = View.VISIBLE
            fetchExamItems(course.courseId, term.termId)
        }


        examAdapter = ExamListAdapter(context, examList)
        recyclerView.adapter = examAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true
        return view
    }

    private fun fetchExamItems(courseId: Int, termId: Int) {
        app.controller.actionHandler(
                AppController.EXAMS,
                ExamCollectionParametersContainer(
                        termId = termId,
                        courseId = courseId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { exams ->
                            examList.addAll(exams.examList)
                            examAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            exams_progress_bar.visibility = View.GONE
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