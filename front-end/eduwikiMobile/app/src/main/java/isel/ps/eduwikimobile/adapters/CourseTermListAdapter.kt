package isel.ps.eduwikimobile.adapters

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.single.Term
import isel.ps.eduwikimobile.ui.activities.MainActivity
import isel.ps.eduwikimobile.ui.fragments.collection.ClassCollectionOfSpecificCourseFragment
import isel.ps.eduwikimobile.ui.fragments.collection.ExamCollectionFragment
import isel.ps.eduwikimobile.ui.fragments.collection.WorkAssignmentCollectionFragment

class CourseTermListAdapter (var context: Context, var list: MutableList<Term>) : RecyclerView.Adapter<CourseTermListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.course_details_item_row, parent, false)
        val newHolder = ListViewHolder(view)
        val examsButton = view.findViewById<Button>(R.id.course_term_exams)
        val workAssignmentsButton = view.findViewById<Button>(R.id.course_term_work_assignments)
        val classesButtons = view.findViewById<Button>(R.id.course_term_classes)

        examsButton.setOnClickListener {
            val fragment = ExamCollectionFragment()
            val bundle = Bundle()
            bundle.putParcelable("actualTerm", newHolder.term)
            fragment.arguments = bundle
            val activity = context as MainActivity
            activity.loadFragment(fragment)
        }

        workAssignmentsButton.setOnClickListener {
            val fragment = WorkAssignmentCollectionFragment()
            val bundle = Bundle()
            bundle.putParcelable("actualTerm", newHolder.term)
            fragment.arguments = bundle
            val activity = context as MainActivity
            activity.loadFragment(fragment)
        }

        classesButtons.setOnClickListener {
            val fragment = ClassCollectionOfSpecificCourseFragment()
            val bundle = Bundle()
            bundle.putParcelable("actualTerm", newHolder.term)
            fragment.arguments = bundle
            val activity = context as MainActivity
            activity.loadFragment(fragment)
        }

        return newHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ListViewHolder?, position: Int) {
        holder!!.bindView(position)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var termShortName: TextView
        lateinit var term: Term

        init {
            termShortName = itemView.findViewById(R.id.term_short_name)
        }

        fun bindView(position: Int) {
            termShortName.text = list[position].shortName
            term = list[position]
        }
    }

}