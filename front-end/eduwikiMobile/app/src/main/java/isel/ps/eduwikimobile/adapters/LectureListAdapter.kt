package isel.ps.eduwikimobile.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.single.Lecture
import isel.ps.eduwikimobile.ui.activities.MainActivity
import org.w3c.dom.Text
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class LectureListAdapter (var context: Context, var list: MutableList<Lecture>) : RecyclerView.Adapter<LectureListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.lecture_item_row, parent, false)
        val newHolder = ListViewHolder(view)

        newHolder.setListItemClickListener(object : ListItemClickListener {
            override fun onClick(view: View, position: Int) {
                val mainActivity = context as MainActivity
                mainActivity.navigateToListItem(newHolder.getItem(position))
            }
        })
        return newHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ListViewHolder?, position: Int) {
        holder!!.bindView(position)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var weekDay: TextView

        private lateinit var listener: ListItemClickListener

        init {
            weekDay = itemView.findViewById(R.id.to_insert_lecture_weekday)
            itemView.setOnClickListener(this)
        }

        fun setListItemClickListener(listener: ListItemClickListener) {
            this.listener = listener
        }

        fun getItem(position: Int) = list[position]

        fun bindView(position: Int) {
            val item = list[position]
            val hours = Duration.parse(item.duration).toHours()
            val minutes = Duration.parse(item.duration).minusHours(hours).toMinutes()
            weekDay.text = item.weekDay
        }

        override fun onClick(v: View) {
            listener.onClick(v, adapterPosition)
        }

    }

}