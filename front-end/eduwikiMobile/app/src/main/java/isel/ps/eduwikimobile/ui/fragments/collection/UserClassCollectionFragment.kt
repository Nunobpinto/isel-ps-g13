package isel.ps.eduwikimobile.ui.fragments.collection

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
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseClassListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.CourseClass
import isel.ps.eduwikimobile.domain.paramsContainer.CourseClassCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import kotlinx.android.synthetic.main.class_collection_fragment.*

class UserClassCollectionFragment : Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var classList: MutableList<CourseClass>
    private lateinit var classAdapter: CourseClassListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        classList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.class_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.classes_recycler_view)

        if (classList.size != 0) {
            classList.clear()
        }

        view.findViewById<ProgressBar>(R.id.classes_progress_bar).visibility = View.VISIBLE
        getUserClassItems()

        classAdapter = CourseClassListAdapter(context, classList)
        recyclerView.adapter = classAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getUserClassItems() {
        app.controller.actionHandler(
                AppController.USER_FOLLOWING_CLASSES,
                CourseClassCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { classes ->
                            classList.addAll(classes.courseClassList)
                            classAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            classes_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if(error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            }
                            else {
                                classes_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )
    }

}