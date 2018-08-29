package isel.ps.eduwikimobile.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.controller.AppController
import isel.ps.eduwikimobile.domain.single.Homework
import isel.ps.eduwikimobile.domain.paramsContainer.ResourceParametersContainer
import isel.ps.eduwikimobile.ui.activities.MainActivity

class HomeworkListAdapter(var context: Context, var list: MutableList<Homework>) : RecyclerView.Adapter<HomeworkListAdapter.ListViewHolder>() {

    var mainActivity: MainActivity = context as MainActivity
    var app: EduWikiApplication = mainActivity.applicationContext as EduWikiApplication

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.homework_item_row, parent, false)
        val newHolder = ListViewHolder(view)

        newHolder.setListItemClickListener(object : ListItemClickListener {
            override fun onClick(view: View, position: Int) {
                mainActivity.navigateToListItem(newHolder.getItem(position), null)
            }
        })
        return newHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ListViewHolder?, position: Int) {
        holder!!.bindView(position)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var homeworkDueDate: TextView
        var homeworkLateDelivery: Switch
        var homeworkMultipleDeliveries: Switch

        private lateinit var listener: ListItemClickListener

        init {
            homeworkDueDate = itemView.findViewById(R.id.to_insert_homework_due_date)
            homeworkLateDelivery = itemView.findViewById(R.id.homework_late_delivey_swich)
            homeworkMultipleDeliveries = itemView.findViewById(R.id.homework_multiple_deliveries_switch)
            itemView.setOnClickListener(this)
        }

        fun setListItemClickListener(listener: ListItemClickListener) {
            this.listener = listener
        }

        fun getItem(position: Int) = list[position]

        fun bindView(position: Int) {
            val item = list[position]
            homeworkDueDate.text = item.dueDate
            homeworkLateDelivery.isChecked = item.lateDelivery
            homeworkMultipleDeliveries.isChecked = item.multipleDeliveries
        }

        override fun onClick(v: View) {
            listener.onClick(v, adapterPosition)
        }
    }

}