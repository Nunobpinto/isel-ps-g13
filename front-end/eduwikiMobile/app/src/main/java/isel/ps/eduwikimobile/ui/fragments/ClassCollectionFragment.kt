package isel.ps.eduwikimobile.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.ClassListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.Class
import isel.ps.eduwikimobile.paramsContainer.ClassCollectionParametersContainer
import kotlinx.android.synthetic.main.classes_fragment.*

class ClassCollectionFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var classList: MutableList<Class>
    private lateinit var classAdapter: ClassListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        classList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.classes_fragment, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)

        fetchClassItems()

        classAdapter = ClassListAdapter(context, classList)
        recyclerView.adapter = classAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun fetchClassItems() {
        AppController.actionHandler(
                AppController.ALL_CLASSES,
                ClassCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { classes ->
                            classList.addAll(classes.classList)
                            classAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            classes_progress_bar.visibility = View.GONE;
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
    }

}