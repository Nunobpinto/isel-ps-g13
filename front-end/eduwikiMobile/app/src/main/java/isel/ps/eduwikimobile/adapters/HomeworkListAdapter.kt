package isel.ps.eduwikimobile.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.model.single.Homework
import isel.ps.eduwikimobile.ui.activities.MainActivity

class HomeworkListAdapter (var context: Context, var list: MutableList<Homework>) : RecyclerView.Adapter<HomeworkListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.homework_item_row, parent, false)
        val newHolder = ListViewHolder(view)

        newHolder.setListItemClickListener(object : ListItemClickListener {
            override fun onClick(view: View, position: Int) {
                val mainActivity = context as MainActivity
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
        var homeworkLateDelivery: CheckBox
        var homeworkMultipleDeliveries: CheckBox
        var homeworkSheet: Button

        private lateinit var listener: ListItemClickListener

        init {
            homeworkDueDate = itemView.findViewById(R.id.to_insert_homework_due_date)
            homeworkLateDelivery = itemView.findViewById(R.id.homework_late_delivey_checkbox)
            homeworkMultipleDeliveries = itemView.findViewById(R.id.homework_multiple_deliveries_checkbox)
            homeworkSheet = itemView.findViewById(R.id.homework_sheet)
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