package isel.ps.eduwikimobile.ui.fragments.single

import android.app.ActionBar
import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.ProgrammeViewPagerAdapter
import isel.ps.eduwikimobile.domain.single.Programme
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class ProgrammeFragment : Fragment() {

    private lateinit var programme: Programme
    private lateinit var mainActivity: MainActivity
    private lateinit var dataComunication: IDataComunication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.programme_details_fragment, container, false)
        val bundle: Bundle = arguments
        programme = bundle.getParcelable("item_selected")
        dataComunication.setProgramme(programme)

        val viewPager = view.findViewById<ViewPager>(R.id.programme_view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.programme_tab_layout)

        val programmeName = view.findViewById<TextView>(R.id.programme_full_name)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = programme.shortName
        mainActivity.toolbar.subtitle = programme.createdBy

        programmeName.text = programme.fullName

        val fragmentAdapter = ProgrammeViewPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view
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