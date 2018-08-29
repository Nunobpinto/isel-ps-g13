package isel.ps.eduwikimobile.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import isel.ps.eduwikimobile.API_URL
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.model.single.*
import isel.ps.eduwikimobile.paramsContainer.CourseClassCollectionParametersContainer
import isel.ps.eduwikimobile.paramsContainer.CourseProgrammeCollectionParametersContainer
import isel.ps.eduwikimobile.paramsContainer.EntityParametersContainer
import isel.ps.eduwikimobile.paramsContainer.ParametersContainer
import isel.ps.eduwikimobile.ui.activities.MainActivity
import isel.ps.eduwikimobile.ui.fragments.single.CourseClassFragment

class ActionsFeedListAdapter(var context: Context, var list: MutableList<UserAction>) : RecyclerView.Adapter<ActionsFeedListAdapter.ListViewHolder>() {

    val mainActivity: MainActivity = context as MainActivity
    val app: EduWikiApplication = context.applicationContext as EduWikiApplication
    var userActionsMap: HashMap<String, String> =
            hashMapOf(
                    "CREATE" to "created",
                    "ALTER" to "altered",
                    "DELETE" to "deleted",
                    "VOTE_UP" to "upvoted",
                    "VOTE_DOWN" to "downvoted",
                    "APPROVE_REPORT" to "approved",
                    "APPROVE_STAGE" to "approved",
                    "REJECT_REPORT" to "rejected",
                    "REJECT_STAGE" to "rejected"
            )

    var supportedEntitiesMap: HashMap<String, String> =
            hashMapOf(
                    "course_class" to "Course in Class",
                    "course_class_stage" to "Staged Course in Class",
                    "course_class_report" to "Reported Course in Class",
                    "course_programme" to "Course in Programme",
                    "course_programme_stage" to "Staged Course in Programme",
                    "course_programme_report" to "Reported Course in Programme",
                    "class" to "Class",
                    "class_stage" to "Staged Class",
                    "class_report" to "Reported Class",
                    "course" to "Course",
                    "course_stage" to "Staged Course",
                    "course_report" to "Reported Course",
                    "programme" to "Programme",
                    "programme_stage" to "Staged Programme",
                    "programme_report" to "Reported Programme",
                    "exam" to "Exam",
                    "exam_stage" to "Staged Exam",
                    "exam_report" to "Reported Exam",
                    "homework" to "Homework",
                    "homework_stage" to "Staged Homework",
                    "homework_report" to "Reported Homework",
                    "work_assignment" to "WorkAssignment",
                    "work_assignment_stage" to "Staged Work Assignment",
                    "work_assignment_report" to "Reported Work Assignment",
                    "lecture" to "Lecture",
                    "lecture_stage" to "Staged Work Lecture",
                    "lecture_report" to "Reported Work Lecture"
            )

    private fun getType(type: String) =
            when (type) {
                "course_class" -> CourseClass::class.java
                "course_programme", "programme" -> Course::class.java
                "class" -> Class::class.java
                "course" -> Course::class.java
                "exam" -> Exam::class.java
                "homework" -> Homework::class.java
                "work_assignment" -> WorkAssignment::class.java
                "lecture" -> Lecture::class.java
                else -> null
            }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.action_item_row, parent, false)
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

        var timestamp: TextView
        var actionType: TextView
        var entity: TextView
        var createdBy: TextView
        var resource: Button

        private lateinit var listener: ListItemClickListener

        init {
            timestamp = itemView.findViewById(R.id.action_timestamp)
            actionType = itemView.findViewById(R.id.action_type)
            entity = itemView.findViewById(R.id.action_entity)
            createdBy = itemView.findViewById(R.id.action_created_by)
            resource = itemView.findViewById(R.id.action_resource)
            itemView.setOnClickListener(this)
        }

        fun setListItemClickListener(listener: ListItemClickListener) {
            this.listener = listener
        }

        fun getItem(position: Int) = list[position]

        fun bindView(position: Int) {
            val item = list[position]
            timestamp.text = item.timestamp
            actionType.text = userActionsMap[item.action_type]
            entity.text = supportedEntitiesMap[item.entity_type]
            createdBy.text = item.action_user
            if (getType(item.entity_type) != null) {
                resource.visibility = View.VISIBLE
                resource.setOnClickListener {
                    app.repository.getEntity(
                            "$API_URL/${item.entity_link}",
                            getType(item.entity_type)!!,
                            EntityParametersContainer(
                                    app = app,
                                    successCb = { entity ->
                                        mainActivity.navigateToListItem(entity, null)
                                    },
                                    errorCb = { error -> Toast.makeText(app, "Error" + error.message, Toast.LENGTH_LONG).show() }
                            )
                    )
                }
            }
        }

        override fun onClick(v: View) {
            listener.onClick(v, adapterPosition)
        }

    }

}
