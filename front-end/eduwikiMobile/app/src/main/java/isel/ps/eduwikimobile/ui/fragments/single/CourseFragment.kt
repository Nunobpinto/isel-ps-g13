package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseTermListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.single.Term
import isel.ps.eduwikimobile.domain.paramsContainer.TermCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class CourseFragment : Fragment() {

    lateinit var app: EduWikiApplication
    lateinit var dataComunication: IDataComunication
    lateinit var termList: MutableList<Term>
    lateinit var course: Course
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseTermsAdapter: CourseTermListAdapter
    private lateinit var map: HashMap<Int, MutableList<Term>>
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        termList = ArrayList()
        map = HashMap()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.course_details_fragment, container, false)
        recyclerView = view.findViewById(R.id.course_recycler_view)

        val courseName = view.findViewById<TextView>(R.id.course_full_name)

        val bundle: Bundle = arguments
        course = bundle.getParcelable("item_selected")

        if (map[course.courseId] == null) {
            termList.clear()
            dataComunication.setCourse(course)
            progressBar = view.findViewById(R.id.course_progress_bar)
            progressBar.visibility = View.VISIBLE
            getCourseTerms(course.courseId)
        } else if (course.courseId != dataComunication.getCourse()!!.courseId) {
            dataComunication.setCourse(course)
            termList = map[course.courseId]!!
        }

        courseTermsAdapter = CourseTermListAdapter(context, termList)
        recyclerView.adapter = courseTermsAdapter

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        mainActivity.toolbar.title = course.shortName
        mainActivity.toolbar.subtitle = course.createdBy

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        courseName.text = course.fullName
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

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getCourseTerms(courseId: Int) {
        app.controller.actionHandler(
                AppController.TERMS_OF_COURSE,
                TermCollectionParametersContainer(
                        courseId = courseId,
                        app = app,
                        successCb = { terms ->
                            termList.addAll(terms.termList)
                            map[courseId] = terms.termList.toMutableList()
                            courseTermsAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if(error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            }
                            else{
                                progressBar.visibility = View.GONE
                                Toast.makeText(app, "Error", LENGTH_LONG).show()
                            }
                        }
                )
        )
    }
}