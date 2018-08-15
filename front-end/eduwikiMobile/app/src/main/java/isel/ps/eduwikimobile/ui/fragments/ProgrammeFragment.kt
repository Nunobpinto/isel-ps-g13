package isel.ps.eduwikimobile.ui.fragments

import android.app.ActionBar
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.TabAdapter
import isel.ps.eduwikimobile.domain.model.single.Programme
import isel.ps.eduwikimobile.ui.activities.MainActivity

class ProgrammeFragment : Fragment(){

    lateinit var app: EduWikiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.programme_details_fragment, container, false)

        val bundle: Bundle = arguments
        val programme = bundle.getParcelable<Programme>("item_selected")
        app.store = programme
        app.programmeId = programme.programmeId

        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        val programmeName = view.findViewById<TextView>(R.id.programme_full_name)

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = programme.shortName
        mainActivity.toolbar.subtitle = programme.createdBy
        programmeName.text = programme.fullName

        val fragmentAdapter = TabAdapter(fragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view

    }

}