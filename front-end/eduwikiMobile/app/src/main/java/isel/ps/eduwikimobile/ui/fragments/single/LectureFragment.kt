package isel.ps.eduwikimobile.ui.fragments.single

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import isel.ps.eduwikimobile.EduWikiApplication
import isel.ps.eduwikimobile.R
import isel.ps.eduwikimobile.domain.single.CourseClass
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
    var courseClass: CourseClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.lecture_details_fragment, container, false)
        val bundle: Bundle = arguments
        lecture = bundle.getParcelable("item_selected")
        dataComunication.setLecture(lecture)
        courseClass = dataComunication.getCourseClass()

        val lectureName = view.findViewById<TextView>(R.id.lecture_details_name)
        val lectureWeekDay = view.findViewById<TextView>(R.id.insert_lecture_week_day)
        val lectureBegins = view.findViewById<TextView>(R.id.insert_lecture_begins)
        val lectureDuration = view.findViewById<TextView>(R.id.insert_lecture_duration)
        val lectureLocation = view.findViewById<TextView>(R.id.insert_lecture_location)

        mainActivity.toolbar.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        if (courseClass != null) {
            mainActivity.toolbar.title = courseClass!!.lecturedTerm + "/" + courseClass!!.className + "/" + courseClass!!.courseShortName + "/Lecture"
        } else { //TODO pedido para obter o course
            mainActivity.toolbar.title = "Lecture"
        }
        mainActivity.toolbar.subtitle = lecture.createdBy

        lectureName.text = lecture.weekDay + " Lecture"
        val hours = Duration.parse(lecture.duration).toHours()
        val minutes = Duration.parse(lecture.duration).minusHours(hours).toMinutes()
        lectureWeekDay.text = lecture.weekDay
        lectureLocation.text = lecture.location
        lectureBegins.text = "${LocalTime.parse(lecture.begins, DateTimeFormatter.ISO_TIME).hour}:${LocalTime.parse(lecture.begins, DateTimeFormatter.ISO_TIME).minute}"
        if(minutes > 9) lectureDuration.text = "${hours}h:${minutes}m"
        else lectureDuration.text = "${hours}h:${minutes}0m"

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