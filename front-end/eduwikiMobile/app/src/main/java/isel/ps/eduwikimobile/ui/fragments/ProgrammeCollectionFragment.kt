package isel.ps.eduwikimobile.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.paramsContainer.ProgrammeCollectionParametersContainer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import isel.ps.eduwikimobile.adapters.ProgrammeListAdapter
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.programme_collection_fragment.*

class ProgrammeCollectionFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var programmeList: MutableList<Programme>
    private lateinit var pAdapter: ProgrammeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        programmeList = ArrayList()
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.toolbar.title = "Programmes"
        activity.toolbar.subtitle = ""
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.programme_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.programmes_recycler_view)
        fetchProgrammeItems()

        pAdapter = ProgrammeListAdapter(context, programmeList)
        recyclerView.adapter = pAdapter
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun fetchProgrammeItems() {
        AppController.actionHandler(
                AppController.ALL_PROGRAMMES,
                ProgrammeCollectionParametersContainer(
                        app = activity.applicationContext as EduWikiApplication,
                        successCb = { programmes ->
                            programmeList.addAll(programmes.programmeList)
                            pAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE;
                            programmes_progress_bar.visibility = View.GONE;
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )
    }

}

