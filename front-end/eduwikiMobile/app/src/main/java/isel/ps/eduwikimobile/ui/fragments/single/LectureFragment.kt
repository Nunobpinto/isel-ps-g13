package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.single.Lecture
import isel.ps.eduwikimobile.ui.IDataComunication
import isel.ps.eduwikimobile.ui.activities.MainActivity
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LectureFragment : Fragment() {

    lateinit var dataComunication: IDataComunication
    lateinit var lecture: Lecture
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.lecture_details_fragment, container, false)
        val bundle: Bundle = arguments
        lecture = bundle.getParcelable("item_selected")
        dataComunication.setLecture(lecture)

        val lectureName = view.findViewById<TextView>(R.id.lecture_details_name)
        val lectureBegins = view.findViewById<TextView>(R.id.insert_lecture_begins)
        val lectureDuration = view.findViewById<TextView>(R.id.insert_lecture_duration)
        val lectureLocation = view.findViewById<TextView>(R.id.insert_lecture_location)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        mainActivity.toolbar.title = "${lecture.lecturedTerm}/${lecture.className}/${lecture.courseShortName}/Lecture"
        mainActivity.toolbar.subtitle = lecture.createdBy

        lectureName.text = "${lecture.weekDay} Lecture"
        val durationHours = Duration.parse(lecture.duration).toHours()
        val durationMinutes = Duration.parse(lecture.duration).minusHours(durationHours).toMinutes()
        var beginsHours = LocalTime.parse(lecture.begins, DateTimeFormatter.ISO_TIME).hour
        val beginsMinutes = LocalTime.parse(lecture.begins, DateTimeFormatter.ISO_TIME).minute
        lectureLocation.text = lecture.location

        if (beginsMinutes < 9)
            lectureBegins.text = "${beginsHours}h:${LocalTime.parse(lecture.begins, DateTimeFormatter.ISO_TIME).minute}0m"
        else
            lectureBegins.text = "${beginsHours}h:${LocalTime.parse(lecture.begins, DateTimeFormatter.ISO_TIME).minute}m"

        if (durationMinutes > 9) lectureDuration.text = "${durationHours}h:${durationMinutes}m"
        else lectureDuration.text = "${durationHours}h"

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            dataComunication = context as IDataComunication
        } catch (e: ClassCastException) {
            throw ClassCastException(e.message)
        }
    }

}