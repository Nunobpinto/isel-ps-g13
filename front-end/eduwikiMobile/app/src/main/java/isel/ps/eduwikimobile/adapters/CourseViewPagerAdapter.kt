package isel.ps.eduwikimobile.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import isel.ps.eduwikimobile.ui.fragments.*

class CourseViewPagerAdapter(fragManager: FragmentManager) : FragmentStatePagerAdapter(fragManager) {

    private val NUM_OF_ITEMS = 3

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> ExamCollectionFragment()
                1 -> WorkAssignmentCollectionFragment()
                else -> ClassesOfSpecificCourseFragment()
            }

    override fun getCount(): Int = NUM_OF_ITEMS

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Exams"
            1 -> "Work Assignments"
            else -> "Classes"
        }
    }
}