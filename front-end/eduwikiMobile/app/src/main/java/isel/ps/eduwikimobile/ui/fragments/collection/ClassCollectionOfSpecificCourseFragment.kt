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
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.ClassListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.Class
import isel.ps.eduwikimobile.domain.model.single.Course
import isel.ps.eduwikimobile.domain.model.single.Term
import isel.ps.eduwikimobile.paramsContainer.CourseClassCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.class_collection_fragment.*

class ClassCollectionOfSpecificCourseFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var classesOfSpecificCourseList: MutableList<Class>
    private lateinit var classAdapter: ClassListAdapter
    lateinit var dataComunication: IDataComunication
    lateinit var course: Course


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        classesOfSpecificCourseList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.class_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.classes_recycler_view)

        val bundle: Bundle = arguments
        val term: Term = bundle.getParcelable("actualTerm")
        dataComunication.setTerm(term)
        course = dataComunication.getCourse()!!

        view.findViewById<ProgressBar>(R.id.classes_progress_bar).visibility = View.VISIBLE
        fetchClassesOfCourse(course.courseId, term.termId)

        val activity = activity as MainActivity
        activity.toolbar.title = course.shortName + "/" + term.shortName + "/" + "Classes"
        activity.toolbar.subtitle = ""

        classAdapter = ClassListAdapter(context, classesOfSpecificCourseList)
        recyclerView.adapter = classAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true
        return view
    }


    private fun fetchClassesOfCourse(courseId: Int, termId: Int) {
        AppController.actionHandler(
                AppController.ALL_CLASSES_OF_SPECIFIC_COURSE,
                CourseClassCollectionParametersContainer(
                        termId = termId,
                        courseId = courseId,
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { classes ->
                            classesOfSpecificCourseList.addAll(classes.classList)
                            classAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            classes_progress_bar.visibility = View.GONE;
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, Toast.LENGTH_LONG).show() }
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