package isel.ps.eduwikimobile.ui.fragments.single

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.android.volley.TimeoutError
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.ProfileViewPagerAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Programme
import isel.ps.eduwikimobile.domain.single.User
import isel.ps.eduwikimobile.domain.paramsContainer.EntityParametersContainer
import isel.ps.eduwikimobile.domain.paramsContainer.UserProgrammeParametersContainer
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var app: EduWikiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate( R.layout.profile_fragment, container, false)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = ""
        mainActivity.toolbar.subtitle = ""

        val viewPager = view.findViewById<ViewPager>(R.id.profile_view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.profile_tab_layout)

        getUserInfo()

        val fragmentAdapter = ProfileViewPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onPause() {
        app.repository.cancelPendingRequests(app)
        super.onPause()
    }

    private fun getUserInfo() {
        app.controller.actionHandler(
                AppController.USER_PROFILE_INFO,
                EntityParametersContainer<User>(
                        app = app,
                        successCb = { user ->
                            user_profile_name.text = "${user.givenName} ${user.familyName}"
                            user_profile_reputation.text = user.reputation!!.points.toString()
                            user_profile_progressBar.visibility = View.GONE
                            user_profile_name.visibility = View.VISIBLE
                            user_profile_reputation.visibility = View.VISIBLE
                            user_profile_points.visibility = View.VISIBLE
                        },
                        errorCb = { error ->
                            if(error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            }
                            else {
                                user_profile_progressBar.visibility = View.GONE
                                Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                            }
                        }
                )
        )

        app.controller.actionHandler(
                AppController.USER_FOLLOWING_PROGRAMME,
                UserProgrammeParametersContainer(
                        app = app,
                        successCb = { programme ->
                            user_profile_programme.text = programme.shortName
                            user_profile_progressBar.visibility = View.GONE
                            user_profile_programme.visibility = View.VISIBLE
                        },
                        errorCb = { error ->
                            if(error.exception is TimeoutError) {
                                Toast.makeText(app, "Server isn't responding...", LENGTH_LONG).show()
                            }
                            user_profile_progressBar.visibility = View.GONE
                            Toast.makeText(app, "${error.title} ${error.detail}", Toast.LENGTH_LONG).show()
                        }
                )
        )
    }

}