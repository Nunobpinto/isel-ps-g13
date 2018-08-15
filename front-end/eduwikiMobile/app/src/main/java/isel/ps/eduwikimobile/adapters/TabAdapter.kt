package isel.ps.eduwikimobile.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import isel.ps.eduwikimobile.ui.fragments.CoursesOfProgrammeFragment
import isel.ps.eduwikimobile.ui.fragments.HomeFragment
import isel.ps.eduwikimobile.ui.fragments.ProgrammeInfoTabFragment

class TabAdapter(fragManager: FragmentManager) : FragmentStatePagerAdapter(fragManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProgrammeInfoTabFragment()
            1 -> CoursesOfProgrammeFragment()
            else -> {
                return HomeFragment()
            }
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Info"
            else -> return "Courses"
        }
    }
}