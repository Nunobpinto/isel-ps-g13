package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseProgrammeListAdapter
import isel.ps.eduwikimobile.adapters.CourseTermListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.paramsContainer.TermCollectionParametersContainer
import isel.ps.eduwikimobile.domain.single.Course
import isel.ps.eduwikimobile.domain.single.CourseProgramme
import isel.ps.eduwikimobile.domain.single.Term
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.course_programme_details_fragment.*

class CourseProgrammeFragment : Fragment() {

    lateinit var app: EduWikiApplication
    lateinit var dataComunication: IDataComunication
    lateinit var termList: MutableList<Term>
    lateinit var courseProgramme: CourseProgramme
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseTermsAdapter: CourseTermListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        termList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.course_programme_details_fragment, container, false)
        val courseName = view.findViewById<TextView>(R.id.course_programme_full_name)

        recyclerView = view.findViewById(R.id.course_programme_recycler_view)

        val bundle: Bundle = arguments
        courseProgramme = bundle.getParcelable("item_selected")
        dataComunication.setCourseProgramme(courseProgramme)
        view.findViewById<TextView>(R.id.course_programme_to_insert_optional).text = if(courseProgramme.optional) "Yes" else "No"
        view.findViewById<TextView>(R.id.course_programme_to_insert_term).text = courseProgramme.lecturedTerm
        view.findViewById<TextView>(R.id.course_programme_to_insert_credits).text = courseProgramme.credits.toString()

        if (termList.size != 0) {
            termList.clear()
        }

        view.findViewById<ProgressBar>(R.id.course_programme_progress_bar).visibility = View.VISIBLE
        getCourseProgrammeTerms(courseProgramme.courseId)

        courseTermsAdapter = CourseTermListAdapter(context, termList)
        recyclerView.adapter = courseTermsAdapter

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE

        mainActivity.toolbar.title = "${courseProgramme.programmeShortName}/${courseProgramme.shortName}"
        mainActivity.toolbar.subtitle = courseProgramme.createdBy

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        courseName.text = courseProgramme.fullName
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

    private fun getCourseProgrammeTerms(courseId: Int) {
        app.controller.actionHandler(
                AppController.TERMS_OF_COURSE,
                TermCollectionParametersContainer(
                        courseId = courseId,
                        app = app,
                        successCb = { terms ->
                            termList.addAll(terms.termList)
                            courseTermsAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            course_programme_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if(error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                            }
                            else {
                                course_programme_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "Error", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )
    }

}