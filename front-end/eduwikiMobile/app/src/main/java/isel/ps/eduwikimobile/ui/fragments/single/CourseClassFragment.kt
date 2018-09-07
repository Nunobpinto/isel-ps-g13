package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.adapters.CourseClassViewPagerAdapter
import isel.ps.eduwikimobile.domain.single.CourseClass
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity

class CourseClassFragment : Fragment() {

    lateinit var dataComunication: IDataComunication

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.course_class_details_fragment, container, false)

        val bundle: Bundle = arguments
        val courseClass = bundle.getParcelable<CourseClass>("item_selected")
        dataComunication.setCourseClass(courseClass)

        val viewPager = view.findViewById<ViewPager>(R.id.course_class_view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.course_class_tab_layout)

        val courseClassName = view.findViewById<TextView>(R.id.course_class_name)

        val mainActivity = context as MainActivity
        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = courseClass.lecturedTerm + "/" + courseClass.className + "/" + courseClass.courseShortName
        mainActivity.toolbar.subtitle = courseClass.createdBy

        courseClassName.text = courseClass.courseFullName

        val fragmentAdapter = CourseClassViewPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            dataComunication = context as IDataComunication
        }
        catch (e: ClassCastException) {
            throw ClassCastException(e.message)
        }
    }
}