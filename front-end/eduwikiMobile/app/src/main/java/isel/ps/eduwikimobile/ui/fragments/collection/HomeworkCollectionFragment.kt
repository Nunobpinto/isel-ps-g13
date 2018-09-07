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
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.HomeworkListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Homework
import isel.ps.eduwikimobile.domain.paramsContainer.HomeworkCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import kotlinx.android.synthetic.main.homework_collection_fragment.*

class HomeworkCollectionFragment: Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeworkList: MutableList<Homework>
    private lateinit var homeworkAdapter: HomeworkListAdapter
    private lateinit var dataComunication: IDataComunication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        homeworkList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.homework_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.homeworks_recycler_view)

        val courseClass = dataComunication.getCourseClass()

        if (homeworkList.size != 0) {
            homeworkList.clear()
        }
        view.findViewById<ProgressBar>(R.id.homeworks_progress_bar).visibility = View.VISIBLE
        getHomeworksOfCourseClass(courseClass!!.courseId, courseClass.classId)

        homeworkAdapter = HomeworkListAdapter(context, homeworkList)
        recyclerView.adapter = homeworkAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getHomeworksOfCourseClass(courseId: Int, classId: Int) {
        app.controller.actionHandler(
                AppController.ALL_HOMEWORKS_OF_COURSE_CLASS,
                HomeworkCollectionParametersContainer(
                        courseId = courseId,
                        classId = classId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { homeworks ->
                            homeworkList.addAll(homeworks.homeworkList)
                            homeworkAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            homeworks_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if(error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                            }
                            else {
                                homeworks_progress_bar.visibility = View.GONE
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