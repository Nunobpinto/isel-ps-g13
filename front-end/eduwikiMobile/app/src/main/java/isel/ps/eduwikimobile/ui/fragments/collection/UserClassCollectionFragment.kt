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
import isel.ps.eduwikimobile.adapters.ClassListAdapter
import isel.ps.eduwikimobile.adapters.CourseClassListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.Class
import isel.ps.eduwikimobile.domain.model.single.CourseClass
import isel.ps.eduwikimobile.paramsContainer.ClassCollectionParametersContainer
import isel.ps.eduwikimobile.paramsContainer.CourseClassCollectionParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.class_collection_fragment.*

class UserClassCollectionFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var classList: MutableList<CourseClass>
    private lateinit var classAdapter: CourseClassListAdapter
    lateinit var dataComunication: IDataComunication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        classList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.class_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.classes_recycler_view)

        if (classList.size == 0) {
            view.findViewById<ProgressBar>(R.id.classes_progress_bar).visibility = View.VISIBLE
            fetchUserClassItems()
        }

        classAdapter = CourseClassListAdapter(context, classList)
        recyclerView.adapter = classAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun fetchUserClassItems() {
        app.controller.actionHandler(
                AppController.USER_FOLLOWING_CLASSES,
                CourseClassCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { classes ->
                            classList.addAll(classes.courseClassList)
                            classAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            classes_progress_bar.visibility = View.GONE;
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