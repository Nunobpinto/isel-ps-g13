package isel.ps.eduwikimobile.ui.fragments.single

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.HomeViewPagerAdapter
import isel.ps.eduwikimobile.ui.activities.MainActivity


class HomeFragment : Fragment(){

    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.home_fragment, container, false)
        mainActivity.toolbar.title = "Home"
        mainActivity.toolbar.subtitle = ""

        val viewPager = view.findViewById<ViewPager>(R.id.home_view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.home_tab_layout)

        val fragmentAdapter = HomeViewPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

}