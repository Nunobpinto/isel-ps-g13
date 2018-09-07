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
import isel.ps.eduwikimobile.adapters.ClassListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.paramsContainer.ClassCollectionParametersContainer
import isel.ps.eduwikimobile.domain.single.*
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.class_collection_fragment.*

class ClassCollectionOfSpecificCourseFragment : Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var classesOfSpecificCourseList: MutableList<Class>
    private lateinit var classAdapter: ClassListAdapter
    private lateinit var dataComunication: IDataComunication
    private var course: Course? = null
    private var courseProgramme: CourseProgramme? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        classesOfSpecificCourseList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.class_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.classes_recycler_view)
        val activity = activity as MainActivity
        val bundle: Bundle = arguments
        val term: Term = bundle.getParcelable("actualTerm")
        dataComunication.setTerm(term)

        if (classesOfSpecificCourseList.size != 0) {
            classesOfSpecificCourseList.clear()
        }
        view.findViewById<ProgressBar>(R.id.classes_progress_bar).visibility = View.VISIBLE

        if (dataComunication.getCourse() != null) {
            course = dataComunication.getCourse()
            getClassesOfCourse(course!!.courseId, term.termId)
            activity.toolbar.title = course!!.shortName + "/" + term.shortName + "/" + "Classes"
            activity.toolbar.subtitle = course!!.createdBy
        } else {
            courseProgramme = dataComunication.getCourseProgramme()
            getClassesOfCourse(courseProgramme!!.courseId, term.termId)
            activity.toolbar.title = courseProgramme!!.shortName + "/" + term.shortName + "/" + "Classes"
            activity.toolbar.subtitle = courseProgramme!!.createdBy
        }

        classAdapter = ClassListAdapter(context, classesOfSpecificCourseList)
        recyclerView.adapter = classAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true
        return view
    }

    private fun getClassesOfCourse(courseId: Int, termId: Int) {
        app.controller.actionHandler(
                AppController.ALL_CLASSES_OF_SPECIFIC_COURSE,
                ClassCollectionParametersContainer(
                        termId = termId,
                        courseId = courseId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { classes ->
                            classesOfSpecificCourseList.addAll(classes.classList)
                            classAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            classes_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if (error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                            } else {
                                classes_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
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