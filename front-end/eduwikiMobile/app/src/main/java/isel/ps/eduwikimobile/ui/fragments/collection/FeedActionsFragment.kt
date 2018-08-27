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
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.ActionsFeedListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.UserAction
import isel.ps.eduwikimobile.paramsContainer.ActionsFeedParametersContainer
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.action_collection_fragment.*

class FeedActionsFragment : Fragment() {

    lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var actionList: MutableList<UserAction>
    private lateinit var actionsAdapter: ActionsFeedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        actionList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.action_collection_fragment, container, false)
        recyclerView = view.findViewById(R.id.actions_recycler_view)

        if (actionList.size != 0) {
            actionList.clear()
        }

        view.findViewById<ProgressBar>(R.id.actions_progress_bar).visibility = View.VISIBLE
        getFeedActions()

        actionsAdapter = ActionsFeedListAdapter(activity, actionList)
        recyclerView.adapter = actionsAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    private fun getFeedActions() {
        app.controller.actionHandler(
                AppController.FEED_ACTIONS,
                ActionsFeedParametersContainer(
                        app = app,
                        successCb = { actions ->
                            actionList.addAll(
                                    actions.actions.filter { actionsAdapter.supportedEntitiesMap[it.entity_type] != null }
                            )
                            actionsAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            actions_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, Toast.LENGTH_LONG).show() }
                )
        )
    }

}