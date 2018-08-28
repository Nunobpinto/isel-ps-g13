package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.ProfileViewPagerAdapter
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.domain.model.single.User
import isel.ps.eduwikimobile.paramsContainer.EntityParametersContainer
import isel.ps.eduwikimobile.paramsContainer.UserProgrammeParametersContainer
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    lateinit var dataComunication: IDataComunication
    lateinit var programme: Programme
    lateinit var user: User
    lateinit var mainActivity: MainActivity
    lateinit var app: EduWikiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate( R.layout.profile_fragment, container, false)

        val viewPager = view.findViewById<ViewPager>(R.id.profile_view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.profile_tab_layout)

        getUserInfo()

        val fragmentAdapter = ProfileViewPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    private fun getUserInfo() {
        app.controller.actionHandler(
                AppController.USER_PROFILE_INFO,
                EntityParametersContainer<User>(
                        app = app,
                        successCb = { user ->
                            user_profile_name.text = "${user.givenName} ${user.familyName}"
                            user_profile_reputation.text = user.reputation!!.points.toString()
                            mainActivity.toolbar.title = user.username
                            user_profile_progressBar.visibility = View.GONE
                            user_profile_name.visibility = View.VISIBLE
                            user_profile_reputation.visibility = View.VISIBLE
                        },
                        errorCb = { error -> Toast.makeText(app, "Error" + error.message, LENGTH_LONG).show() }
                )
        )

        app.controller.actionHandler(
                AppController.USER_FOLLOWING_PROGRAMME,
                EntityParametersContainer<Programme>(
                        app = app,
                        successCb = { programme ->
                            user_profile_programme.text = programme.shortName
                            user_profile_progressBar.visibility = View.GONE
                            user_profile_programme.visibility = View.VISIBLE
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