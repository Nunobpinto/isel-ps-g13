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
import isel.ps.eduwikimobile.adapters.ClassListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Class
import isel.ps.eduwikimobile.domain.paramsContainer.ClassCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.class_collection_fragment.*
import java.util.concurrent.TimeoutException

class ClassCollectionFragment : Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var classList: MutableList<Class>
    private lateinit var classAdapter: ClassListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        classList = ArrayList()
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.toolbar.title = "Classes"
        activity.toolbar.subtitle = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.class_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.classes_recycler_view)

        if (classList.size != 0) {
            classList.clear()
        }
        view.findViewById<ProgressBar>(R.id.classes_progress_bar).visibility = View.VISIBLE
        getClassItems()

        classAdapter = ClassListAdapter(context, classList)
        recyclerView.adapter = classAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun getClassItems() {
        app.controller.actionHandler(
                AppController.ALL_CLASSES,
                ClassCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { classes ->
                            classList.addAll(classes.classList)
                            classAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            classes_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if (error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            }
                            classes_progress_bar.visibility = View.GONE;
                            Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                        }
                )
        )
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

}