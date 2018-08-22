package isel.ps.eduwikimobile.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.model.single.Class
import isel.ps.eduwikimobile.ui.activities.MainActivity

class ClassFragment : Fragment() {

    lateinit var app: EduWikiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.applicationContext as EduWikiApplication
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.class_details_fragment, container, false)

        val bundle: Bundle = arguments
        val klass = bundle.getParcelable<Class>("item_selected")
        val klassName = view.findViewById<TextView>(R.id.class_short_name)

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = klass.className
        mainActivity.toolbar.subtitle = klass.createdBy

        klassName.text = klass.className

        /* val fragmentAdapter = ProgrammeViewPagerAdapter(fragmentManager)
         viewPager.adapter = fragmentAdapter
         tabLayout.setupWithViewPager(viewPager)*/

        return view
    }
}