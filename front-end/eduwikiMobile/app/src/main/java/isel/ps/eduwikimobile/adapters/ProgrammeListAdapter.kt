package isel.ps.eduwikimobile.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.single.Programme
import isel.ps.eduwikimobile.ui.activities.MainActivity

class ProgrammeListAdapter(var context: Context, var list: MutableList<Programme>) : RecyclerView.Adapter<ProgrammeListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.programme_item_row, parent, false)
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

        var programmeShortName: TextView

        private lateinit var listener: ListItemClickListener

        init {
            programmeShortName = itemView.findViewById(R.id.programme_short_name)
            itemView.setOnClickListener(this)
        }

        fun setListItemClickListener (listener: ListItemClickListener) {
            this.listener = listener
        }

        fun getItem(position: Int) = list[position]

        fun bindView(position: Int) {
            programmeShortName.text = list[position].shortName
        }

        override fun onClick(v: View) {
            listener.onClick(v, adapterPosition)
        }

    }

}