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
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.FollowingListAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.paramsContainer.CourseClassCollectionParametersContainer
import isel.ps.eduwikimobile.domain.paramsContainer.CourseCollectionParametersContainer
import isel.ps.eduwikimobile.domain.paramsContainer.UserProgrammeParametersContainer
import kotlinx.android.synthetic.main.following_fragment.*

class FollowingFragment : Fragment() {

    private lateinit var app: EduWikiApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var followingList: MutableList<Any>
    private lateinit var followingAdapter: FollowingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
        followingList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.following_fragment, container, false)
        recyclerView = view.findViewById(R.id.following_recycler_view)

        if (followingList.size != 0) {
            followingList.clear()
        }

        view.findViewById<ProgressBar>(R.id.following_progress_bar).visibility = View.VISIBLE
        getUserFollowingItems()

        followingAdapter = FollowingListAdapter(context, followingList)
        recyclerView.adapter = followingAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = true

        return view
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getUserFollowingItems() {
        //getting following classes
        app.controller.actionHandler(
                AppController.USER_FOLLOWING_CLASSES,
                CourseClassCollectionParametersContainer(
                        app = app,
                        successCb = { courseClassList ->
                            followingList.addAll(courseClassList.courseClassList)
                            followingAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            following_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if (error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                            } else {
                                following_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )
        //getting following courses
        app.controller.actionHandler(
                AppController.USER_FOLLOWING_COURSES,
                CourseCollectionParametersContainer(
                        app = app,
                        successCb = { course ->
                            followingList.addAll(course.courseList)
                            followingAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            following_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if (error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                            } else {
                                following_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )
        //getting following programme
        app.controller.actionHandler(
                AppController.USER_FOLLOWING_PROGRAMME,
                UserProgrammeParametersContainer(
                        app = app,
                        successCb = { programme ->
                            followingList.addAll(listOf(programme))
                            followingAdapter.notifyDataSetChanged()
                            recyclerView.visibility = View.VISIBLE
                            following_progress_bar.visibility = View.GONE
                        },
                        errorCb = { error ->
                            if (error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", Toast.LENGTH_LONG).show()
                            } else {
                                following_progress_bar.visibility = View.GONE
                                Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )
    }

}