package isel.ps.eduwikimobile.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import isel.ps.eduwikimobile.ui.fragments.collection.UserClassCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.UserCourseCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.single.OrganizationFragment

class ProfileViewPagerAdapter (fragManager: FragmentManager) : FragmentStatePagerAdapter(fragManager) {

    private val NUM_OF_ITEMS = 3

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> UserCourseCollectionFragment()
                1 -> UserClassCollectionFragment()
                else -> OrganizationFragment()
            }

    override fun getCount(): Int = NUM_OF_ITEMS

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "My Courses"
            1 -> "My Classes"
            else -> "My Organization"
        }
    }
}