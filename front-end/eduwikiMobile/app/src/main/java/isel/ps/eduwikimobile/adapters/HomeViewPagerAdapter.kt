package isel.ps.eduwikimobile.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import isel.ps.eduwikimobile.ui.fragments.collection.FeedActionsFragment
import isel.ps.eduwikimobile.ui.fragments.collection.FollowingFragment

class HomeViewPagerAdapter (fragManager: FragmentManager) : FragmentStatePagerAdapter(fragManager) {

    private val NUM_OF_ITEMS = 2

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> FeedActionsFragment()
                else -> FollowingFragment()
            }

    override fun getCount(): Int = NUM_OF_ITEMS

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Feed"
            else -> "Following"
        }
    }

}